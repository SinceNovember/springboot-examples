package com.simple.redisson;

import com.simple.redissson.Application;
import com.simple.redissson.service.RedissonService;
import jodd.util.ThreadUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedissonTest {

    private static final Logger log = LoggerFactory.getLogger(RedissonTest.class);

    @Autowired
    private RedissonService redissonService;

    @Resource
    private RedissonClient redisson;

    @Test
    public void testLockExamples() {
        // RLock 继承了 java.util.concurrent.locks.Lock 接口
        RLock lock = redissonService.getRLock("redission:test:lock:1");

        final int[] count = {0};
        int threads = 10;
        ExecutorService pool = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        long start = System.currentTimeMillis();
        for (int i = 0; i < threads; i++) {
            pool.submit(() ->
            {
                for (int j = 0; j < 1000; j++) {
                    lock.lock();

                    count[0]++;
                    lock.unlock();
                }
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("10个线程每个累加1000为： = " + count[0]);
        //输出统计结果
        float time = System.currentTimeMillis() - start;

        System.out.println("运行的时长为：" + time);
        System.out.println("每一次执行的时长为：" + time/count[0]);
    }

    /**
     * 可重入锁
     */
    public void testReentrantLock(){
        RLock lock = redissonService.getRLock("anyLock");
        try{
            // 1. 最常见的使用方法
            //lock.lock();
            // 2. 支持过期解锁功能,10秒钟以后自动解锁, 无需调用unlock方法手动解锁
            //lock.lock(10, TimeUnit.SECONDS);
            // 3. 尝试加锁，最多等待3秒，上锁以后10秒自动解锁
            boolean res = lock.tryLock(3, 10, TimeUnit.SECONDS);
            if(res){ //成功
                // do your business
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 异步可重入锁
     * @param redisson
     */
    public void testAsyncReentrantLock(RedissonClient redisson){
        RLock lock = redissonService.getRLock("anyLock");
        try{
            lock.lockAsync();
            lock.lockAsync(10, TimeUnit.SECONDS);
            Future<Boolean> res = lock.tryLockAsync(3, 10, TimeUnit.SECONDS);
            if(res.get()){
                // do your business
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 公平锁， 优先分配给先发出请求的线程
     */
    public void testFairLock(RedissonClient redisson){
        RLock fairLock = redissonService.getFairLock("anyLock");
        try{
            // 最常见的使用方法
            fairLock.lock();
            // 支持过期解锁功能, 10秒钟以后自动解锁,无需调用unlock方法手动解锁
            fairLock.lock(10, TimeUnit.SECONDS);
            // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
            boolean res = fairLock.tryLock(100, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            fairLock.unlock();
        }
    }

    /**
     * 异步公平锁， 优先分配给先发出请求的线程
     */
    public void testAsyncFairLock(RedissonClient redisson){
        RLock fairLock = redissonService.getFairLock("anyLock");
        try{
            fairLock.lockAsync();
            fairLock.lockAsync(10, TimeUnit.SECONDS);
            Future<Boolean> res = fairLock.tryLockAsync(100, 10, TimeUnit.SECONDS);
            if(res.get()){
                // do your business
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            fairLock.unlock();
        }
    }

    /**
     * 联锁
     * Redisson的RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例。
     * @param redisson1
     * @param redisson2
     * @param redisson3
     */
    public void testMultiLock(RedissonClient redisson1,RedissonClient redisson2, RedissonClient redisson3){
        RLock lock1 = redisson1.getLock("lock1");
        RLock lock2 = redisson2.getLock("lock2");
        RLock lock3 = redisson3.getLock("lock3");
        RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
        try {
            // 同时加锁：lock1 lock2 lock3, 所有的锁都上锁成功才算成功。
            lock.lock();
            // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
            boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 红锁
     * @param redisson1
     * @param redisson2
     * @param redisson3
     */
    public void testRedLock(RedissonClient redisson1,RedissonClient redisson2, RedissonClient redisson3){
        RLock lock1 = redisson1.getLock("lock1");
        RLock lock2 = redisson2.getLock("lock2");
        RLock lock3 = redisson3.getLock("lock3");
        RedissonRedLock lock = new RedissonRedLock(lock1, lock2, lock3);
        try {
            // 同时加锁：lock1 lock2 lock3, 红锁在大部分节点上加锁成功就算成功。
            lock.lock();
            // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
            boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    /**
     * 读写锁
    * @param redisson
     */
    public void testReadAndWriteLock(RedissonClient redisson){
        RReadWriteLock rwlock =  redisson.getReadWriteLock("anyRWLock");
        // 最常见的使用方法
        rwlock.readLock().lock();
        // 或
        rwlock.writeLock().lock();
        // 支持过期解锁功能
        // 10秒钟以后自动解锁
        // 无需调用unlock方法手动解锁
        rwlock.readLock().lock(10, TimeUnit.SECONDS);
        // 或
        rwlock.writeLock().lock(10, TimeUnit.SECONDS);
        try {
            // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
            boolean tLock = rwlock.readLock().tryLock(100, 10, TimeUnit.SECONDS);
            // 或
            boolean rLock = rwlock.writeLock().tryLock(100, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            rwlock.readLock().unlock();
            rwlock.writeLock().unlock();
        }

    }

    /**
     * 信号量
     */
    public void testSemaphore() {
        RSemaphore semaphore = redisson.getSemaphore("semaphore");
        try {
            semaphore.acquire();
            //或
            semaphore.acquireAsync();
            semaphore.acquire(23);
            semaphore.tryAcquire();
            //或
            semaphore.tryAcquireAsync();
            semaphore.tryAcquire(23, TimeUnit.SECONDS);
            //或
            semaphore.tryAcquireAsync(23, TimeUnit.SECONDS);
            semaphore.release(10);
            semaphore.release();
            //或
            semaphore.releaseAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 可过期信号量
     */
    public void testPermitExpirableSemaphore() {
        RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore("mySemaphore");
        try {
            String permitId = semaphore.acquire();
            // 获取一个信号，有效期只有2秒钟。
//            String permitId = semaphore.acquire(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 使用CompletableFuture 和  CountDownLatch  进行并发回调
//     */
//
//    @Test
//    public void testMutiCallBack() {
//        CountDownLatch countDownLatch = new CountDownLatch(10);
//        //批量异步
//        ExecutorService executor = ThreadUtil.getIoIntenseTargetThreadPool();
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 10; i++) {
//            CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
//                long tid = ThreadUtil.getCurThreadId();
//                try {
//                    System.out.println("线程" + tid + "开始了,模拟一下远程调用");
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return tid;
//            }, executor);
//
//            future.thenAccept((tid) -> {
//                System.out.println("线程" + tid + "结束了");
//                countDownLatch.countDown();
//            });
//        }
//        try {
//            countDownLatch.await();
//            //输出统计结果
//            float time = System.currentTimeMillis() - start;
//
//            System.out.println("所有任务已经执行完毕");
//            System.out.println("运行的时长为(ms)：" + time);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
