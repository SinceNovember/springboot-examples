package com.rocketmq.message;

public class Demo05Message {

    public static final String TOPIC = "DEMO_05";

    /**
     * 编号
     */
    private Integer id;

    // ... 省略 set/get/toString 方法


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}