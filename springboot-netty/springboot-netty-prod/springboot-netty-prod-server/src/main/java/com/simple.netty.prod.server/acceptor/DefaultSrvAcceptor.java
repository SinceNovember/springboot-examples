package com.simple.netty.prod.server.acceptor;

import java.net.SocketAddress;
import com.simple.netty.common.NettyEvent;

public abstract class DefaultSrvAcceptor extends NettySrvAcceptor {

    protected final NettyEventExecutor nettyEventExecutor = new NettyEventExecutor(getChannelEventListener());

    public void putNettyEvent(final NettyEvent event) {
        nettyEventExecutor.putNettyEvent(event);
    }

    public DefaultSrvAcceptor(SocketAddress localAddress) {
        super(localAddress);
    }

    protected abstract ChannelEventListener getChannelEventListener();

}
