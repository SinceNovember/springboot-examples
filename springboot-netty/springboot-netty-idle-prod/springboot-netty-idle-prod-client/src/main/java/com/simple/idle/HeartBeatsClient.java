package com.simple.idle;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import org.springframework.stereotype.Component;

@Component
public class HeartBeatsClient {

    protected final HashedWheelTimer timer = new HashedWheelTimer();

    private Bootstrap boot;

    private final ConnectorIdleStateTrigger trigger = new ConnectorIdleStateTrigger();

    public void connect(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        boot = new Bootstrap();
        boot.group(group)
            .channel(NioSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.INFO));

        final ConnectionWatchdog watchdog = new ConnectionWatchdog(boot, timer, port,host, true) {

            public ChannelHandler[] handlers() {
                return new ChannelHandler[] {
                    this,
                    new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
                    trigger,
                    new StringDecoder(),
                    new StringEncoder(),
                    new HeartBeatClientHandler()
                };
            }
        };

        ChannelFuture future;
        //进行连接
        try {
            for (int i = 0; i < 5; i++) {
                synchronized (boot) {
                    boot.handler(new ChannelInitializer<Channel>() {

                        //初始化channel
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(watchdog.handlers());
                        }
                    });

                    future = boot.connect(host,port);
                }
                future.sync();
                Channel channel = future.channel();
                System.out.println(channel);
            }


            // 以下代码在synchronized同步块外面是安全的
        } catch (Throwable t) {
            throw new Exception("connects to  fails", t);
        }
    }

    @PostConstruct
    public void start() throws Exception {
        connect(8080, "127.0.0.1");
    }


    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new HeartBeatsClient().connect(port, "127.0.0.1");
    }
}
