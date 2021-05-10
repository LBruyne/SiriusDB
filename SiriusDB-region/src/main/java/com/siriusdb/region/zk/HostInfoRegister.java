package com.siriusdb.region.zk;

import com.siriusdb.utils.ZkUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import com.siriusdb.common.UtilConstant;

/**
 * @Description: 服务注册功能，启动应用时向ZooKeeper注册一个服务，即创建一个Znode临时节点，并将所在host和port写到对应节点下
 * @author: liuxuanming
 * @date: 2021/05/09 4:32 下午
 */
public class HostInfoRegister implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HostInfoRegister.class);

    private ZooKeeper zk;

    public HostInfoRegister() throws IOException {
        zk = new ZooKeeper(UtilConstant.ZOOKEEPER_HOST, 10000,null);
    }

    /**
     * 实现Runnable.run方法
     * 作为单独线程运行
     */
    @Override
    public void run() {
        try {
            // 向ZooKeeper注册临时节点
            createEphemeralNode();

            // 阻塞该线程，直到发生异常或者主动退出
            synchronized (this) {
                wait();
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * @description: 创建子节点，必须在父节点存在的时候，才能创建子节点。同时，创建的节点是临时类型的
     * @author: liuxuanming
     */
    private String createEphemeralNode() throws KeeperException, InterruptedException {
        // 如果根节点不存在，则创建根节点
        Stat stat = zk.exists(UtilConstant.ZNODE, false);
        if (stat == null) {
            zk.create(UtilConstant.ZNODE, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        // 创建EPHEMERAL类型节点
        String path = zk.create(ZkUtil.getRegisterPath(),
                UtilConstant.hostUrl.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        return path;
    }
}
