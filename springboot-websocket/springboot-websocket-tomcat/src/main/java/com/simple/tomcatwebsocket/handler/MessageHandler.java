package com.simple.tomcatwebsocket.handler;

import com.simple.tomcatwebsocket.message.Message;

import javax.websocket.Session;

/**
 * 每个客户端发起的 Message 消息类型，我们会声明对应的 MessageHandler 消息处理器。
 * 这个就类似在 SpringMVC 中，每个 API 接口对应一个 Controller 的 Method 方法。
 */
public interface MessageHandler<T extends Message> {
    /**
     * 执行处理消息
     *
     * @param session 会话
     * @param message 消息
     */
    void execute(Session session, T message);

    /**
     * @return 消息类型，即每个 Message 实现类上的 TYPE 静态字段
     */
    String getType();
}
