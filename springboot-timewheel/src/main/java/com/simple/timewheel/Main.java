package com.simple.timewheel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class Main {
    public static void main(String[] args) {

        ExecutorService service = Executors.newFixedThreadPool(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(1, TimeUnit.SECONDS);
        System.out.println("start:" + LocalDateTime.now().format(formatter));

        hashedWheelTimer.newTimeout(() -> {
            System.out.println("task1:" + LocalDateTime.now().format(formatter));
        }, 3, TimeUnit.SECONDS);

        hashedWheelTimer.newTimeout(() ->
            service.execute(() -> {
                System.out.println("task2:" + LocalDateTime.now().format(
                    formatter));
            }), 10, TimeUnit.SECONDS);

        hashedWheelTimer.newTimeout(() ->
            service.execute(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("task3:" + LocalDateTime.now().format(
                    formatter));
            }), 5, TimeUnit.SECONDS);

        hashedWheelTimer.newTimeout(() ->
            service.execute(() -> {
                System.out.println("task4:" + LocalDateTime.now().format(
                    formatter));
            }), 6, TimeUnit.SECONDS);
        hashedWheelTimer.newTimeout(() ->
            service.execute(() -> {
                System.out.println("task5:" + LocalDateTime.now().format(
                    formatter));
            }), 8, TimeUnit.SECONDS);

        System.out.println("ddddd");
    }
}