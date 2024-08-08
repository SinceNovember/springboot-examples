package com.simple.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.simple.disruptor.event.LongEvent;

import java.nio.ByteBuffer;

public class LongEventProducer {
    public final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer byteBuffer) {
        long sequence = ringBuffer.next();
        long data;
        try {
            LongEvent longEvent = ringBuffer.get(sequence);
            data = byteBuffer.getLong(0);
            longEvent.setValue(data);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }  finally {
            System.out.println("生产者准备发送数据");
            ringBuffer.publish(sequence);
        }
    }

}
