package com.simple.tomcatwebsocket.message;

/**
 * 广播用户加入群聊的通知 Message
 */
public class UserJoinNoticeRequest implements Message {

    public static final String TYPE = "USER_JOIN_NOTICE_REQUEST";

    /**
     * 昵称
     */
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // ... 省略 set/get 方法
}
