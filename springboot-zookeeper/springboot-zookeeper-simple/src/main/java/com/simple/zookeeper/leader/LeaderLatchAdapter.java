package com.simple.zookeeper.leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

public class LeaderLatchAdapter {

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
     * 多节点下每个节点轮询当leader
     */
    private void initLeaderLatch() throws Exception {
        List<CuratorFramework> clients = new ArrayList<>();
        List<LeaderLatch> latches = new ArrayList<>();
        try {
            for (int i = 0; i < 10; i++){
                CuratorFramework client = createClient("127.0.0.1:2181");
                clients.add(client);
                LeaderLatch latch = new LeaderLatch(client, "/node", "client#" + i);
                latch.addListener(new LeaderLatchListener() {
                    @Override
                    public void isLeader() {
                        System.out.println("i am leader");
                    }

                    @Override
                    public void notLeader() {
                        System.out.println("i am not leader");
                    }
                });
                latches.add(latch);
                client.start();
                latch.start();
            }
            Thread.sleep(10000);
            LeaderLatch currentLeader = null;
            for (LeaderLatch latch : latches) {
                if (latch.hasLeadership()) {
                    currentLeader = latch;
                }
            }
            System.out.println("current leader is " + currentLeader.getId());
            currentLeader.close();
            Thread.sleep(10000);
            for (LeaderLatch latch : latches) {
                if (latch.hasLeadership()) {
                    currentLeader = latch;
                }
            }
            System.out.println("current leader is " + currentLeader.getId());
        }finally {
            for (LeaderLatch latch : latches) {
                if (latch.getState() != null && latch.getState() != LeaderLatch.State.CLOSED){
                    latch.close();
                }
            }
            for (CuratorFramework client : clients) {
                if(client != null){
                    client.close();
                }
            }
        }
    }
}
