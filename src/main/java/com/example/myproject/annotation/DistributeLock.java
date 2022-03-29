package com.example.myproject.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributeLock {
    /**
     * 分布式锁的key
     */
    String key() ;

    long timeout() default 5;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
