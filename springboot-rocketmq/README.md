RocketMQ 是由阿里巴巴团队采用 Java 语言开发，在 2016 年的时候贡献给 Apache 基金会，是 Apache 的顶级项目。
### 1）RocketMQ 的安装和运行
安装和运行 RocketMQ 需要的先决条件：
> 
> -  64 bit 的系统，推荐 Linux/Unix/Mac，Windows 系统也可以运行
> -  64 bit 的 JDK 1.8+
> -  4G 的空闲磁盘

下载 RocketMQ 4.6.1 ，打开 RocketMQ 官网 rocketmq.apache.org/release_not… ,选择二进制文件：
![image](https://user-gold-cdn.xitu.io/2020/4/12/1716c1ff1dbe75c4?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
下载完成后，解压到安装目录，打开终端进入安装目录 ```ROCKETMQ_HOME```，运行如下的命令：
##### 设置 JVM 的最小内存和最大内存
```
# 打开 runbroker.sh 或者 runbroker.cmd(Windows)
# 根据电脑内存情况设置 JVM 最大内存和最小内存
JAVA_OPT="${JAVA_OPT} -server -Xms8g -Xmx8g -Xmn4g"
```
##### 运行 Name Server
```
> nohup sh bin/mqnamesrv &
> tail -f ~/logs/rocketmqlogs/namesrv.log
# 如果成功的话可以看到这样的内容
The Name Server boot success. serializeType=JSON
```
##### 运行 Broker
```
> nohup sh bin/mqbroker -n localhost:9876 &
> tail -f ~/logs/rocketmqlogs/broker.log 
The broker[..., ...] boot success. serializeType=JSON and name server is localhost:9876
```
##### 关闭 Server
```
 > sh bin/mqshutdown broker
 > sh bin/mqshutdown namesrv
 ```
##### Windows 系统
```
# Windows 系统需要设置环境变量 %ROCKETMQ_HOME%
> cd %ROCKETMQ_HOME%\bin
> .\mqnamesrv
# 成功后会在终端看到这样子的输出
The Name Server boot success. serializeType=JSON
# 重新打开一个终端
> cd %ROCKETMQ_HOME%\bin
> .\mqbroker.cmd -n localhost:9876
The broker[..., ...] boot success. serializeType=JSON and name server is localhost:9876
# 在 Windows 关闭 Server 通过关闭终端或者 Ctrl + C 终止任务吧
```
### 2）开始使用
rocketmq-spring-boot-starter 是 Spring Boot 快速与 RocketMQ 集成的启动器（Starter），需要 Spring Boot 2.0 及更高版本。
实战 Spring Boot 整合 RocketMQ，实现写入消息（Producer）和消费消息（Consumer）。

![image](https://user-gold-cdn.xitu.io/2020/4/12/1716c1ff1dbf561e?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

#### 2.1）新建项目和共同配置
这里将新建两个项目，04-rocketmq-producer 和 04-rocketmq-consumer，分别生产信息和消费信息。Spring Boot 选择 2.1.13，依赖选择 Spring Web，其除了项目名称以外，其它配置基本相同。

![image](https://user-gold-cdn.xitu.io/2020/4/12/1716c1ff1d9d8f50?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

添加 rocketmq-spring-boot-starter：
```
// Gradle
// https://mvnrepository.com/artifact/org.apache.rocketmq/rocketmq-spring-boot-starter
compile group: 'org.apache.rocketmq', name: 'rocketmq-spring-boot-starter', version: '2.1.0'
```
```
<!-- Maven -->
<!-- https://mvnrepository.com/artifact/org.apache.rocketmq/rocketmq-spring-boot-starter -->
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
    <version>2.1.0</version>
</dependency>
```
配置 application.properties
```
# 04-rocketmq-producer 不需要设置 spring.main.web-application-type
# none 表示不启动 Web 容器
spring.main.web-application-type=none
spring.application.name=rocketmq-consumer
# RocketMQ Name Server (替换为 RocketMQ 的 IP 地址和端口号)
rocketmq.name-server=192.168.128.10:9876
# 兹定于 Name Server
boot.rocketmq.NameServer=192.168.128.10:9876
# 程序用使用到的属性配置 (替换为 RocketMQ 的 IP 地址和端口号)
boot.rocketmq.topic=string-topic
boot.rocketmq.topic.user=user-topic
boot.rocketmq.tag=tagA
```
##### 在两个项目中新建 User 类：
```
public class User {
    private String userName;
    private Byte userAge;
   	// 省略 Getter Setter
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userAge=" + userAge +
                '}';
    }
}
```
#### 2.2）Producer 实现消息的写入
项目名称 04-rocketmq-producer 。实现从 RESTful API 接收的消息写入 RocketMQ。
新建 ProducerService.class：
```
@Service
public class ProducerService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RocketMQTemplate mqTemplate;

    @Value(value = "${boot.rocketmq.topic}")
    private String springTopic;

    @Value(value = "${boot.rocketmq.topic.user}")
    private String userTopic;

    @Value(value = "${boot.rocketmq.tag}")
    private String tag;

    public SendResult sendString(String message) {
        // 发送 String 类型的消息
        // 调用 RocketMQTemplate 的 syncSend 方法
        SendResult sendResult = mqTemplate.syncSend(springTopic + ":" + tag, message);
        logger.info("syncSend String to topic {} sendResult={} \n", springTopic, sendResult);
        return sendResult;
    }

    public SendResult sendUser(User user) {
        // 发送 User
        SendResult sendResult = mqTemplate.syncSend(userTopic, user);
        logger.info("syncSend User to topic {} sendResult= {} \n", userTopic, sendResult);
        return sendResult;
    }
}
```
代码解析：
@Value(value = "${boot.rocketmq.topic}")：将 application.properties 文件中定义的 boot.rocketmq.topic 值自动注入到 springTopic 变量。
新建 RESTful API ，ProducerController.class
@RestController
@RequestMapping("/producer")
public class ProducerController {
    @Resource ProducerService producerService;

    @PostMapping("/string")
    public SendResult sendString(@RequestBody String message){
        return producerService.sendString(message);
    }

    @PostMapping("/user")
    public SendResult sendUser(@RequestBody User user){
        return producerService.sendUser(user);
    }
}
#### 2.2）Consumer 消费信息
项目名称 04-rocketmq-consumer ，实现 RocketMQ 中消息的读取与消费。注意 这个项目不需要启动 Web 容器。
StringConsumer.class 消费 String 类型的消息。
```
@Service
@RocketMQMessageListener(topic = "${boot.rocketmq.topic}", consumerGroup = "string_consumer", selectorExpression = "${boot.rocketmq.tag}")
public class StringConsumer implements RocketMQListener<String> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(String message) {
        // 重写消息处理方法
        logger.info("------- StringConsumer received:{} \n", message);
        // TODO 对消息进行处理，比如写入数据
    }
}
复制代码UserConsumer.class 消费 User 类型的消息
@Service
@RocketMQMessageListener(nameServer = "${boot.rocketmq.NameServer}", topic = "${boot.rocketmq.topic.user}", consumerGroup = "user_consumer")
public class UserConsumer implements RocketMQListener<User> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void onMessage(User user) {
        logger.info("######## user_consumer received: {} ; age: {} ; name: {} \n", user,user.getUserAge(),user.getUserName());
        // TODO 对消息进行处理User
    }
}
```
代码解析：
- ```@RocketMQMessageListener```：指定监听的 topic，consumerGroup，selectorExpression等；
- ```topic```：消息主题，表示一类的消息，比如上文的 string-topic 、```user-topic```，topic = "string-topic" 表示值消费 string-topic 主题的消息；
- ```consumerGroup```：消费组，同一个消费组一般情况消费相同的消息；
- ```selectorExpression```：选择 tag，selectorExpression="tagA"，只消费 tag 为 tagA 的消息；默认 "*"，即所有的 tag；
- ```RocketMQListener``` ： 实现 RocketMQListener，我么只需要重写消息处理方法即可；
### 3）运行项目
启动 RocketMQ，分别启动 04-rocketmq-producer 和 04-rocketmq-consumer。
Producer 运行的 Web 端口是 8080，Consumer 没有启动 Web 容器。
启动 Consumer 可以看到如下日志输出：
```
DefaultRocketMQListenerContainer{consumerGroup='user_consumer', nameServer='192.168.128.10:9876', topic='user-topic', consumeMode=CONCURRENTLY, selectorType=TAG, selectorExpression='*', messageModel=CLUSTERING}
2020-03-16 23:11:19.636  INFO 16092 --- [           main] o.a.r.s.a.ListenerContainerConfiguration : Register the listener to container, listenerBeanName:userConsumer, containerBeanName:org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer_1
2020-03-16 23:11:19.924  INFO 16092 --- [           main] a.r.s.s.DefaultRocketMQListenerContainer : running container: DefaultRocketMQListenerContainer{consumerGroup='string_consumer', nameServer='192.168.128.10:9876', topic='string-topic', consumeMode=CONCURRENTLY, selectorType=TAG, selectorExpression='tagA', messageModel=CLUSTERING}
2020-03-16 23:11:19.924  INFO 16092 --- [           main] o.a.r.s.a.ListenerContainerConfiguration : Register the listener to container, listenerBeanName:stringConsumer, containerBeanName:org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer_2
```
打开 Postname,测试 String 类型的消息，访问 http://localhost:8080/producer/string

![image](https://user-gold-cdn.xitu.io/2020/4/12/1716c1ff26958ab6?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
![image](https://user-gold-cdn.xitu.io/2020/4/12/1716c1ff26958ab6?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
Producer 日志输出：
```
2020-03-16 23:14:21.681  INFO 16776 --- [nio-8080-exec-2] org.xian.producer.ProducerService        : 
syncSend String to topic string-topic sendResult=SendResult [sendStatus=SEND_OK, msgId=0000000000000000000000000000000100001F89AB83523BF6E30000, offsetMsgId=C0A8800A00002A9F000000000003ADF2, messageQueue=MessageQueue [topic=string-topic, brokerName=master, queueId=2], queueOffset=4] 
```
Consumer 日志输出：
```
2020-03-16 23:14:21.983  INFO 16092 --- [MessageThread_1] org.xian.consumer.StringConsumer         : 
------- StringConsumer received:Hello RocketMQ By Spring Boot 0
```
测试 User 类型的消息，访问 http://localhost:8080/producer/user
![image](https://user-gold-cdn.xitu.io/2020/4/12/1716c1ff2673557f?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
![image](https://user-gold-cdn.xitu.io/2020/4/12/1716c1ff2673557f?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
Producer 日志输出：
```
2020-03-16 23:18:11.548  INFO 16776 --- [nio-8080-exec-5] org.xian.producer.ProducerService        : 
syncSend User to topic user-topic sendResult= SendResult [sendStatus=SEND_OK, msgId=0000000000000000000000000000000100001F89AB83523F79590003, offsetMsgId=C0A8800A00002A9F000000000003B11F, messageQueue=MessageQueue [topic=user-topic, brokerName=master, queueId=3], queueOffset=2] 
```
Consumer 日志输出：
```
2020-03-16 23:18:11.591  INFO 16092 --- [MessageThread_1] org.xian.consumer.UserConsumer           : 
######## user_consumer received: User{userName='RocketMQ With Spring Boot', userAge=4} ; age: 4 ; name: RocketMQ With Spring Boot 
```
