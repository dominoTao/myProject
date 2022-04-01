package com.example.myproject.aspect;

import com.example.myproject.annotation.DistributeLock;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class DistributeLockAspect {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Pointcut("execution(public * com.example.myproject.controller.*.*(..))")
    public void distributeLockPointCut(){

    }

    @Around("distributeLockPointCut()&&@annotation(distributeLock)")
    public Object distributeLockAdvice(ProceedingJoinPoint joinPoint, DistributeLock distributeLock) throws Throwable {
        log.info("分布式锁-环绕通知-执行前：{}, 入参：{}", joinPoint.getClass().getSimpleName(), Arrays.toString(joinPoint.getArgs()));
        // TODO 解析key
        String key = distributeLock.key();
        String keyValue = lock(key, distributeLock.timeout(), distributeLock.timeUnit());
        if (StringUtil.isNullOrEmpty(keyValue)) {
            log.info("分布式锁-环绕通知-获取锁失败");
            return null;
        }
        try {
            Object proceed = joinPoint.proceed();
            log.info("分布式锁-环绕通知-执行后：{}, 返回值：{}", joinPoint.getClass().getSimpleName(), proceed);
            return proceed;
        } catch (Throwable throwable) {
            log.error("分布式锁-环绕通知-key：{}", key, throwable);
            return null;
        } finally {
            unLock(key, keyValue);
        }
    }

    // 获取锁
    private String lock(String key, long timeout, TimeUnit timeUnit) {
        try {
            final String value = UUID.randomUUID().toString();
            boolean lockStat = stringRedisTemplate.execute((RedisCallback<Boolean>) connect -> connect.set(key.getBytes(StandardCharsets.UTF_8),
                    value.getBytes(StandardCharsets.UTF_8), Expiration.from(timeout, timeUnit), RedisStringCommands.SetOption.SET_IF_ABSENT));
            if (!lockStat) {
                log.info("分布式锁-获取锁失败-key:{}, 状态:{}", key, lockStat);
                return null;
            }
            log.info("分布式锁-获取锁成功-key:{}, value:{}", key, value);
            return value;
        } catch (Exception e) {
            log.error("分布式锁-获取锁失败-key:{}, 异常信息:{}", key, e);
            return null;
        }
    }

    // 释放锁
    private void unLock(String key, String value){
        try {
            // 定义 lua 脚本， 尝试获取锁
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            boolean unLockStat = stringRedisTemplate.execute((RedisCallback<Boolean>) connect -> connect.eval(luaScript.getBytes(StandardCharsets.UTF_8), ReturnType.BOOLEAN,
                    1, key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8)));
            if (!unLockStat) {
                log.info("分布式锁-释放锁失败-key:{}, 状态:{}, 超时自动释放", key, unLockStat);
            }else {
                log.info("分布式锁-释放锁成功-key:{}, 状态:{}", key, unLockStat);
            }
        } catch (Exception e) {
            log.error("分布式锁-释放锁失败-key:{}, 异常信息:{}", key, e);
        }
    }
}
