package com.simple.tomcatwebsocket.handler;

import com.simple.tomcatwebsocket.message.SendResponse;
import com.simple.tomcatwebsocket.message.SendToOneRequest;
import com.simple.tomcatwebsocket.message.SendToUserRequest;
import com.simple.tomcatwebsocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 处理 SendToOneRequest 消息
 */
@Component
public class SendToOneHandler implements MessageHandler<SendToOneRequest> {

    @Override
    public void execute(Session session, SendToOneRequest message) {
        // 这里，假装直接成功
        SendResponse sendResponse = new SendResponse();
        sendResponse.setMessage(message.getMsgId());
        sendResponse.setCode(0);

        WebSocketUtil.send(session, SendResponse.TYPE, sendResponse);

        // 创建转发的消息
        SendToUserRequest sendToUserRequest = new SendToUserRequest();
        sendToUserRequest.setMsgId(message.getMsgId());
        sendToUserRequest.setContent(message.getContent());
        // 广播发送
        WebSocketUtil.send(message.getToUser(), SendToUserRequest.TYPE, sendToUserRequest);
    }

    @Override
    public String getType() {
        return SendToOneRequest.TYPE;
    }

}