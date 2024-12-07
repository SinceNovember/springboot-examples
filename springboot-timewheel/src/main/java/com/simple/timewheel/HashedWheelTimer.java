package com.simple.timewheel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;

public class HashedWheelTimer implements Timer {

    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);

    private static final AtomicBoolean WARNED_TOO_MANY_INSTANCES = new AtomicBoolean();

    private static final int INSTANCE_COUNT_LIMIT = 64;

    /**
     * 给定的一个默认最小时间跨度时间
     */
    private static final long MILLISECOND_NANOS = TimeUnit.MILLISECONDS.toNanos(1);

    /**
     * 实际工作类，移动刻度等操作
     */
    private final Worker worker = new Worker();
    private final Thread workerThread;

    public static final int WORKER_STATE_INIT = 0;

    public static final int WORKER_STATE_STARTED = 1;

    public static final int WORKER_STATE_SHUTDOWN = 2;

    /**
     * 工作者的实时状态，用volatile来保证更新后其他线程能够实时的看见，因为在worker类里用到这个状态来进行取消指针移动
     * 0 - init, 1 - started, 2 - shut down
     */
    private volatile int workerState;

    /**
     * 保证workerState更新的原子性 （该方法跟上面的组合起来比 AtomicInteger性能更好，占用更小）
     */
    private static final AtomicIntegerFieldUpdater<HashedWheelTimer> WORKER_STATE_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimer.class, "workerState");

    /**
     * 每个刻度的转动时间
     */
    private final long tickDuration;

    /**
     * 时间轮数组
     */
    private final HashedWheelBucket[] wheel;

    /**
     * 用来 & wheel.length 需要保证是2倍数
     */
    private final int mask;

    /**
     * 用来防止主任务开始了，但worker还没进入处理状态
     */
    private final CountDownLatch startTimeInitialized = new CountDownLatch(1);

    /**
     * 所有的任务先放入到该队列中
     */
    private final Queue<HashedWheelTimeout> timeouts = new ArrayBlockingQueue<>(1024);

    /**
     * 所有的取消的任务
     */
    private final Queue<HashedWheelTimeout> cancelledTimeouts = new ArrayBlockingQueue<>(1024);

    /**
     * 所有还未处理的任务数量
     */
    private final AtomicLong pendingTimeouts = new AtomicLong(0);

    /**
     * 允许最大还未处理的数量
     */
    private final long maxPendingTimeouts;

    /**
     * 时间轮开始时间
     */
    private volatile long startTime;

    /**
     * 用来执行正在处理的任务，不然有大任务的话会阻塞后面的任务
     */
    private final ExecutorService executorService;


    public HashedWheelTimer() {
        this(Executors.defaultThreadFactory());
    }

    public HashedWheelTimer(long tickDuration, TimeUnit unit) {
        this(Executors.defaultThreadFactory(), tickDuration, unit);
    }

    public HashedWheelTimer(long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(Executors.defaultThreadFactory(), tickDuration, unit, ticksPerWheel);
    }

    public HashedWheelTimer(ThreadFactory threadFactory) {
        this(threadFactory, 100, TimeUnit.MILLISECONDS);
    }

    public HashedWheelTimer(
            ThreadFactory threadFactory, long tickDuration, TimeUnit unit) {
        this(threadFactory, tickDuration, unit, 512);
    }


    public HashedWheelTimer(
            ThreadFactory threadFactory,
            long tickDuration, TimeUnit unit, int ticksPerWheel) {
        this(threadFactory, tickDuration, unit, ticksPerWheel, -1);
    }

    public HashedWheelTimer(ThreadFactory threadFactory, long ticksPerWheel, TimeUnit unit,
                            int tickPerWheel, long maxPendingTimeouts) {
        //创建时间轮
        wheel = createWheel(tickPerWheel);
        mask = wheel.length - 1;

        long duration = unit.toNanos(ticksPerWheel);

        //保证时间跨度最小为1纳秒
        this.tickDuration = Math.max(duration, MILLISECOND_NANOS);
        executorService = Executors.newFixedThreadPool(10);
        workerThread = threadFactory.newThread(worker);
        this.maxPendingTimeouts = maxPendingTimeouts;
        if (INSTANCE_COUNTER.incrementAndGet() > INSTANCE_COUNT_LIMIT &&
                WARNED_TOO_MANY_INSTANCES.compareAndSet(false, true)) {
            System.out.println("too many instances");
        }
    }

    private HashedWheelBucket[] createWheel(int ticksPerWheel) {
        if (ticksPerWheel <= 0) {
            throw new IllegalArgumentException(
                    "ticksPerWheel must be greater than 0: " + ticksPerWheel);
        }
        if (ticksPerWheel > 1073741824) {
            throw new IllegalArgumentException(
                    "ticksPerWheel may not be greater than 2^30: " + ticksPerWheel);
        }

        //规范化时间轮的长度，改成2的倍数，方便&操作 提高性能
        ticksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
        HashedWheelBucket[] wheel = new HashedWheelBucket[ticksPerWheel];
        for (int i = 0; i < wheel.length; i++) {
            wheel[i] = new HashedWheelBucket();
        }
        return wheel;
    }

    /**
     * 规范化实践论大小，改为2的倍数
     * @param ticksPerWheel
     * @return
     */
    private static int normalizeTicksPerWheel(int ticksPerWheel) {
        int n = ticksPerWheel - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= 1073741824) ? 1073741824 : n + 1;
    }

    /**
     * 调用时间刻度，调用任务等实际操作类
     */
    private final class Worker implements Runnable {

        /**
         * 未处理的任务
         */
        private final Set<Timeout> unprocessedTimeouts = new HashSet<>();

        /**
         * 当前走到的刻度
         */
        private long tick;

        @Override
        public void run() {
            startTime = System.nanoTime();
            if (startTime == 0) {
                startTime = 1;
            }
            //唤醒阻塞在start()的线程可以开始进行任务了，保证多线程newTimeout的安全
            startTimeInitialized.countDown();

            // 只要时间轮的状态为WORKER_STATE_STARTED，就循环的“转动”tick，循环判断响应格子中的到期任务
            do {
                //获取下一刻的时间
                final long deadline = waitForNextTick();
                if (deadline > 0) {
                    //获取当前的时间轮桶的索引
                    int idx = (int) (tick & mask);
                    //从时间轮里将取消掉的任务移除
                    processCancelledTasks();
                    //获取对应实践论桶
                    HashedWheelBucket bucket = wheel[idx];
                    //将对应中的任务移动到指定的桶中
                    transferTimeoutsToBuckets();
                    //将当前刻度里的所有本轮的任务全部执行掉
                    bucket.expireTimeouts(deadline);
                    tick++;
                }
            } while (WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == WORKER_STATE_STARTED);

            /**
             * 到这边说明主线程调用了stop方法，实践论停止了，下面收集还未处理的任务用来返回
             */
            //获取桶里所有的任务添加到还未执行队列中
            for (HashedWheelBucket bucket : wheel) {
                bucket.clearTimeouts(unprocessedTimeouts);
            }

            //获取对应中所有未取消的任务添加到未执行队列中
            for (; ; ) {
                HashedWheelTimeout timeout = timeouts.poll();
                if (timeout == null) {
                    break;
                }
                if (!timeout.isCancelled()) {
                    unprocessedTimeouts.add(timeout);
                }
            }
            //移除掉桶中所有取消掉的任务
            processCancelledTasks();
        }

        /**
         * 将任务从队列中转移到指定的桶中
         */
        private void transferTimeoutsToBuckets() {
            for (int i = 0; i < 100000; i++) {
                HashedWheelTimeout timeout = timeouts.poll();
                if (timeout == null) {
                    break;
                }
                if (timeout.state() == HashedWheelTimeout.ST_CANCELLED) {
                    // Was cancelled in the meantime.
                    continue;
                }
                //计算应该放到的桶的索引位置，未模除
                long calculated = timeout.deadline / tickDuration;
                //计算需要第几圈才会执行
                timeout.remainingRounds = (calculated - tick) / wheel.length;
                //保证在当前刻度之前的桶索引
                final long ticks = Math.max(calculated, tick);
                //获取对应的桶 已模除
                HashedWheelBucket bucket = wheel[(int) (ticks & mask)];
                //将任务添加的桶的双向链表尾部
                bucket.addTimeout(timeout);
            }
        }

        /**
         * 处理所有取消掉任务，从链表中拿掉，并交给GC进行回收
         */
        private void processCancelledTasks() {
            for (; ; ) {
                HashedWheelTimeout timeout = cancelledTimeouts.poll();
                if (timeout == null) {
                    break;
                }
                try {
                    timeout.remove();
                } catch (Throwable ignore) {
                }
            }
        }

        /**
         * sleep, 直到下次tick到来, 然后返回该次tick和启动时间之间的时长
         * @return 该次tick和启动时间之间的时长
         */
        private long waitForNextTick() {
            //下次tick的时间点, 用于计算需要sleep的时间
            long deadline = tickDuration * (tick + 1);
            for (; ; ) {
                //现在跟在时间轮启动的时间差
                final long currentTime = System.nanoTime() - startTime;
                // 计算需要sleep的时间, 之所以加999999后再除10000000, 是为了保证足够的sleep时间
                // 例如：当deadline - currentTime=2000002的时候，如果不加999999，则只睡了2ms，
                // 而2ms其实是未到达deadline这个时间点的，所有为了使上述情况能sleep足够的时间，加上999999后，会多睡1ms
                long sleepTimeMs = (deadline - currentTime + 999999) / 1000000;

                if (sleepTimeMs <= 0) {
                    if (currentTime == Long.MIN_VALUE) {
                        return -Long.MAX_VALUE;
                    } else {
                        return currentTime;
                    }
                }

                try {
                    Thread.sleep(sleepTimeMs);
                } catch (InterruptedException ignored) {
                    // 调用HashedWheelTimer.stop()时优雅退出
                    if (WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == WORKER_STATE_SHUTDOWN) {
                        return Long.MIN_VALUE;
                    }
                }
            }
        }

        public Set<Timeout> unprocessedTimeouts() {
            return Collections.unmodifiableSet(unprocessedTimeouts);
        }

    }

    /**
     * 时间桶
     * 一个双向链表结构
     */
    private final class HashedWheelBucket {

        // 指向格子中任务的首尾
        private HashedWheelTimeout head;
        private HashedWheelTimeout tail;

        // 基础的链表添加操作
        public void addTimeout(HashedWheelTimeout timeout) {
            timeout.bucket = this;
            if (head == null) {
                head = tail = timeout;
            } else {
                tail.next = timeout;
                timeout.prev = tail;
                tail = timeout;
            }
        }

        // 过期并执行格子中的到期任务，tick到该格子的时候，worker线程会调用这个方法，根据deadline和remainingRounds判断任务是否过期
        public void expireTimeouts(long deadline) {
            HashedWheelTimeout timeout = head;

            // 遍历格子中的所有定时任务
            while (timeout != null) {
                HashedWheelTimeout next = timeout.next;
                if (timeout.remainingRounds <= 0) { // 定时任务到期，开始处理任务
                    next = remove(timeout);
                    if (timeout.deadline <= deadline) {
                        executorService.execute(timeout.task()); //异步执行任务，netty的是同步执行，导致如果该时间段有大任务在执行会阻塞后面的任务执行
                    } else {
                        // 如果round数已经为0，deadline却>当前格子的deadline，说放错格子了，这种情况应该不会出现
                        throw new IllegalStateException(String.format(
                                "timeout.deadline (%d) > deadline (%d)", timeout.deadline, deadline));
                    }
                } else if (timeout.isCancelled()) { //任务取消轮
                    next = remove(timeout);
                } else { //没有到期，轮数-1
                    timeout.remainingRounds--;
                }
                timeout = next;
            }
        }

        // 基础的链表移除node操作
        public HashedWheelTimeout remove(HashedWheelTimeout timeout) {
            HashedWheelTimeout next = timeout.next;
            if (timeout.prev != null) {
                timeout.prev.next = next;
            }
            if (timeout.next != null) {
                timeout.next.prev = timeout.prev;
            }
            if (timeout == head) {
                if (timeout == tail) {
                    tail = null;
                    head = null;
                } else {
                    head = next;
                }
            } else if (timeout == tail) {
                tail = timeout.prev;
            }
            timeout.prev = null;
            timeout.next = null;
            timeout.bucket = null;
            timeout.timer.pendingTimeouts.decrementAndGet();
            return next;
        }

        /**
         * 链表的poll操作 拿出第一个元素
         * @return
         */
        private HashedWheelTimeout pollTimeout() {
            HashedWheelTimeout head = this.head;
            if (head == null) {
                return null;
            }
            HashedWheelTimeout next = head.next;
            if (next == null) {
                tail = this.head = null;
            } else {
                this.head = next;
                next.prev = null;
            }
            head.next = null;
            head.prev = null;
            head.bucket = null;
            return head;
        }

        /**
         * 将所有的元素取出来并添加的set中
         * @param set
         */
        public void clearTimeouts(Set<Timeout> set) {
            for (; ; ) {
                HashedWheelTimeout timeout = pollTimeout();
                if (timeout == null) {
                    return;
                }
                if (timeout.isExpired() || timeout.isCancelled()) {
                    continue;
                }
                set.add(timeout);
            }
        }
    }

    /**
     * 任务包装器，包装了一些取消，移除相关的操作
     */
    private static final class HashedWheelTimeout implements Timeout {

        /**
         * 定义定时任务的3个状态：初始化、取消、过期
         */
        private static final int ST_INIT = 0;

        private static final int ST_CANCELLED = 1;

        private static final int ST_EXPIRED = 2;

        /**
         * 用来CAS方式更新定时任务状态
         */
        private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER =
                AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, "state");

        /**
         * 时间轮引用
         */
        private final HashedWheelTimer timer;

        /**
         * 具体到期需要执行的任务
         */
        private final TimerTask task;

        /**
         * 具体的执行时间
         */
        private final long deadline;

        private volatile int state = ST_INIT;

        // 离任务执行的轮数，当将次任务加入到格子中是计算该值，每过一轮，该值减一。
        long remainingRounds;

        // 双向链表结构，由于只有worker线程会访问，这里不需要synchronization / volatile
        HashedWheelTimeout prev;
        HashedWheelTimeout next;

        // 定时任务所在的桶
        HashedWheelBucket bucket;

        HashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline) {
            this.timer = timer;
            this.task = task;
            this.deadline = deadline;
        }

        @Override
        public Timer timer() {
            return timer;
        }

        @Override
        public TimerTask task() {
            return task;
        }

        public boolean compareAndSetState(int expected, int state) {
            return STATE_UPDATER.compareAndSet(this, expected, state);
        }

        public int state() {
            return state;
        }

        @Override
        public boolean isExpired() {
            return state() == ST_EXPIRED;
        }

        @Override
        public boolean isCancelled() {
            return state() == ST_CANCELLED;
        }

        @Override
        public boolean cancel() {
            // 这里只是修改状态为ST_CANCELLED，会在下次tick时，在格子中移除
            if (!compareAndSetState(ST_INIT, ST_CANCELLED)) {
                return false;
            }
            //添加到待取消的任务队列中，下次tick移掉
            timer.cancelledTimeouts.add(this);
            return false;
        }

        /**
         * 从格子中移除自身
         */
        public void remove() {
            HashedWheelBucket bucket = this.bucket;
            if (bucket != null) {
                bucket.remove(this);
            } else {
                timer.pendingTimeouts.decrementAndGet();
            }
        }

        /**
         * netty的执行任务方法，由于是同步的所以会慢，暂时不用此方法，该用异步执行
         */
        public void expire() {
            if (!compareAndSetState(ST_INIT, ST_EXPIRED)) {
                return;
            }
            try {
                task.run();
            } catch (Throwable ignore) {

            }
        }
    }

    /**
     * 添加任务的方法
     * @param task
     * @param delay
     * @param unit
     * @return
     */
    @Override
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        //增加待处理的任务数量
        long pendingTimeoutsCount = pendingTimeouts.incrementAndGet();

        //超过最大待处理任务额度，报错
        if (maxPendingTimeouts > 0 && pendingTimeoutsCount > maxPendingTimeouts) {
            pendingTimeouts.decrementAndGet();
            throw new RejectedExecutionException("Number of pending timeouts ("
                    + pendingTimeoutsCount + ") is greater than or equal to maximum allowed pending "
                    + "timeouts (" + maxPendingTimeouts + ")");
        }
        // 如果时间轮没有启动，则启动
        start();
        // 计算任务的deadline（执行的相对时间）
        long deadline = System.nanoTime() + unit.toNanos(delay) - startTime;
        if (delay > 0 && deadline < 0) {
            deadline = Long.MAX_VALUE;
        }
        // 这里定时任务不是直接加到对应的格子中，而是先加入到一个队列里，然后等到下一个tick的时候，会从队列里取出最多100000个任务加入到指定的格子中
        HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
        timeouts.add(timeout);
        return timeout;
    }


    /**
     * 启动时间轮。这个方法其实不需要显示的主动调用，因为在添加定时任务（newTimeout()方法）的时候会自动调用此方法。
     * 这个是合理的设计，因为如果时间轮里根本没有定时任务，启动时间轮也是空耗资源
     */
    public void start() {
        switch (WORKER_STATE_UPDATER.get(this)) {
            // 判断当前时间轮的状态，如果是初始化，则启动worker线程，启动整个时间轮；如果已经启动则略过；如果是已经停止，则报错
            // 这里是一个Lock Free的设计。因为可能有多个线程调用启动方法，这里使用AtomicIntegerFieldUpdater原子的更新时间轮的状态
            case WORKER_STATE_INIT:
                if (WORKER_STATE_UPDATER.compareAndSet(this, WORKER_STATE_INIT, WORKER_STATE_STARTED)) {
                    workerThread.start();
                }
                break;
            case WORKER_STATE_STARTED:
                break;
            case WORKER_STATE_SHUTDOWN:
                throw new IllegalStateException("cannot be started once stopped");
            default:
                throw new Error("Invalid WorkerState");
        }

        // 等待worker线程初始化时间轮的启动时间
        while (startTime == 0) {
            try {
                startTimeInitialized.await();
            } catch (InterruptedException ignore) {
                // Ignore - it will be ready very soon.
            }
        }

    }

    @Override
    public Set<Timeout> stop() {
        // worker线程不能停止时间轮，也就是加入的定时任务，不能调用这个方法。
        // 不然会有恶意的定时任务调用这个方法而造成大量定时任务失效
        if (Thread.currentThread() == workerThread) {
            throw new IllegalStateException(
                    HashedWheelTimer.class.getSimpleName() +
                            ".stop() cannot be called from " +
                            TimerTask.class.getSimpleName());
        }
        // 尝试CAS替换当前状态为“停止：2”。如果失败，则当前时间轮的状态只能是“初始化：0”或者“停止：2”。直接将当前状态设置为“停止：2“
        if (!WORKER_STATE_UPDATER.compareAndSet(this, WORKER_STATE_STARTED, WORKER_STATE_SHUTDOWN)) {
            // workerState can be 0 or 2 at this moment - let it always be 2.
            if (WORKER_STATE_UPDATER.getAndSet(this, WORKER_STATE_SHUTDOWN) != WORKER_STATE_SHUTDOWN) {
                INSTANCE_COUNTER.decrementAndGet();
            }
            return Collections.emptySet();
        }
        try {
            // 终端worker线程
            boolean interrupted = false;
            while (workerThread.isAlive()) {
                workerThread.interrupt();
                try {
                    workerThread.join(100);
                } catch (InterruptedException ignored) {
                    interrupted = true;
                }
            }

            // 从中断中恢复
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        } finally {
            INSTANCE_COUNTER.decrementAndGet();
        }
        // 返回未处理的任务
        return worker.unprocessedTimeouts();
    }
}
