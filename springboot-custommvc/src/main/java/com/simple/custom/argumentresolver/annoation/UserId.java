package com.simple.custom.argumentresolver.annoation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserId {
    boolean required() default true;
}