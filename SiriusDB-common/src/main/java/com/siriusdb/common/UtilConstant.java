package com.siriusdb.common;

/**
 * @Description: 公共常量写在这里
 * @author: liuxuanming
 * @date: 2021/04/29 4:37 下午
 */
public class UtilConstant {

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
    public static final String ZOOKEEPER_HOST = "127.0.0.1:2181";

    /**
     * 本机主机名，修改为真实的
     */
    public static final String hostUrl = "192.168.0.1:2345";

}
