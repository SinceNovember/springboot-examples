package com.simple.kafka.message;

/**
 * 示例 04 的 Message 消息
 */
public class Demo07Message {
    public static final String TOPIC = "DEMO_07";

    /**
     * 编号
     */
    private Integer id;

    public Demo07Message setId(Integer id) {
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