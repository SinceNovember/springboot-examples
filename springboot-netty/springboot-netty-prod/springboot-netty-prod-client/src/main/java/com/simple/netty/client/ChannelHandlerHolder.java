package com.simple.netty.client;

import io.netty.channel.ChannelHandler;

public interface ChannelHandlerHolder {
    ChannelHandler[] handlers();
}
