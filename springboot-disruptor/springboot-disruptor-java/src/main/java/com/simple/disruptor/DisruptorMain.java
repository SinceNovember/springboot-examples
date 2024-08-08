package com.simple.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.simple.disruptor.event.LongEvent;

import java.nio.ByteBuffer;

import java.util.concurrent.ThreadFactory;

public class DisruptorMain {

    public static void main(String[] args) {
        EventFactory<LongEvent> eventFactory = new LongEventFactory();
        ThreadFactory threadFactory = r -> new Thread(r, "disruptor-thread");
        int ringBufferSize = 1024 * 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, threadFactory,
                ProducerType.SINGLE, new YieldingWaitStrategy());
        disruptor.handleEventsWith(new LongEventHandler());
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        LongEventProducer producer = new LongEventProducer(ringBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        for (int i = 1; i <= 100; i++) {
            byteBuffer.putLong(0, i);
            producer.onData(byteBuffer);
        }
        //10.关闭disruptor和executor
        disruptor.shutdown();
    }
}
