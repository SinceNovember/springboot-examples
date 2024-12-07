package com.simple.timewheel;

/**
 * 实际执行的任务包装器, 包含前后指针
 */
public interface Timeout {

    /**
     * 所在的时间轮
     */
    Timer timer();

    /**
     * 具体的任务动作
     */
    TimerTask task();

    /**
     * 任务是否过期
     */
    boolean isExpired();

    /**
     * 任务是否已经取消
     */
    boolean isCancelled();

    /**
     * 取消任务
     *
     * @return
     */
    boolean cancel();

}
