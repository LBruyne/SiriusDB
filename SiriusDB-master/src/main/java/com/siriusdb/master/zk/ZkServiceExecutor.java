package com.siriusdb.master.zk;

import com.siriusdb.common.UtilConstant;
import com.siriusdb.common.ZkConstant;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @Description: ZooKeeper服务执行类
 * @author: liuxuanming
 * @date: 2021/05/10 10:06 下午
 */
public class ZkServiceExecutor implements Watcher, Runnable, RegionServerMonitor.RegionServerMonitorListener {

    private final static Logger logger = LoggerFactory.getLogger(ZkServiceExecutor.class);

    private ZooKeeper zk;

    private RegionServerMonitor regionServerMonitor;

    /**
     * 初始化执行器:
     * 执行器自己作为Zookeeper的监控者，监控和Zookeeper连接的变化
     * 同时自己作为RegionServerMonitor的监控者，当Monitor监控到变化时会调用Executor执行业务操作
     */
    public ZkServiceExecutor() {
        try {
            zk = new ZooKeeper(ZkConstant.ZOOKEEPER_HOST, ZkConstant.ZK_SESSION_TIMEOUT, this);
            regionServerMonitor = new RegionServerMonitor(zk, null, this);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * 当服务器列表发生变化，即ZNODE路径下节点发生变化时，回调使用该方法
     * @param data
     */
    @Override
    public void changed(List<String> data) {

    }

    /**
     * 当关闭时，让线程继续走完
     */
    @Override
    public void closing(int rc) {
        synchronized (this) {
            // 为了严谨起见这里唤醒所有线程
            notifyAll();
        }
    }

    /**
     * 实现Runnable.run方法
     * 作为单独线程运行
     */
    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!regionServerMonitor.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * 实现Watcher.process方法
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        /**
         * 将事件全部交给Monitor统一处理
         */
        regionServerMonitor.process(watchedEvent);
    }
}
