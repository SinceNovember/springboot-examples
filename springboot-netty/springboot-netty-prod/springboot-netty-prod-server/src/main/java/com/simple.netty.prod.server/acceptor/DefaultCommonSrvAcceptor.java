package com.simple.netty.prod.server.acceptor;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import com.simple.netty.common.NativeSupport;
import com.simple.netty.common.NettyEvent;
import com.simple.netty.common.NettyEventType;
import com.simple.netty.prod.server.handler.MessageDecoder;
import com.simple.netty.prod.server.handler.MessageEncoder;
import com.simple.netty.prod.server.handler.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 基本的常用的netty Server配置
 */
public class DefaultCommonSrvAcceptor extends DefaultSrvAcceptor {


    //acceptor的trigger
    private final AcceptorIdleStateTrigger idleStateTrigger = new AcceptorIdleStateTrigger();

    private final MessageEncoder encoder = new MessageEncoder();

    private final AcknowledgeEncoder ackEncoder = new AcknowledgeEncoder();

    private final MessageHandler messageHandler = new MessageHandler();

    private final ChannelEventListener channelEventListener;

    public DefaultCommonSrvAcceptor(int port, ChannelEventListener channelEventListener) {
        super(new InetSocketAddress(port));
        this.init();
        this.channelEventListener = channelEventListener;
    }

    @Override
    protected void init() {
        super.init();

        /**
         * backlog参数的含义:
         * 一个未完成连接的队列，此队列维护着那些已收到了客户端SYN分节信息，等待完成三路握手的连接，socket的状态是SYN_RCVD
         * .一个已完成的连接的队列，此队列包含了那些已经完成三路握手的连接，socket的状态是ESTABLISHED
         * backlog参数历史上被定义为上面两个队列的大小之和
         * 当客户端的第一个SYN到达的时候，TCP会在未完成队列中增加一个新的记录然后回复给客户端三路握手中的第二个分节(服务端的SYN和针对客户端的ACK)
         * ，这条记录会在未完成队列中一直存在，直到三路握手中的最后一个分节到达，或者直到超时(Berkeley时间将这个超时定义为75秒)
         * 如果当客户端SYN到达的时候队列已满，TCP将会忽略后续到达的SYN，但是不会给客户端发送RST信息，因为此时允许客户端重传SYN分节，如果返回错误
         * 信息，那么客户端将无法分清到底是服务端对应端口上没有相应应用程序还是服务端对应端口上队列已满这两种情况
         */
        bootstrap().option(ChannelOption.SO_BACKLOG, 32768)
            /**
             * [TCP/IP协议详解]中描述:
             * 当TCP执行一个主动关闭, 并发回最后一个ACK ,该连接必须在TIME_WAIT状态停留的时间为2倍的MSL.
             * 这样可让TCP再次发送最后的ACK以防这个ACK丢失(另一端超时并重发最后的FIN).
             * 这种2MSL等待的另一个结果是这个TCP连接在2MSL等待期间, 定义这个连接的插口对(TCP四元组)不能再被使用.
             * 这个连接只能在2MSL结束后才能再被使用.
             *
             * 许多具体的实现中允许一个进程重新使用仍处于2MSL等待的端口(通常是设置选项SO_REUSEADDR),
             * 但TCP不能允许一个新的连接建立在相同的插口对上。
             */
            .option(ChannelOption.SO_REUSEADDR, true)
            //
            .childOption(ChannelOption.SO_REUSEADDR, true)
            /**
             * 为TCP套接字设置keepalive选项时, 如果在2个小时（实际值与具体实现有关）内在
             * 任意方向上都没有跨越套接字交换数据, 则 TCP 会自动将 keepalive 探头发送到对端.
             * 此探头是对端必须响应的TCP段.
             *
             * 期望的响应为以下三种之一:
             * 1. 收到期望的对端ACK响应
             *      不通知应用程序(因为一切正常), 在另一个2小时的不活动时间过后，TCP将发送另一个探头。
             * 2. 对端响应RST
             *      通知本地TCP对端已崩溃并重新启动, 套接字被关闭.
             * 3. 对端没有响
             *      套接字被关闭。
             *
             * 此选项的目的是检测对端主机是否崩溃, 仅对TCP套接字有效.
             */
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            /**
             * 对此连接禁用 Nagle 算法.
             * 在确认以前的写入数据之前不会缓冲写入网络的数据. 仅对TCP有效.
             *
             * Nagle算法试图减少TCP包的数量和结构性开销, 将多个较小的包组合成较大的包进行发送.
             * 但这不是重点, 关键是这个算法受TCP延迟确认影响, 会导致相继两次向连接发送请求包,
             * 读数据时会有一个最多达500毫秒的延时.
             *
             * 这叫做“ACK delay”, 解决办法是设置TCP_NODELAY。
             */
            .childOption(ChannelOption.TCP_NODELAY, true)
            /**
             * 禁用掉半关闭的状态的链接状态
             * TCP四次握手关闭连接的时候，step2-step3中出现的状态
             */
            .childOption(ChannelOption.ALLOW_HALF_CLOSURE, false);
    }

    @Override
    protected EventLoopGroup initEventLoopGroup(int nThread, ThreadFactory bossFactory) {
        return NativeSupport.isSupportNativeET() ? new EpollEventLoopGroup(nThread, bossFactory) :
            new NioEventLoopGroup(nThread, bossFactory);
    }

    @Override
    protected ChannelFuture bind(SocketAddress localAddress) {
        ServerBootstrap boot = bootstrap();
        boot.channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                        //每隔60s的时间内如果没有接受到任何的read事件的话，则会触发userEventTriggered事件，并指定IdleState的类型为READER_IDLE
                        new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS),
                        idleStateTrigger,
                        new MessageDecoder(),
                        encoder,
                        ackEncoder,
                        new NettyConnectManageHandler(),
                        messageHandler);
                }
            });
        return boot.bind(localAddress);
    }

    @Override
    protected ChannelEventListener getChannelEventListener() {
        return channelEventListener;
    }

    public class NettyConnectManageHandler extends ChannelDuplexHandler {

        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
                            ChannelPromise future) throws Exception {
            final String local = localAddress == null ? "UNKNOWN" : localAddress.toString();
            final String remote = remoteAddress == null ? "UNKNOWN" : remoteAddress.toString();
            System.out.printf("NETTY CLIENT PIPELINE: CONNECT  %s => %s%n", local, remote);

            super.connect(ctx, remoteAddress, localAddress, future);

            if (DefaultCommonSrvAcceptor.this.channelEventListener != null) {
                DefaultCommonSrvAcceptor.this.putNettyEvent(
                    new NettyEvent(NettyEventType.CONNECT, remoteAddress.toString(), ctx.channel()));
            }
        }

        @Override
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
            final String remoteAddress = ctx.channel().remoteAddress().toString();
            System.out.printf("NETTY CLIENT PIPELINE: DISCONNECT {}", remoteAddress);
            super.disconnect(ctx, future);

            if (DefaultCommonSrvAcceptor.this.channelEventListener != null) {
                DefaultCommonSrvAcceptor.this.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress
                    .toString(), ctx.channel()));
            }
        }

        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            final String remoteAddress = ctx.channel().remoteAddress().toString();
            System.out.printf("NETTY CLIENT PIPELINE: CLOSE {}", remoteAddress);
            super.close(ctx, promise);

            if (DefaultCommonSrvAcceptor.this.channelEventListener != null) {
                DefaultCommonSrvAcceptor.this.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress
                    .toString(), ctx.channel()));
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            final String remoteAddress = ctx.channel().remoteAddress().toString();
            System.out.println("NETTY CLIENT PIPELINE: exceptionCaught {}" + remoteAddress);
            System.out.println("NETTY CLIENT PIPELINE: exceptionCaught exception." + cause);
            if (DefaultCommonSrvAcceptor.this.channelEventListener != null) {
                DefaultCommonSrvAcceptor.this.putNettyEvent(new NettyEvent(NettyEventType.EXCEPTION, remoteAddress
                    .toString(), ctx.channel()));
            }
        }
    }
}
