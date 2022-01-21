package com.simple.zookeeper.leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LeaderSelectorAdapter extends LeaderSelectorListenerAdapter implements Closeable {

    private String name;
    private LeaderSelector leaderSelector;

    public LeaderSelectorAdapter(CuratorFramework client, String path, String name){
        this.name = name;
        leaderSelector = new LeaderSelector(client, path, this);
        leaderSelector.autoRequeue();
    }

    public void start() {
        leaderSelector.start();
    }

    @Override
    public void close() throws IOException {
        leaderSelector.close();
    }

    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        final int waitSeconds = (int) (5 * Math.random()) + 1;
        System.out.println(name + " is now the leader. Waiting " + waitSeconds + " seconds...");
        try {
            TimeUnit.SECONDS.sleep(waitSeconds);
        } catch (InterruptedException e) {
            System.err.println(name + " was interrupted.");
            Thread.currentThread().interrupt();
        } finally {
            System.out.println(name + " release leadership.\n");
        }
    }

    /**
     * 创建zk客户端
     */
    private CuratorFramework createClient(String zkAddr){
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(zkAddr)
                .sessionTimeoutMs(50000)
                .connectionTimeoutMs(30000)
                .retryPolicy(new ExponentialBackoffRetry(10000, 5))
                .namespace("leaderSelector")
                .build();
        return client;
    }

    /**
     * 轮询创建leader
     */
    private void initLeaderSelector() throws Exception{
        List<CuratorFramework> clients = new ArrayList<>();
        List<LeaderSelectorAdapter> leaders = new ArrayList<>();
        try {
            for (int i = 0; i < 10; i++) {
                CuratorFramework client = createClient("127.0.0.1:2181");
                clients.add(client);
                LeaderSelectorAdapter selectorAdapter = new LeaderSelectorAdapter(client, "/node2", "client#" + i);
                leaders.add(selectorAdapter);
                client.start();
                selectorAdapter.start();
            }
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        }finally {
            for (LeaderSelectorAdapter leader : leaders) {
                leader.close();
            }
            for (CuratorFramework client : clients) {
                client.close();
            }
        }
    }

}