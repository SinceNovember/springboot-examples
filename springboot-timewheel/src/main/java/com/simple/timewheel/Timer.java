package com.simple.timewheel;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface Timer {

    /**
     * 调度任务的具体方法
     * @param task
     * @param delay
     * @param unit
     * @return 任务包装器
     */
    Timeout newTimeout(TimerTask task, long delay, TimeUnit unit);

    Set<Timeout> stop();

}
