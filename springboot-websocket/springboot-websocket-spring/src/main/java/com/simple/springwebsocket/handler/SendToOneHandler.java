package com.simple.springwebsocket.handler;

import com.simple.springwebsocket.message.SendResponse;
import com.simple.springwebsocket.message.SendToOneRequest;
import com.simple.springwebsocket.message.SendToUserRequest;
import com.simple.springwebsocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;

/**
 * 处理 SendToOneRequest 消息
 */
@Component
public class SendToOneHandler implements MessageHandler<SendToOneRequest> {

    @Override
    public void execute(WebSocketSession session, SendToOneRequest message) {
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