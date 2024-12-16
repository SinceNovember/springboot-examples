package com.simple.netty.prod.server.handler;

import com.simple.netty.common.Acknowledge;
import com.simple.netty.common.Message;
import com.simple.netty.prod.server.acceptor.AcceptorIdleStateTrigger;
import com.simple.netty.prod.server.acceptor.ChannelEventListener;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<Message> {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        Channel channel = ctx.channel();
        logger.info(msg.toString());
        channel.writeAndFlush(new Acknowledge(msg.sequence()))
                .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}
