package com.simple.netty.prod.server.acceptor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import com.simple.netty.common.NettyEvent;
import com.simple.netty.common.ServiceThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NettyEventExecutor extends ServiceThread {

    private final LinkedBlockingQueue<NettyEvent> eventQueue = new LinkedBlockingQueue<NettyEvent>();

    private final ChannelEventListener channelEventListener;
    private final int maxSize = 10000;

    NettyEventExecutor(ChannelEventListener channelEventListener) {
        this.channelEventListener = channelEventListener;
    }


    public void putNettyEvent(final NettyEvent event) {
        if (this.eventQueue.size() <= maxSize) {
            this.eventQueue.add(event);
        } else {
            System.out.println("event queue size[{}] enough, so drop this event %s" + this.eventQueue.size() +
                event.toString());
        }
    }

    @Override
    public String getServiceName() {
        return NettyEventExecutor.class.getSimpleName();
    }

    @Override
    public void run() {
        System.out.println(this.getServiceName() + " service started");

        while (!this.isStopped()) {
            try {
                NettyEvent event = this.eventQueue.poll(3000, TimeUnit.MILLISECONDS);
                if (event != null && channelEventListener != null) {
                    switch (event.getType()) {
                        case IDLE:
                            channelEventListener.onChannelIdle(event.getRemoteAddr(), event.getChannel());
                            break;
                        case CLOSE:
                            channelEventListener.onChannelClose(event.getRemoteAddr(), event.getChannel());
                            break;
                        case CONNECT:
                            channelEventListener.onChannelConnect(event.getRemoteAddr(), event.getChannel());
                            break;
                        case EXCEPTION:
                            channelEventListener.onChannelException(event.getRemoteAddr(), event.getChannel());
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println(this.getServiceName() + " service has exception. ");
            }
        }
        System.out.println(this.getServiceName() + " service end");
    }
}