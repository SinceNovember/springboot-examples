package com.simple.netty.prod.server.acceptor;

import com.simple.netty.common.Acknowledge;
import com.simple.netty.common.serializer.SerializerHolder.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static com.simple.netty.common.NettyCommonProtocol.ACK;
import static com.simple.netty.common.NettyCommonProtocol.MAGIC;
import static com.simple.netty.common.serializer.SerializerHolder.serializerImpl;

public class AcknowledgeEncoder extends MessageToByteEncoder<Acknowledge> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Acknowledge ack, ByteBuf out) throws Exception {
        byte[] bytes = serializerImpl().writeObject(ack);
        out.writeByte(MAGIC)
                .writeByte(ACK)
                .writeByte(0)
                .writeLong(ack.sequence())
                .writeInt(bytes.length)
                .writeBytes(bytes);
    }
}
