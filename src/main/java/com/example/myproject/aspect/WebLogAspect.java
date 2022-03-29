package com.example.myproject.aspect;

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

    @Around("webLogPointCut()")
    public void aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        final Object proceed = joinPoint.proceed();

    }
}
