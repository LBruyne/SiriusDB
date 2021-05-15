package com.siriusdb.utils.zk;

import com.siriusdb.common.ZkConstant;
import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.exception.BasicBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 持有一个连接到ZooKeeper的Curator连接客户端
 * @author: liuxuanming
 * @date: 2021/05/12 8:39 下午
 */
@Slf4j
public class CuratorClientHolder {

    private CuratorFramework client = null;

    /**
     * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
     */
    private ExecutorService pool = Executors.newFixedThreadPool(2);

    private String hostUrl = null;

    public CuratorClientHolder() {
        this.setUpConnection(ZkConstant.ZOOKEEPER_HOST);
    }

    public CuratorClientHolder(String hostUrl) {
        this.setUpConnection(hostUrl);
    }

    public CuratorFramework getClient() {
        return client;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    /**
     * 在特定Host中连接ZooKeeper
     *
     * @param hostUrl
     */
    public void setUpConnection(String hostUrl) {
        this.hostUrl = hostUrl;

        if (client == null) {
            synchronized (this) {
                // 创建连接
                client = CuratorFrameworkFactory.builder()
                        .connectString(hostUrl)
                        .connectionTimeoutMs(ZkConstant.ZK_CONNECTION_TIMEOUT)
                        .sessionTimeoutMs(ZkConstant.ZK_SESSION_TIMEOUT)
                        .retryPolicy(ZkUtil.getRetryStrategy())   // 重试策略：初试时间为1s 重试10次
                        .build();

                // 开启连接
                client.start();
            }
        }
    }

    /**
     * 在特定路径上创建节点，并且设定一个特殊的值。默认为持久节点
     *
     * @param registerPath
     * @param value
     * @return
     */
    public String createNode(String registerPath, String value) throws Exception {
        checkClientConnected();
        /**
         * 如果父节点不存在，会先创建父节点
         */
        return client.create().creatingParentsIfNeeded().forPath(registerPath, value.getBytes());
    }

    /**
     * 创建节点
     *
     * @param registerPath 节点路径
     * @param value        值
     * @param nodeType     节点类型
     */
    public String createNode(String registerPath, String value, CreateMode nodeType) throws Exception {
        checkClientConnected();
        if (nodeType == null) {
            throw new BasicBusinessException(ErrorCodeEnum.BASIC_VALIDATION_FAILED.getCode(), "创建节点类型不合法");
        } else if (CreateMode.PERSISTENT.equals(nodeType)) {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(registerPath, value.getBytes());
        } else if (CreateMode.EPHEMERAL.equals(nodeType)) {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(registerPath, value.getBytes());
        } else if (CreateMode.PERSISTENT_SEQUENTIAL.equals(nodeType)) {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(registerPath, value.getBytes());
        } else if (CreateMode.EPHEMERAL_SEQUENTIAL.equals(nodeType)) {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(registerPath, value.getBytes());
        } else {
            throw new BasicBusinessException(ErrorCodeEnum.BASIC_VALIDATION_FAILED.getCode(), "创建节点类型不被采纳");
        }
    }


    /**
     * 获取单个节点
     *
     * @param targetPath
     * @return
     */
    public String getData(String targetPath) throws Exception {
        checkClientConnected();
        return new String(client.getData().forPath(targetPath));
    }

    /**
     * 获取子节点列表
     *
     * @param targetPath
     * @return 子节点名字列表
     */
    public List<String> getChildren(String targetPath) throws Exception {
        checkClientConnected();
        return client.getChildren().forPath(targetPath);
    }

    /**
     * 检测节点是否存在
     *
     * @param targetPath
     * @return
     */
    public boolean checkNodeExist(String targetPath) throws Exception {
        checkClientConnected();
        Stat s = client.checkExists().forPath(targetPath);
        return s == null ? false : true;

    }

    /**
     * 监听数据节点的数据变化
     *
     * @param targetPath
     * @param listener
     * @throws Exception
     */
    public void monitorNode(String targetPath, NodeCacheListener listener) throws Exception {
        final NodeCache nodeCache = new NodeCache(client, targetPath, false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                log.warn("服务器节点【{}】数据发生变化，新数据为：{}", targetPath, new String(nodeCache.getCurrentData().getData()));
            }
        }, pool);
    }

    /**
     * 监听子节点数据变化
     *
     * @param targetPath
     * @param listener
     * @throws Exception
     */
    public void monitorChildrenNodes(String targetPath, PathChildrenCacheListener listener) throws Exception {
        final PathChildrenCache childrenCache = new PathChildrenCache(client, targetPath, true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(listener, pool);
    }

    private void checkClientConnected() {
        if (client == null) this.setUpConnection(ZkConstant.ZOOKEEPER_HOST);
    }
}

