package com.simple.netty.prod.server.handler;

import com.simple.netty.prod.server.acceptor.DefaultCommonSrvAcceptor;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class NettyConnectManageHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);


    public  void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise future) throws Exception {
        final String local = localAddress == null ? "UNKNOWN" : localAddress.toString();
        final String remote = remoteAddress == null ? "UNKNOWN" : remoteAddress.toString();
        logger.info("NETTY CLIENT PIPELINE: CONNECT  {} => {}", local, remote);

        super.connect(ctx, remoteAddress, localAddress, future);

    }


}
