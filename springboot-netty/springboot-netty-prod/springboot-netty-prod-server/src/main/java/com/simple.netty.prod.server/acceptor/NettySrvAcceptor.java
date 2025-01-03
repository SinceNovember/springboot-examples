package com.simple.netty.prod.server.acceptor;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.util.HashedWheelTimer;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.PlatformDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

import static io.netty.util.internal.SocketUtils.bind;

public abstract class NettySrvAcceptor implements SrvAcceptor{

    public static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    protected final SocketAddress localAddress;

    private ServerBootstrap bootstrap;
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private final int nWorkers;
    protected volatile ByteBufAllocator allocator;

    public NettySrvAcceptor(SocketAddress localAddress) {
        this(localAddress, AVAILABLE_PROCESSORS << 1);
    }

    public NettySrvAcceptor(SocketAddress localAddress, int nWorkers) {
        this.localAddress = localAddress;
        this.nWorkers = nWorkers;
    }

    protected void init() {
        ThreadFactory bossFactory = new DefaultThreadFactory("netty.acceptor.boss");
        ThreadFactory workerFactory = new DefaultThreadFactory("netty.acceptor.worker");

        boss = initEventLoopGroup(1, bossFactory);

        worker = initEventLoopGroup(nWorkers, workerFactory);
        //使用池化的directBuffer
        /**
         * 一般高性能的场景下,使用的堆外内存，也就是直接内存，使用堆外内存的好处就是减少内存的拷贝，和上下文的切换，缺点是
         * 堆外内存处理的不好容易发生堆外内存OOM
         * 当然也要看当前的JVM是否只是使用堆外内存，换而言之就是是否能够获取到Unsafe对象#PlatformDependent.directBufferPreferred()
         */
        allocator = new PooledByteBufAllocator(PlatformDependent.directBufferPreferred());

        bootstrap = new ServerBootstrap().group(boss, worker);

        bootstrap.childOption(ChannelOption.ALLOCATOR, allocator);
    }

    public void start() throws InterruptedException {
        this.start(true);
    }

    public void start(boolean sync) throws InterruptedException {
        ChannelFuture future = bind(localAddress).sync();
        System.out.println("netty acceptor server start");

        if (sync) {
            future.channel().closeFuture().sync();
        }
    }

    public SocketAddress localAddress() {
        return localAddress;
    }

    protected ServerBootstrap bootstrap() {
        return bootstrap;
    }

    public void shutdownGracefully() {
        boss.shutdownGracefully().awaitUninterruptibly();
        worker.shutdownGracefully().awaitUninterruptibly();
    }


    protected abstract EventLoopGroup initEventLoopGroup(int nThread, ThreadFactory bossFactory);

    protected abstract ChannelFuture bind(SocketAddress localAddress);



}
