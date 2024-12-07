package com.simple.disruptor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.*;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * lmax.disruptor 高效队列处理模板. 支持初始队列，即在init()前进行发布。
 *
 * 调用init()时才真正启动线程开始处理 系统退出自动清理资源.

 */
public abstract class BaseQueueHelper<D, E extends ValueWrapper<D>, H extends WorkHandler<E>> {

    /**
     * 记录所有的队列，系统退出时统一清理资源
     */
    private static List<BaseQueueHelper> queueHelperList = new ArrayList<BaseQueueHelper>();
    /**
     * Disruptor 对象
     */
    private Disruptor<E> disruptor;
    /**
     * RingBuffer
     */
    private RingBuffer<E> ringBuffer;
    /**
     * initQueue
     */
    private List<D> initQueue = new ArrayList<D>();

    /**
     * 队列大小
     *
     * @return 队列长度，必须是2的幂
     */
    protected abstract int getQueueSize();

    /**
     * 事件工厂
     *
     * @return EventFactory
     */
    protected abstract EventFactory eventFactory();

    /**
     * 事件消费者
     *
     * @return WorkHandler[]
     */
    protected abstract WorkHandler[] getHandler();

    public EventHandler[] getEventHandlers() {
        return new EventHandler[]{};
    }


    /**
     * 初始化
     */
    public void init() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("DisruptorThreadPool").build();
        disruptor = new Disruptor<E>(eventFactory(), getQueueSize(), namedThreadFactory, ProducerType.SINGLE, getStrategy());
        disruptor.setDefaultExceptionHandler(new MyHandlerException());
        /**
         * 独立消费者
         * 生产者生产20条消息，由两个消费者各自独立消费20条，两者加起共40
         * 调用handleEventsWith，表示创建的多个消费者，每个都是独立消费的
         * 可以定义不同的消费者处理器，也可使用相同的处理器。
         * 实际场景中应该多数使用不同的处理器，因为正常来讲独立消费者做的应该是不同的事。
         * 所以本例中是定义了两个不同的消费者DisruptorEventIndHandler0和DisruptorEventIndHandler1
         */
//        disruptor.handleEventsWith(getEventHandlers());

        /**
         * 共同消费者
         * 生产者生产20条消息，由两个消费者共同消费，两者加起共20
         * 调用handleEventsWithWorkerPool，表示创建的多个消费者以共同消费的模式消费；
         * 单个消费者时可保证其有序性，多个时无法保证其顺序；
         * 或者说每个消费者是有序的，但每个消费者间是并行执行的，所以无法保证整体的有序
         * 共同消费者做的应该是同个事，所以本例中只定义了一个共同消费者DisruptorEventCommHandler
         */
        disruptor.handleEventsWithWorkerPool(getHandler());
        ringBuffer = disruptor.start();

        //初始化数据发布
        for (D data : initQueue) {
            ringBuffer.publishEvent(new EventTranslatorOneArg<E, D>() {
                @Override
                public void translateTo(E event, long sequence, D data) {
                    event.setValue(data);
                }
            }, data);
        }

        //加入资源清理钩子
        synchronized (queueHelperList) {
            if (queueHelperList.isEmpty()) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        for (BaseQueueHelper baseQueueHelper : queueHelperList) {
                            baseQueueHelper.shutdown();
                        }
                    }
                });
            }
            queueHelperList.add(this);
        }
    }

    /**
     * 如果要改变线程执行优先级，override此策略. YieldingWaitStrategy会提高响应并在闲时占用70%以上CPU，
     * 慎用SleepingWaitStrategy会降低响应更减少CPU占用，用于日志等场景.
     *
     * @return WaitStrategy
     */
    protected abstract WaitStrategy getStrategy();

    /**
     * 插入队列消息，支持在对象init前插入队列，则在队列建立时立即发布到队列处理.
     */
    public synchronized void publishEvent(D data) {
        if (ringBuffer == null) {
            initQueue.add(data);
            return;
        }
        ringBuffer.publishEvent(new EventTranslatorOneArg<E, D>() {
            @Override
            public void translateTo(E event, long sequence, D data) {
                event.setValue(data);
            }
        }, data);
    }

    /**
     * 关闭队列
     */
    public void shutdown() {
        disruptor.shutdown();
    }
}