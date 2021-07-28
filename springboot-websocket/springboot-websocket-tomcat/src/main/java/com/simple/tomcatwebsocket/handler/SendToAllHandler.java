package com.simple.tomcatwebsocket.handler;

import com.simple.tomcatwebsocket.message.SendResponse;
import com.simple.tomcatwebsocket.message.SendToAllRequest;
import com.simple.tomcatwebsocket.message.SendToUserRequest;
import com.simple.tomcatwebsocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * 处理 SendToAllRequest 消息
 */
@Component
public class SendToAllHandler implements MessageHandler<SendToAllRequest> {


    @Override
    public void execute(Session session, SendToAllRequest message) {
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
