package com.simple.timewheel;

/**
 * 具体任务实现类
 */
@FunctionalInterface
public interface TimerTask extends Runnable {

    /**
     * 执行任务
     * @param timeout
     * @throws Exception
     */
}
