package com.hystrix.controller;

import java.util.function.Function;
import java.util.stream.Stream;

public class TestG implements TestD<TestB>{




    public static void main(String[] args) {
        TestF<TestA, Integer> testF = s -> 123;
        new TestG().test(testF);

    }
    /**
     * 因为 第一个参数是 ? super TestB，所有main反方中可以是TestA
     * @param testF
     * @param <R>
     * @return
     */
    @Override
    public <R> R test(TestF<? super TestB, ? extends R> testF) {
        //因为入参T 等于 ? super TestB ，所有可以传入TestB 以及其子类TestC 因为super用于入参上，代表入参只写
        //并且TestA testA = new TestC();  TestC可直接转变为TestA类
        return testF.test(new TestC());
    }
}
