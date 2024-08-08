package com.simple.disruptor;

import com.lmax.disruptor.EventFactory;
import com.simple.disruptor.event.LongEvent;

public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }

}
