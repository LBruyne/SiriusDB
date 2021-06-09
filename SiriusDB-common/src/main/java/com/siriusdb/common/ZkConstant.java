package com.siriusdb.common;

/**
 * @Description: ZooKeeper服务相关常量
 * @author: liuxuanming
 * @date: 2021/05/11 1:48 下午
 */
public class ZkConstant {

    /**
     * ZooKeeper集群内各个服务器注册的节点路径
     */
    public static final String ZNODE = "/zookeeper/db/data_servers";

    /**
     * ZooKeeper集群内各个服务器注册自身信息的节点名前缀
     */
    public static final String HOST_NAME_PREFIX = "SERVER-";

    /**
     * ZooKeeper集群访问的端口
     */
    public static final String ZOOKEEPER_HOST = UtilConstant.HOST_IP + ":2181";

    /**
     * ZooKeeper会话超时时间
     */
    public static final Integer ZK_SESSION_TIMEOUT = 10000;

    /**
     * ZooKeeper连接超时时间
     */
    public static final Integer ZK_CONNECTION_TIMEOUT = 10000;

    /**
     * ZooKeeper最多重新尝试连接次数
     */
    public static final int ZK_MAX_RETRIES = 5;

    /**
     * ZooKeeper尝试连接间隔时间
     */
    public static final int ZK_BASE_SLEEP_TIME = 1000;

}
