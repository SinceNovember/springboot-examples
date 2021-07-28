package com.simple.springwebsocket.handler;

import com.simple.springwebsocket.handler.MessageHandler;
import com.simple.springwebsocket.message.SendResponse;
import com.simple.springwebsocket.message.SendToAllRequest;
import com.simple.springwebsocket.message.SendToUserRequest;
import com.simple.springwebsocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;

/**
 * 处理 SendToAllRequest 消息
 */
@Component
public class SendToAllHandler implements MessageHandler<SendToAllRequest> {


    @Override
    public void execute(WebSocketSession session, SendToAllRequest message) {
        // 这里，假装直接成功
        SendResponse sendResponse = new SendResponse();
        sendResponse.setMsgId(message.getMsgId());
        sendResponse.setCode(0);
        WebSocketUtil.send(session, SendResponse.TYPE, sendResponse);

        // 创建转发的消息
        SendToUserRequest sendToUserRequest = new SendToUserRequest();
        sendToUserRequest.setMsgId(message.getMsgId());
        sendToUserRequest.setContent(message.getContent());
        // 广播发送
        WebSocketUtil.broadcast(SendToUserRequest.TYPE, sendToUserRequest);
    }

    @Override
    public String getType() {
        return SendToAllRequest.TYPE;
    }
}
