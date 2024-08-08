package com.simple.disruptor;

import com.lmax.disruptor.EventHandler;
import com.simple.disruptor.event.LongEvent;

public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        System.out.println("消费者：" + longEvent.getValue());
    }
}
