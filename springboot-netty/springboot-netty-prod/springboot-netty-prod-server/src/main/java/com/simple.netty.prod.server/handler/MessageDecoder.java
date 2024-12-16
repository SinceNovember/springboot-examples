package com.simple.netty.prod.server.handler;

import java.util.List;
import static com.simple.netty.common.NettyCommonProtocol.HEARTBEAT;
import static com.simple.netty.common.NettyCommonProtocol.MAGIC;
import static com.simple.netty.common.NettyCommonProtocol.REQUEST;
import static com.simple.netty.common.NettyCommonProtocol.SERVICE_1;
import static com.simple.netty.common.NettyCommonProtocol.SERVICE_2;
import static com.simple.netty.common.NettyCommonProtocol.SERVICE_3;
import static com.simple.netty.common.NettyCommonProtocol.SERVICE_4;
import static com.simple.netty.common.serializer.SerializerHolder.serializerImpl;
import com.simple.netty.common.Message;
import com.simple.netty.common.NettyCommonProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.Signal;

/**
 * 解码器，继承于ReplayingDecoder
 * @author BazingaLyn
 * @description
 * @time
 * @modifytime
 */
/**
 * **************************************************************************************************
 *                                          Protocol
 *  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *       2   │   1   │    1   │     8     │      4      │
 *  ├ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┤
 *           │       │        │           │             │
 *  │  MAGIC   Sign    Status   Invoke Id   Body Length                   Body Content              │
 *           │       │        │           │             │
 *  └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 *
 * 消息头16个字节定长
 * = 2 // MAGIC = (short) 0xbabe
 * + 1 // 消息标志位, 用来表示消息类型
 * + 1 // 空
 * + 8 // 消息 id long 类型
 * + 4 // 消息体body长度, int类型
 *//**
 * 解码器，继承于ReplayingDecoder
 */
/**
 * **************************************************************************************************
 *                                          Protocol
 *  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *       2   │   1   │    1   │     8     │      4      │
 *  ├ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┤
 *           │       │        │           │             │
 *  │  MAGIC   Sign    Status   Invoke Id   Body Length                   Body Content              │
 *           │       │        │           │             │
 *  └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 *
 * 消息头16个字节定长
 * = 2 // MAGIC = (short) 0xbabe
 * + 1 // 消息标志位, 用来表示消息类型
 * + 1 // 空
 * + 8 // 消息 id long 类型
 * + 4 // 消息体body长度, int类型
 */
public class MessageDecoder extends ReplayingDecoder<MessageDecoder.State> {

    private final NettyCommonProtocol header = new NettyCommonProtocol();

    public MessageDecoder() {
        super(State.HEADER_MAGIC);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case HEADER_MAGIC:
                checkMagic(in.readShort());
                checkpoint(State.HEADER_STATUS);
            case HEADER_SIGN:
                header.sign(in.readByte());
                checkpoint(State.HEADER_STATUS);
            case HEADER_STATUS:
                in.readByte();
                checkpoint(State.HEADER_ID);
            case HEADER_ID:
                header.id(in.readLong());
                checkpoint(State.HEADER_BODY_LENGTH);
            case HEADER_BODY_LENGTH:
                header.bodyLength(in.readInt());
            case BODY:
                switch (header.sign()) {
                    case HEARTBEAT:
                        break;
                    case REQUEST:
                    case SERVICE_1:
                    case SERVICE_2:
                    case SERVICE_3:
                    case SERVICE_4: {
                        byte[] bytes = new byte[header.bodyLength()];
                        in.readBytes(bytes);

                        Message msg = serializerImpl().readObject(bytes, Message.class);
                        msg.sign(header.sign());
                        out.add(msg);

                        break;
                    }
                    default:
                        throw new IllegalAccessException();
                }
                checkpoint(State.HEADER_MAGIC);

        }
    }

    private  void checkMagic(short magic) throws Signal {
        if (MAGIC != magic) {
            throw new IllegalArgumentException();
        }
    }

    enum State {
        HEADER_MAGIC,
        HEADER_SIGN,
        HEADER_STATUS,
        HEADER_ID,
        HEADER_BODY_LENGTH,
        BODY
    }
}
