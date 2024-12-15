package com.simple.netty.common;

import java.util.concurrent.atomic.LongAdder;

public class Message {

    private static final LongAdder sequenceGenerator = new LongAdder();

    private final long sequence;
    private short sign;
    private long version;
    private Object data;

    public Message() {
        this(sequenceGenerator.longValue());
    }

    public Message(long sequence) {
        this.sequence = sequence;
    }
    public long sequence() {
        return sequence;
    }

    public short sign() {
        return sign;
    }

    public void sign(short sign) {
        this.sign = sign;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Object data() {
        return data;
    }

    public void data(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sequence=" + sequence +
                ", sign=" + sign +
                ", version=" + version +
                ", data=" + data +
                '}';
    }
}
