package com.simple.netty.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServiceThread implements Runnable {
    private static final Logger stLog = LoggerFactory.getLogger(ServiceThread.class);
    protected final Thread thread;
    private static final long joinTime = 90 * 1000;
    protected volatile boolean hasNotified = false;
    protected volatile boolean stopped = false;

    public ServiceThread() {
        this.thread = new Thread(this, this.getServiceName());
    }

    public abstract String getServiceName();

    public void start() {
        this.thread.start();
    }

    public void shutdown() {
        this.shutdown(false);
    }

    public void stop() {
        this.stop(false);
    }

    public void makeStop() {
        this.stopped = true;
    }

    public void stop(final boolean interrupt) {
        this.stopped = true;
        stLog.info("stop thread " + this.getServiceName() + " interrupt " + interrupt);
        synchronized (this) {
            if (!this.hasNotified) {
                this.hasNotified = true;
                this.notify();
            }
        }
        if (interrupt) {
            this.thread.interrupt();
        }
    }

    public void shutdown(final boolean interrupt) {
        this.stopped = true;
        stLog.info("shutdown thread " + this.getServiceName() + " interrupt " + interrupt);
        synchronized (this) {
            if (!this.hasNotified) {
                this.hasNotified = true;
                this.notify();
            }
        }

        try {
            if (interrupt) {
                this.thread.interrupt();
            }

            long beginTime = System.currentTimeMillis();
            this.thread.join(this.getJoinTime());
            long eclipseTime = System.currentTimeMillis() - beginTime;
            stLog.info("join thread " + this.getServiceName() + " eclipse time(ms) " + eclipseTime + " "
                    + this.getJoinTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void wakeup() {
        synchronized (this) {
            if (!this.hasNotified) {
                this.hasNotified = true;
                this.notify();
            }
        }
    }


    protected void waitForRunning(long interval) {
        synchronized (this) {
            if (this.hasNotified) {
                this.hasNotified = false;
                this.onWaitEnd();
                return;
            }

            try {
                this.wait(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.hasNotified = false;
                this.onWaitEnd();
            }
        }
    }


    protected void onWaitEnd() {
    }


    public boolean isStopped() {
        return stopped;
    }


    public long getJoinTime() {
        return joinTime;
    }

}
