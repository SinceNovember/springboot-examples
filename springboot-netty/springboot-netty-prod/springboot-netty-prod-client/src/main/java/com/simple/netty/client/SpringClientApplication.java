package com.simple.netty.client;

import static com.simple.netty.common.NettyCommonProtocol.REQUEST;
import com.simple.netty.common.Message;
import com.simple.netty.common.bean.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringClientApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringClientApplication.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        DefaultCommonClientConnector clientConnector = new DefaultCommonClientConnector();
        Channel channel = clientConnector.connect(20011, "127.0.0.1");
        User user = new User(1, "dubbo");
        Message message = new Message();
        message.sign(REQUEST);
        message.data(user);
        //获取到channel发送双方规定的message格式的信息
        channel.writeAndFlush(message).addListener(new ChannelFutureListener() {

            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    System.out.println("send fail,reason is {}" + future.cause().getMessage());
                }
            }
        });
        //防止对象处理发生异常的情况
        DefaultCommonClientConnector.MessageNonAck msgNonAck =
            new DefaultCommonClientConnector.MessageNonAck(message, channel);
        clientConnector.addNeedAckMessageInfo(msgNonAck);
    }


}
