package com.example.myproject.aspect;

import com.alibaba.fastjson.JSON;
import com.example.myproject.annotation.WebLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class WebLogAspect {
    @Pointcut(value = "execution(* com.example.myproject.controller.*.*(..))")
    public void webLogPointCut(){}

    @Around("webLogPointCut()&&@annotation(webLog)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, WebLog webLog) throws Throwable {
        log.info("webLog-环绕通知：{}, webLog-value: {}, 是否序列化到数据库：{}", joinPoint.getClass().getSimpleName(), webLog.value(), webLog.inDB());
        final Object proceed = joinPoint.proceed();
        log.info("webLog-环绕通知：{}, 方法体执行结果：{}", joinPoint.getClass().getSimpleName(), JSON.toJSONString(proceed));
        return proceed;
    }
}
