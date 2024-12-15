package com.simple.netty.prod.server.acceptor;

import java.net.SocketAddress;

public interface SrvAcceptor {

    SocketAddress localAddress();

    void start() throws InterruptedException;

    void shutdownGracefully();

    void start(boolean sync) throws InterruptedException;
}
