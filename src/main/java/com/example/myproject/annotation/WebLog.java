package com.example.myproject.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WebLog {
    String value();
    /**
     * 是否入库
     */
    boolean inDB() default false;
}
