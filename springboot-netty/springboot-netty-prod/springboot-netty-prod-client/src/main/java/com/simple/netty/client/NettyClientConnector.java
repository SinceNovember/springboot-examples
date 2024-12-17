package com.simple.netty.client;

import java.util.concurrent.ThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;

public abstract class NettyClientConnector implements ClientConnector {

    public static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    private Bootstrap bootstrap;
    private EventLoopGroup worker;
    private final int nWorkers;

    protected volatile ByteBufAllocator allocator;

    public NettyClientConnector() {
        this(AVAILABLE_PROCESSORS << 1);
    }

    public NettyClientConnector(int nWorkers) {
        this.nWorkers = nWorkers;
    }

    protected void init() {
        ThreadFactory workerFactory = new DefaultThreadFactory("client.connect");
        worker = initEventLoopGroup(nWorkers, workerFactory);
        bootstrap = new Bootstrap().group(worker);
    }

    protected Bootstrap bootstrap() {
        return bootstrap;
    }

    protected Object bootstrapLock() {
        return bootstrap;
    }

    public void shutdownGracefully() {
        worker.shutdownGracefully();
    }

    protected abstract EventLoopGroup initEventLoopGroup(int nWorkers, ThreadFactory workerFactory);


}
