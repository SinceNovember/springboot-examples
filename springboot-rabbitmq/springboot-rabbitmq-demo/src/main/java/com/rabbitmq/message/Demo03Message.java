package com.rabbitmq.message;

import java.io.Serializable;

public class Demo03Message implements Serializable {

    public static final String QUEUE_A = "QUEUE_DEMO_03_A";
    public static final String QUEUE_B = "QUEUE_DEMO_03_B";

    public static final String EXCHANGE = "EXCHANGE_DEMO_03";

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
