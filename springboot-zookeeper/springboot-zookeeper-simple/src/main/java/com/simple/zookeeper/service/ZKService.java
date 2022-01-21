package com.simple.zookeeper.service;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ZKService {
    Logger log = LoggerFactory.getLogger(ZKService.class);

    @Resource
    private CuratorFramework zkClient;

    /********************创建***************/

    /**
     * 创建一个节点，初始内容为空
     * 如果没有设置节点属性，节点创建模式默认为持久化节点，内容默认为空
     * @param path
     * @return
     */
    public String createNode(String path){
        try {
            return zkClient.create().forPath(path);
        } catch (Exception e) {
            log.error("创建节点失败", e);
        }
        return null;
    }

    /**
     *  创建一个节点，附带初始化内容
     * @param path
     * @param content
     * @return
     */
    public String createNode(String path, String content){
        try {
            return zkClient.create().forPath(path, content.getBytes());
        } catch (Exception e) {
            log.error("创建节点失败", e);
        }
        return null;
    }

    /**
     * 创建一个节点，指定创建模式（临时节点），内容为空
     * @param path
     * @return
     */
    public String createTempNode(String path){
        try {
            return zkClient.create().forPath(path);
        } catch (Exception e) {
            log.error("创建临时节点失败", e);
        }
        return null;
    }

    /**
     * 创建一个节点，指定创建模式（临时节点），附带初始化内容，并且自动递归创建父节点
     * @param path
     * @param content
     * @return
     */
    public String createTempNode(String path, String content){
        try {
            return zkClient.create().forPath(path, content.getBytes());
        } catch (Exception e) {
            log.error("创建临时节点失败", e);
        }
        return null;
    }

    /**
     * 事务操作进行创建和设置节点值
     * @param path
     * @param node
     * @return
     */
    public String createAndSetNodeInTransaction(String path, String node) {
        try {
            CuratorOp createOp = zkClient.transactionOp().create().forPath(path);
            CuratorOp setOp = zkClient.transactionOp().setData().forPath(path, node.getBytes());
            //事务执行结果
            List<CuratorTransactionResult> results =zkClient.transaction().forOperations(createOp, setOp);

            //遍历输出结果
            for(CuratorTransactionResult result : results){
                System.out.println("执行结果是： " + result.getForPath() + "--" + result.getType());
            }

        } catch (Exception e) {
            log.error("创建事务节点失败", e);
        }
        return null;
    }

    /**
     * 异步创建节点
     * @param path
     */
    public void createNodeAsync(String path){

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 300, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10000), new ThreadFactory(){
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "zkSyncThreadPool");
            }
        });
        try {
            zkClient.create().creatingParentsIfNeeded().inBackground((curatorFramework, curatorEvent) -> {
                System.out.println(String.format("eventType:%s,resultCode:%s", curatorEvent.getType(), curatorEvent.getResultCode()));
            }, executor).forPath(path);
        } catch (Exception e) {
            log.error("异步创建节点错误", e);
        }
    }

   /****************删除******************/

    /**
     *  删除一个节点
     *  此方法只能删除叶子节点，否则会抛出异常。
     * @param path
     */
    public void delNode(String path) {
        try {
            zkClient.delete().forPath(path);
        } catch (Exception e) {
            log.error("删除节点失败", e);
        }
    }

    /**
     * 删除一个节点，并且递归删除其所有的子节点
     * @param path
     */
    public void delNodeWithChildren(String path) {
        try {
            zkClient.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            log.error("删除节点失败", e);
        }
    }

    /**
     * 删除一个节点，强制指定版本进行删除
     * @param path
     * @param version
     */
    public void delNodeWithVersion(String path, int version) {
        try {
            zkClient.delete().withVersion(version).forPath(path);
        } catch (Exception e) {
            log.error("删除{}版本节点失败", version, e);
        }
    }

    /**
     * 删除一个节点，强制保证删除
     * guaranteed()是一个保障措施，只要客户端会话有效，会在后台持续进行删除操作，直到成功。
     *  上面的多个流式接口是可以自由组合的，例如：
     * zkClient.delete().guaranteed().deletingChildrenIfNeeded().withVersion(1000).forPath("test");
     * @param path
     */
    public void delNodeGuaranteed(String path) {
        try {
            zkClient.delete().guaranteed().forPath(path);
        } catch (Exception e) {
            log.error("强制删除节点失败", e);
        }
    }

    /***************读取数据节点************/

    /**
     *  读取一个节点的数据内容
     * @param path
     * @return
     */
    public byte[] getNode(String path) {
        try {
            return zkClient.getData().forPath(path);
        } catch (Exception e) {
            log.error("获取节点失败", e);
        }
        return new byte[]{};
    }

    /**
     * 读取一个节点的数据内容，同时获取到该节点的stat
     * @param path
     * @return
     */
    public byte[] getNodeWithStat(String path) {
        try {
            Stat stat = new Stat();
            return zkClient.getData().storingStatIn(stat).forPath(path);
        } catch (Exception e) {
            log.error("获取节点失败", e);
        }
        return new byte[]{};
    }

    /**
     * 获取某个节点的所有子节点路径
     * @param path 节点路径
     * @return 子节点路径
     */
    public List<String> getNodeChildrenPaths(String path) {
        try {
            return zkClient.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("强制删除节点失败", e);
        }
        return Collections.emptyList();
    }

    /*****************更新节点************/

    /**
     * 更新一个节点的数据内容
     * 该接口会返回一个Stat实例
     * @param path
     * @param content
     * @return
     */
    public Stat setNode(String path, String content) {
        try {
            return zkClient.setData().forPath(path, content.getBytes());
        } catch (Exception e) {
            log.error("更新节点失败", e);
        }
        return null;
    }

    /**
     * 更新一个节点的数据内容，强制指定版本进行更新
     * @param path
     * @param content
     * @param version
     * @return
     */
    public Stat setNodeWithVersion(String path, String content, int version) {
        try {
            return zkClient.setData().withVersion(version).forPath(path, content.getBytes());
        } catch (Exception e) {
            log.error("更新{}版本节点失败", version, e);
        }
        return null;
    }

    /**
     * 检查节点是否存在
     * @param path
     * @return
     */
    public Stat existNode(String path) {
        try {
            return zkClient.checkExists().forPath(path);
        } catch (Exception e) {
            log.error("强制删除节点失败", e);
        }
        return null;
    }

    /********************监听节点****************/

    /**
     * 监听节点变化
     * @param path
     */
    public void watchNodeChange(String path){
        NodeCache nodeCache = new NodeCache(zkClient, path);
        nodeCache.getListenable().addListener(() -> {
            ChildData data = nodeCache.getCurrentData();
            if (null != data) {
                System.out.println("节点数据：" + new String(nodeCache.getCurrentData().getData()));
            } else {
                System.out.println("节点被删除");
            }
        });
        try {
            nodeCache.start();
        } catch (Exception e) {
            log.error("监听器启动失败", e);
        }
    }

    /**
     * 监听子节点变化
     * @param path
     */
    public void watchNodeChildrenChange(String path) {
        // 监听路径子节点的变化
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, path, true);
        PathChildrenCacheListener childrenCacheListener = (framework, event) -> {
            if (event.getType() != PathChildrenCacheEvent.Type.CHILD_UPDATED || !path.equals(event.getData().getPath())) {
                return;
            }
            System.out.println("数据源更新完毕");
            System.out.println("子节点事件类型：" + event.getType() + " | 路径：" + (null != event.getData() ? event.getData().getPath() : null));
        };
        pathChildrenCache.getListenable().addListener(childrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            log.error("监听器启动失败", e);
        }
    }

    /**
     * 监听节点和子节点变化
     * @param path
     */
    public void watchTreeChange(String path) {
        // 监听整棵树的变化
        TreeCache treeCache = new TreeCache(zkClient, path);
        TreeCacheListener treeCacheListener = (framework, event) -> {
            System.out.println("事件类型：" + event.getType() + " | 路径：" + (null != event.getData() ? event.getData().getPath() : null));
        };
        treeCache.getListenable().addListener(treeCacheListener);
        try {
            treeCache.start();
        } catch (Exception e) {
            log.error("监听器启动失败", e);
        }

    }

    /**
     * 不可重入锁
     * @param path
     */
    public void lock(String path){
        InterProcessSemaphoreMutex lock = new InterProcessSemaphoreMutex(zkClient, path);

        try {
            lock.acquire(3, TimeUnit.SECONDS);
            System.out.println("aaaaa");
            /**
             * TODO 业务逻辑
             */
        } catch (Exception e) {
            log.error("获取锁失败", e);
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                log.error("释放锁失败", e);
            }
        }
    }

    /**
     * 可重入锁
     * @param path
     */
    public void ReentrantLock(String path){
        InterProcessMutex lock = new InterProcessMutex(zkClient, path);
        try {
            lock.acquire();
            /**
             * TODO 业务逻辑
             */
        } catch (Exception e) {
            log.error("获取锁失败", e);
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                log.error("释放锁失败", e);
            }
        }
    }

    /**
     * 可重入读写锁
     * @param path
     */
    public void ReentrantReadWriteLock(String path) {
        // 实例化锁
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(zkClient, path);
        // 获取读锁
        InterProcessMutex readLock = lock.readLock();
        // 获取写锁
        InterProcessMutex writeLock = lock.writeLock();

        try {
            // 只能先得到写锁再得到读锁，不能反过来
            if(!writeLock.acquire(3, TimeUnit.SECONDS)){
                throw new IllegalStateException("acquire writeLock err");
            }
            if(!readLock.acquire(3, TimeUnit.SECONDS)){
                throw new IllegalStateException("acquire readLock err");
            }
            /**
             * TODO 业务逻辑
             */
        } catch (Exception e) {
            log.error("获取锁失败", e);
        } finally {
            try {
                writeLock.release();
                readLock.release();
            } catch (Exception e) {
                log.error("释放锁失败", e);
            }
        }

    }

}
