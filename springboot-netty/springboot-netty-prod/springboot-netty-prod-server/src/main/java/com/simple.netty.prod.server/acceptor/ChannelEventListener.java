package com.simple.netty.prod.server.acceptor;

import io.netty.channel.Channel;

public interface ChannelEventListener {

    void onChannelConnect(final String remoteAddr, final Channel channel);


    void onChannelClose(final String remoteAddr, final Channel channel);


    void onChannelException(final String remoteAddr, final Channel channel);


    void onChannelIdle(final String remoteAddr, final Channel channel);

}
