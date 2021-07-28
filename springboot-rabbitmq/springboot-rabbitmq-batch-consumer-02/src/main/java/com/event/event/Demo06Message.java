package com.event.event;

import java.io.Serializable;

public class Demo06Message implements Serializable {

    public static final String QUEUE = "QUEUE_DEMO_06";

    public static final String EXCHANGE = "EXCHANGE_DEMO_06";

    public static final String ROUTING_KEY = "ROUTING_KEY_06";

    /**
     * 编号
     */
    private Integer id;

    public Demo06Message setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Demo06Message{" +
                "id=" + id +
                '}';
    }

}