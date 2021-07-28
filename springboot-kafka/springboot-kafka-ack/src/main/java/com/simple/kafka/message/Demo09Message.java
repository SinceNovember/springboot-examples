package com.simple.kafka.message;

/**
 * 示例 04 的 Message 消息
 */
public class Demo09Message {
    public static final String TOPIC = "DEMO_09";

    /**
     * 编号
     */
    private Integer id;

    public Demo09Message setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Demo04Message{" +
                "id=" + id +
                '}';
    }
}