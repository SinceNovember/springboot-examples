package com.hystrix.controller;

import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
public  interface TestF <T, U>{

    U test(T t);
}
