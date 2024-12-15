package com.simple.netty.prod.server.acceptor;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import jdk.internal.net.http.websocket.MessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ThreadFactory;

/**
 * 基本的常用的netty Server配置
 */
public class DefaultCommonSrvAcceptor extends DefaultSrvAcceptor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultCommonSrvAcceptor.class);

    //acceptor的trigger
    private final AcceptorIdleStateTrigger idleStateTrigger = new AcceptorIdleStateTrigger();

    public DefaultCommonSrvAcceptor(SocketAddress localAddress) {
        super(localAddress);
    }

    @Override
    protected ChannelEventListener getChannelEventListener() {
        return null;
    }

    @Override
    protected EventLoopGroup initEventLoopGroup(int nThread, ThreadFactory bossFactory) {
        return null;
    }

    @Override
    protected ChannelFuture bind(SocketAddress localAddress) {
        return null;
    }

    @Override
    public SocketAddress localAddress() {
        return null;
    }

    @Override
    public void shutdownGracefully() {

    }
}
