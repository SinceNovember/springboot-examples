package com.rocketmq.message;

public class Demo03Message {

    public static final String TOPIC = "DEMO_03";

    /**
     * 编号
     */
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}