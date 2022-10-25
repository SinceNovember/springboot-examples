package com.simple.kafka.message;

/**
 * .
 *
 * @author SinceNovember
 * @date 2022/10/24
 */
public class Message {

    public static final String TOPIC = "test-topic";

    private String id;

    private String title;

    public Message setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
