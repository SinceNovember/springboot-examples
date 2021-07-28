package com.hystrix.controller;

import org.aspectj.weaver.ast.Test;

import java.util.function.Function;
import java.util.stream.Stream;

public interface TestD<T> {
    <R> R test(TestF<? super T, ? extends R> testF);

}
