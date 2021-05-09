package com.siriusdb.region.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Description: 服务注册功能，启动应用时向ZooKeeper注册一个服务，即创建一个Znode临时节点，并将所在host和port写到对应节点下
 * @author: liuxuanming
 * @date: 2021/05/09 4:32 下午
 */
public class ServiceRegister implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegister.class);

    private ZooKeeper zk;

    private static final String ZNODE = "/zookeeper/db/data_servers";

    private static final String NODE_PREFIX = "SERVER-";

    private static final String ZOOKEEPER_HOST = "127.0.0.1:2181";

    /**
     * 主机名，作为data_servers的子节点名字
     */
    private String hostName = "1";

    /**
     * 主机URL
     */
    private String hostUrl = "192.168.0.1:2345";

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public ServiceRegister() throws IOException {
        zk = new ZooKeeper(ZOOKEEPER_HOST, 10000,null);
    }

    @Override
    public void run() {
        try {
            // 向ZooKeeper注册节点
            createNode();

            // 阻塞该线程，直到发生异常或者主动退出
            synchronized (this) {
                wait();
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * @description: 创建子节点，必须在父节点存在的时候，才能创建子节点。
     * @author: liuxuanming
     */
    private String createNode() throws KeeperException, InterruptedException {
        // 如果根节点不存在，则创建根节点
        Stat stat = zk.exists(ZNODE, false);
        if (stat == null) {
            zk.create(ZNODE, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        // 创建EPHEMERAL类型节点
        String path = zk.create(ZNODE + "/" + NODE_PREFIX + hostName,
                hostUrl.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        return path;
    }
}
