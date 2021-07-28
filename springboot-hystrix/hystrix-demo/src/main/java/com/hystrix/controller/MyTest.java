package com.hystrix.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MyTest {
    public void test(){
        List<TestA> testAS = new ArrayList<>();
        List<TestC> testCS = new ArrayList<>();

        testAS.add(new TestA());
        testCS.add(new TestC());
        List<? super TestB> list = new ArrayList<TestA>(testAS);

    }
}


