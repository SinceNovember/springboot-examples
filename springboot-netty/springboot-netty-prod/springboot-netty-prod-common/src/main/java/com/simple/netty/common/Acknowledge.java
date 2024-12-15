package com.simple.netty.common;

import com.simple.netty.common.serializer.SerializerHolder;

/**
 * ACK确认
 */
public class Acknowledge {

    private long sequence; // ACK序号

    public Acknowledge() {
    }

    public Acknowledge(long sequence) {
        this.sequence = sequence;
    }

    public long sequence() {
        return sequence;
    }

    public void sequence(long sequence) {
        this.sequence = sequence;
    }
}
