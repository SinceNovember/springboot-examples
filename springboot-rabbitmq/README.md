### 1. 🔍环境配置
第一节我们先来搞一下环境的配置，上一篇中我们已经引入了自动配置的包，我们既然使用了自动配置的方式，那RabbitMQ的连接信息我们直接放在配置文件中就行了，就像我们需要用到JDBC连接的时候去配置一下DataSource一样。

![image](https://segmentfault.com/img/remote/1460000023564864)

如图所示，我们只需要指明一下连接的IP+端口号和用户名密码就行了，这里我用的是默认的用户名与密码，不写的话默认也都是guest，端口号也是默认5672。

主要我们需要看一下手动确认消息的配置，需要配置成manual才是手动确认，日后还会有其他的配置项，眼下我们配置这一个就可以了。

接下来我们要配置一个Queue，上一篇中我们往一个名叫erduo的队列中发送消息，当时是我们手动定义的此队列，这里我们也需要手动配置，声明一个Bean就可以了。
```
@Configuration
public class RabbitmqConfig {
    @Bean
    public Queue erduo() {
        // 其三个参数：durable exclusive autoDelete
        // 一般只设置一下持久化即可
        return new Queue("erduo",true);
    }

}
```
就这么简单声明一下就可以了，当然了RabbitMQ毕竟是一个独立的组件，如果你在RabbitMQ中通过其他方式已经创建过一个名叫erduo的队列了，你这里也可以不声明，这里起到的一个效果就是如果你没有这个队列，会按照你声明的方式帮你创建这个队列。

配置完环境之后，我们就可以以SpringBoot的方式来编写生产者和消费者了。

### 2. 📕生产者与RabbitTemplate
和上一篇的节奏一样，我们先来编写生产者，不过这次我要引入一个新的工具：RabbitTemplate。

听它的这个名字就知道，又是一个拿来即用的工具类，Spring家族这点就很舒服，什么东西都给你封装一遍，让你用起来更方便更顺手。

RabbitTemplate实现了标准AmqpTemplate接口，功能大致可以分为发送消息和接受消息。

我们这里是在生产者中来用，主要就是使用它的发送消息功能：send和convertAndSend方法。
```
// 发送消息到默认的Exchange，使用默认的routing key
void send(Message message) throws AmqpException;

// 使用指定的routing key发送消息到默认的exchange
void send(String routingKey, Message message) throws AmqpException;

// 使用指定的routing key发送消息到指定的exchange
void send(String exchange, String routingKey, Message message) throws AmqpException;
```
send方法是发送byte数组的数据的模式，这里代表消息内容的对象是Message对象，它的构造方法就是传入byte数组数据，所以我们需要把我们的数据转成byte数组然后构造成一个Message对象再进行发送。
```
// Object类型，可以传入POJO
void convertAndSend(Object message) throws AmqpException;

void convertAndSend(String routingKey, Object message) throws AmqpException;

void convertAndSend(String exchange, String routingKey, Object message) throws AmqpException;
```
convertAndSend方法是可以传入POJO对象作为参数，底层是有一个MessageConverter帮我们自动将数据转换成byte类型或String或序列化类型。

所以这里支持的传入对象也只有三种：byte类型，String类型和实现了Serializable接口的POJO。

介绍完了，我们可以看一下代码：
```
@Slf4j
@Component("rabbitProduce")
public class RabbitProduce {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send() {
        String message = "Hello 我是作者和耳朵，欢迎关注我。" + LocalDateTime.now().toString();

        System.out.println("Message content : " + message);

        // 指定消息类型
        MessageProperties props = MessagePropertiesBuilder.newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN).build();

        rabbitTemplate.send(Producer.QUEUE_NAME,new Message(message.getBytes(StandardCharsets.UTF_8),props));
        System.out.println("消息发送完毕。");
    }

    public void convertAndSend() {
        User user = new User();

        System.out.println("Message content : " + user);

        rabbitTemplate.convertAndSend(Producer.QUEUE_NAME,user);
        System.out.println("消息发送完毕。");
    }

}
```
这里我特意写明了两个例子，一个用来测试send，另一个用来测试convertAndSend。

send方法里我们看下来和之前的代码是几乎一样的，定义一个消息，然后直接send，但是这个构造消息的构造方法可能比我们想的要多一个参数，我们原来说的只要把数据转成二进制数组放进去即可，现在看来还要多放一个参数了。

MessageProperties，是的我们需要多放一个MessageProperties对象，从他的名字我们也可以看出它的功能就是附带一些参数，但是某些参数是少不了的，不带不行。

比如我的代码这里就是设置了一下消息的类型，消息的类型有很多种可以是二进制类型，文本类型，或者序列化类型，JSON类型，我这里设置的就是文本类型，指定类型是必须的，也可以为我们拿到消息之后要将消息转换成什么样的对象提供一个参考。

convertAndSend方法就要简单太多，这里我放了一个User对象拿来测试用，直接指定队列然后放入这个对象即可。

**Tips**：User必须实现Serializable接口，不然的话调用此方法的时候会抛出IllegalArgumentException异常。

代码完成之后我们就可以调用了，这里我写一个测试类进行调用：
```
@SpringBootTest
public class RabbitProduceTest {
    @Autowired
    private RabbitProduce rabbitProduce;

    @Test
    public void sendSimpleMessage() {
        rabbitProduce.send();
        rabbitProduce.convertAndSend();
    }
}
```
效果如下图~

![image](https://segmentfault.com/img/remote/1460000023564863)

同时在控制台使用命令rabbitmqctl.bat list_queues查看队列-erduo现在的情况：

![image](https://segmentfault.com/img/remote/1460000023564865)

如此一来，我们的生产者测试就算完成了，现在消息队列里两条消息了，而且消息类型肯定不一样，一个是我们设置的文本类型，一个是自动设置的序列化类型。

### 3. 📗消费者与RabbitListener
既然队列里面已经有消息了，接下来我们就要看我们该如何通过新的方式拿到消息并消费与确认了。

消费者这里我们要用到@RabbitListener来帮我们拿到指定队列消息，它的用法很简单也很复杂，我们可以先来说简单的方式，直接放到方法上，指定监听的队列就行了。
```
@Slf4j
@Component("rabbitConsumer")
public class RabbitConsumer {

    @RabbitListener(queues = Producer.QUEUE_NAME)
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println("Message content : " + message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        System.out.println("消息已确认");
    }

}
```
这段代码就代表onMessage方法会处理erduo(Producer.QUEUE_NAME是常量字符串"erduo")队列中的消息。

我们可以看到这个方法里面有两个参数，Message和Channel，如果用不到Channel可以不写此参数，但是Message消息一定是要的，它代表了消息本身。

我们可以想想，我们的程序从RabbitMQ之中拉回一条条消息之后，要以怎么样的方式展示给我们呢？

没错，就是封装为一个个Message对象，这里面放入了一条消息的所有信息，数据结构是什么样一会我一run你就能看到了。

同时这里我们使用Channel做一个消息确认的操作，这里的DeliveryTag代表的是这个消息在队列中的序号，这个信息存放在MessageProperties中。

### 4. 📖SpringBoot 启动！
编写完生产者和消费者，同时已经运行过生产者往消息队列里面放了两条信息，接下来我们可以直接启动消息，查看消费情况：

![image](https://segmentfault.com/img/remote/1460000023564866)

在我红色框线标记的地方可以看到，因为我们有了消费者所以项目启动后先和RabbitMQ建立了一个连接进行监听队列。

随后就开始消费我们队列中的两条消息：

第一条信息是==contentType=text/plain==类型，所以直接就在控制台上打印出了具体内容。

第二条信息是==contentType=application/x-java-serialized-object==，在打印的时候只打印了一个内存地址+字节大小。

不管怎么说，数据我们是拿到了，也就是代表我们的消费是没有问题的，同时也都进行了消息确认操作，从数据上看，整个消息可以分为两部分：body和MessageProperties。

我们可以单独使用一个注解拿到这个body的内容 - **@Payload**
```
@RabbitListener(queues = Producer.QUEUE_NAME)
public void onMessage(@Payload String body, Channel channel) throws Exception {
    System.out.println("Message content : " + body);
}
```
也可以单独使用一个注解拿到MessageProperties的headers属性，headers属性在截图里也可以看到，只不过是个空的 - @Headers。
```
@RabbitListener(queues = Producer.QUEUE_NAME)
public void onMessage(@Payload String body, @Headers Map<String,Object> headers) throws Exception {
    System.out.println("Message content : " + body);
    System.out.println("Message headers : " + headers);
}
```
这两个注解都算是扩展知识，我还是更喜欢直接拿到全部，全都要！！！

上面我们已经完成了消息的发送与消费，整个过程我们可以再次回想一下，一切都和我画的这张图上一样的轨迹：

![image](https://segmentfault.com/img/remote/1460000023564866)

只不过我们一直没有指定Exchage一直使用的默认路由，希望大家好好记住这张图。

### 5. 📘@RabbitListener与@RabbitHandler
下面再来补一些知识点，有关@RabbitListener与@RabbitHandler。

@RabbitListener上面我们已经简单的进行了使用，稍微扩展一下它其实是可以监听多个队列的，就像这样：
```
@RabbitListener(queues = { "queue1", "queue2" })
public void onMessage(Message message, Channel channel) throws Exception {
    System.out.println("Message content : " + message);
    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    System.out.println("消息已确认");
}
```
还有一些其他的特性如绑定之类的，这里不再赘述因为太硬编码了一般用不上。

下面来说说这节要主要讲的一个特性：@RabbitListener和@RabbitHandler的搭配使用。

前面我们没有提到，@RabbitListener注解其实是可以注解在类上的，这个注解在类上标志着这个类监听某个队列或某些队列。

这两个注解的搭配使用就要让@RabbitListener注解在类上，然后用@RabbitHandler注解在方法上，根据方法参数的不同自动识别并去消费，写个例子给大家看一看更直观一些。
```
@Slf4j
@Component("rabbitConsumer")
@RabbitListener(queues = Producer.QUEUE_NAME)
public class RabbitConsumer {

    @RabbitHandler
    public void onMessage(@Payload String message){
        System.out.println("Message content : " + message);
    }

    @RabbitHandler
    public void onMessage(@Payload User user) {
        System.out.println("Message content : " + user);
    }
}
```
大家可以看看这个例子，我们先用@RabbitListener监听erduo队列中的消息，然后使用@RabbitHandler注解了两个方法。

- 第一个方法的body类型是String类型，这就代表着这个方法只能处理文本类型的消息。
- 第二个方法的body类型是User类型，这就代表着这个方法只能处理序列化类型且为User类型的消息。

这两个方法正好对应着我们第二节中测试类会发送的两种消息，所以我们往RabbitMQ中发送两条测试消息，用来测试这段代码，看看效果：

![image](https://segmentfault.com/img/remote/1460000023564866)

都在控制台上如常打印了，如果@RabbitHandler注解的方法中没有一个的类型可以和你消息的类型对的上，比如消息都是byte数组类型，这里没有对应的方法去接收，系统就会在控制台不断的报错，如果你出现这个情况就证明你类型写的不正确。

假设你的erduo队列中会出现三种类型的消息：byte，文本和序列化，那你就必须要有对应的处理这三种消息的方法，不然消息发过来的时候就会因为无法正确转换而报错。

而且使用了@RabbitHandler注解之后就不能再和之前一样使用Message做接收类型。
```
@RabbitHandler
public void onMessage(Message message, Channel channel) throws Exception {
    System.out.println("Message content : " + message);
    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    System.out.println("消息已确认");
}
```
这样写的话会报类型转换异常的，所以二者选其一。

同时上文我的@RabbitHandler没有进行消息确认，大家可以自己试一下进行消息确认。

### 6. 📙消息的序列化转换
通过上文我们已经知道，能被自动转换的对象只有byte[]、String、java序列化对象(实现了Serializable接口的对象)，但是并不是所有的Java对象都会去实现Serializable接口，而且序列化的过程中使用的是JDK自带的序列化方法，效率低下。

所以我们更普遍的做法是：使用Jackson先将数据转换成JSON格式发送给RabbitMQ，再接收消息的时候再用Jackson将数据反序列化出来。

这样做可以完美解决上面的痛点：消息对象既不必再去实现Serializable接口，也有比较高的效率(Jackson序列化效率业界应该是最好的了)。

默认的消息转换方案是消息转换顶层接口-MessageConverter的一个子类：SimpleMessageConverter，我们如果要换到另一个消息转换器只需要替换掉这个转换器就行了。

![image](https://segmentfault.com/img/remote/1460000023564869)

上图是MessageConverter结构树的结构树，可以看到除了SimpleMessageConverter之外还有一个Jackson2JsonMessageConverter，我们只需要将它定义为Bean，就可以直接使用这个转换器了。
```
@Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter(jacksonObjectMapper);
    }
```
这样就可以了，这里的jacksonObjectMapper可以不传入，但是默认的ObjectMapper方案对JDK8的时间日期序列化会不太友好，具体可以参考我的上一篇文章：从LocalDateTime序列化探讨全局一致性序列化，总的来说就是定义了自己的ObjectMapper。

同时为了接下来测试方便，我又定义了一个专门测试JSON序列化的队列：
```
@Bean
public Queue erduoJson() {
    // 其三个参数：durable exclusive autoDelete
    // 一般只设置一下持久化即可
    return new Queue("erduo_json",true);
}
```
如此之后就可以进行测试了，先是生产者代码：
```
public void sendObject() {
        Client client = new Client();

        System.out.println("Message content : " + client);

        rabbitTemplate.convertAndSend(RabbitJsonConsumer.JSON_QUEUE,client);
        System.out.println("消息发送完毕。");
    }
```
我又重新定义了一个Client对象，它和之前测试使用的User对象成员变量都是一样的，不一样的是它没有实现Serializable接口。

同时为了保留之前的测试代码，我又新建了一个RabbitJsonConsumer，用于测试JSON序列化的相关消费代码，里面定义了一个静态变量：JSON_QUEUE = "erduo_json";

所以这段代码是将Client对象作为消息发送到"erduo_json"队列中去，随后我们在测试类中run一下进行一次发送。

紧着是消费者代码：
```
@Slf4j
@Component("rabbitJsonConsumer")
@RabbitListener(queues = RabbitJsonConsumer.JSON_QUEUE)
public class RabbitJsonConsumer {
    public static final String JSON_QUEUE = "erduo_json";

    @RabbitHandler
    public void onMessage(Client client, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        System.out.println("Message content : " + client);
        System.out.println("Message headers : " + headers);
        channel.basicAck((Long) headers.get(AmqpHeaders.DELIVERY_TAG),false);
        System.out.println("消息已确认");
    }

}
```
有了上文的经验之后，这段代码理解起来也是很简单了吧，同时给出了上一节没写的如何在@RabbitHandler模式下进行消息签收。

我们直接来看看效果：

![image](https://segmentfault.com/img/remote/1460000023564867)

![image](https://segmentfault.com/img/remote/1460000023564870)

在打印的Headers里面，往后翻可以看到contentType=application/json，这个contentType是表明了消息的类型，这里正是说明我们新的消息转换器生效了，将所有消息都转换成了JSON类型。

### 7. 🔍Exchange

![image](https://segmentfault.com/img/remote/1460000023473305)

先来放上几乎每篇都要出现一遍的我画了好久的RabbitMQ架构图。

前两篇文中我们一直没有显式的去使用Exchange，都是使用的默认Exchange，其实Exchange是一个非常关键的组件，有了它才有了各种消息分发模式。

我先简单说说Exchange有哪几种类型：

1. **fanout**：Fanout-Exchange会将它接收到的消息发往所有与他绑定的Queue中。
1. **direct**：Direct-Exchange会把它接收到的消息发往与它有绑定关系且Routingkey完全匹配的Queue中（默认）。
1. **topic**：Topic-Exchange与Direct-Exchange相似，不过Topic-Exchange不需要全匹配，可以部分匹配，它约定：Routingkey为一个句点号“. ”分隔的字符串（我们将被句点号“. ”分隔开的每一段独立的字符串称为一个单词）。
1. **header**：Header-Exchange不依赖于RoutingKey或绑定关系来分发消息，而是根据发送的消息内容中的headers属性进行匹配。此模式已经不再使用，本文中也不会去讲，大家知道即可。
本文中我们主要讲前三种Exchange方式，相信凭借着我简练的文字和灵魂的画技给大家好好讲讲，争取老妪能解。

**Tip**：本文的代码演示直接使用SpringBoot+RabbitMQ的模式。

### 8. 📕Fanout-Exchange
先来看看Fanout-Exchange，Fanout-Exchange又称扇形交换机，这个交换机应该是最容易理解的。

![image](https://segmentfault.com/img/remote/1460000023684249)

Exchange和Queue建立一个绑定关系，Exchange会分发给所有和它有绑定关系的Queue中，绑定了十个Queue就把消息复制十份进行分发。

这种绑定关系为了效率肯定都会维护一张表，从算法效率上来说一般是O(1)，所以Fanout-Exchange是这几个交换机中查找需要被分发队列最快的交换机。

下面是一段代码演示：
```
    @Bean
    public Queue fanout1() {
        return new Queue("fanout1");
    }

    @Bean
    public Queue fanout2() {
        return new Queue("fanout2");
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        // 三个构造参数：name durable autoDelete
        return new FanoutExchange("fanoutExchange", false, false);
    }

    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(fanout1()).to(fanoutExchange());
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(fanout2()).to(fanoutExchange());
    }
```
为了清晰明了，我新建了两个演示用的队列，然后建了一个FanoutExchange，最后给他们都设置上绑定关系，这样一组队列和交换机的绑定设置就算完成了。

紧接着编写一下生产者和消费者：
```
    public void sendFanout() {
        Client client = new Client();

        // 应读者要求，以后代码打印的地方都会改成log方式，这是一种良好的编程习惯，用System.out.println一般是不推荐的。
        log.info("Message content : " + client);

        rabbitTemplate.convertAndSend("fanoutExchange",null,client);
        System.out.println("消息发送完毕。");
    }

    @Test
    public void sendFanoutMessage() {
        rabbitProduce.sendFanout();
    }
```
```
@Slf4j
@Component("rabbitFanoutConsumer")
public class RabbitFanoutConsumer {
    @RabbitListener(queues = "fanout1")
    public void onMessage1(Message message, Channel channel) throws Exception {
        log.info("Message content : " + message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("消息已确认");
    }

    @RabbitListener(queues = "fanout2")
    public void onMessage2(Message message, Channel channel) throws Exception {
        log.info("Message content : " + message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("消息已确认");
    }

}
``` 
这两段代码都很好理解，不再赘述，有遗忘的可以去看RabbitMQ第一弹的内容。

其中发送消息的代码有三个参数，第一个参数是Exchange的名称，第二个参数是routingKey的名称，这个参数在扇形交换机里面用不到，在其他两个交换机类型里面会用到。

代码的准备到此结束，我们可以运行发送方法之后run一下了~

项目启动后，我们可以先来观察一下队列与交换机的绑定关系有没有生效，我们在RabbitMQ控制台使用rabbitmqctl list_bindings命令查看绑定关系。

![image](https://segmentfault.com/img/remote/1460000023684252)        

关键部分我用红框标记了起来，这就代表着名叫fanoutExchange的交换机绑定着两个队列，一个叫fanout1，另一个叫fanout2。

紧接着，我们来看控制台的打印情况：

![image](https://segmentfault.com/img/remote/1460000023684251)

可以看到，一条信息发送出去之后，两个队列都接收到了这条消息，紧接着由我们的两个消费者消费。

**Tip**： 如果你的演示应用启动之后没有消费信息，可以尝试重新运行一次生产者的方法发送消息。

### 9. 📗Direct-Exchange
Direct-Exchange是一种精准匹配的交换机，我们之前一直使用默认的交换机，其实默认的交换机就是Direct类型。

如果将Direct交换机都比作一所公寓的管理员，那么队列就是里面的住户。(绑定关系)

管理员每天都会收到各种各样的信件(消息)，这些信件的地址不光要标明地址(ExchangeKey)还需要标明要送往哪一户(routingKey)，不然消息无法投递。

![image](https://segmentfault.com/img/remote/1460000023684250)


以上图为例，准备一条消息发往名为SendService的直接交换机中去，这个交换机主要是用来做发送服务，所以其绑定了两个队列，SMS队列和MAIL队列，用于发送短信和邮件。

我们的消息除了指定ExchangeKey还需要指定routingKey，routingKey对应着最终要发送的是哪个队列，我们的示例中的routingKey是sms，这里这条消息就会交给SMS队列。

听了上面这段，可能大家对routingKey还不是很理解，我们上段代码实践一下，大家应该就明白了。

准备工作：
```
    @Bean
    public Queue directQueue1() {
        return new Queue("directQueue1");
    }

    @Bean
    public Queue directQueue2() {
        return new Queue("directQueue2");
    }

    @Bean
    public DirectExchange directExchange() {
        // 三个构造参数：name durable autoDelete
        return new DirectExchange("directExchange", false, false);
    }

    @Bean
    public Binding directBinding1() {
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with("sms");
    }

    @Bean
    public Binding directBinding2() {
        return BindingBuilder.bind(directQueue2()).to(directExchange()).with("mail");
    }
```
新建两个队列，新建了一个直接交换机，并设置了绑定关系。

这里的示例代码和上面扇形交换机的代码很像，唯一可以说不同的就是绑定的时候多调用了一个with将routingKey设置了上去。

所以是交换机和队列建立绑定关系的时候设置的routingKey，一个消息到达交换机之后，交换机通过消息上带来的routingKey找到自己与队列建立绑定关系时设置的routingKey，然后将消息分发到这个队列去。

生产者：
```
    public void sendDirect() {
        Client client = new Client();

        log.info("Message content : " + client);

        rabbitTemplate.convertAndSend("directExchange","sms",client);
        System.out.println("消息发送完毕。");
    }
```
消费者：
```
@Slf4j
@Component("rabbitDirectConsumer")
public class RabbitDirectConsumer {
    @RabbitListener(queues = "directQueue1")
    public void onMessage1(Message message, Channel channel) throws Exception {
        log.info("Message content : " + message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("消息已确认");
    }

    @RabbitListener(queues = "directQueue2")
    public void onMessage2(Message message, Channel channel) throws Exception {
        log.info("Message content : " + message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("消息已确认");
    }

}
```
效果图如下：

![image](https://segmentfault.com/img/remote/1460000023684253)

只有一个消费者进行了消息，符合我们的预期。

### 10. 📙Topic-Exchange
Topic-Exchange是直接交换机的模糊匹配版本，Topic类型的交换器，支持使用"*"和"#"通配符定义模糊bindingKey，然后按照routingKey进行模糊匹配队列进行分发。

- ==*==：能够模糊匹配一个单词。
- ==#==：能够模糊匹配零个或多个单词。
- 因为加入了两个通配定义符，所以Topic交换机的routingKey也有些变化，routingKey可以使用.将单词分开。

这里我们直接来用一个例子说明会更加的清晰：

准备工作：
```
    // 主题交换机示例
    @Bean
    public Queue topicQueue1() {
        return new Queue("topicQueue1");
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue("topicQueue2");
    }

    @Bean
    public TopicExchange topicExchange() {
        // 三个构造参数：name durable autoDelete
        return new TopicExchange("topicExchange", false, false);
    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("sms.*");
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("mail.#");
    }
```
新建两个队列，新建了一个Topic交换机，并设置了绑定关系。

这里的示例代码我们主要看设置routingKey，这里的routingKey用上了通配符，且中间用.隔开，这就代表topicQueue1消费sms开头的消息，topicQueue2消费mail开头的消息，具体不同往下看。

生产者：
```
    public void sendTopic() {
        Client client = new Client();

        log.info("Message content : " + client);

        rabbitTemplate.convertAndSend("topicExchange","sms.liantong",client);
        System.out.println("消息发送完毕。");
    }
```
消费者：
```
@Slf4j
@Component("rabbitTopicConsumer")
public class RabbitTopicConsumer {
    @RabbitListener(queues = "topicQueue1")
    public void onMessage1(Message message, Channel channel) throws Exception {
        log.info("Message content : " + message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("消息已确认");
    }

    @RabbitListener(queues = "topicQueue2")
    public void onMessage2(Message message, Channel channel) throws Exception {
        log.info("Message content : " + message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("消息已确认");
    }

}
```
这里我们的生产者发送的消息**routingKey**是**sms.liantong**，它就会被发到**topicQueue1**队列中去，这里消息的routingKey也需要用.隔离开，用其他符号无法正确识别。

如果我们的**routingKey**是**sms.123.liantong**，那么它将无法找到对应的队列，因为**topicQueue1**的模糊匹配用的通配符是*而不是#，只有#是可以匹配多个单词的。

Topic-Exchange和Direct-Exchange很相似，我就不再赘述了，通配符*和#的区别也很简单，大家可以自己试一下。