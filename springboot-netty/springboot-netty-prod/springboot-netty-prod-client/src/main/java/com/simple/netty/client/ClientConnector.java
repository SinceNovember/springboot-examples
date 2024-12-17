package com.simple.netty.client;

import io.netty.channel.Channel;

public interface ClientConnector {

    Channel connect(int port, String host);

    void shutdownGracefully();
}
