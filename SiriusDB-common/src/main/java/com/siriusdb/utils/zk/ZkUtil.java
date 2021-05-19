package com.siriusdb.utils.zk;

import com.siriusdb.common.ZkConstant;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Description: 工具类，获取本机节点名等信息
 * @author: liuxuanming
 * @date: 2021/05/10 3:33 下午
 */
public class ZkUtil {

    public static String getRegisterHolderPath() {
        return ZkConstant.ZNODE;
    }

    /**
     * @description: 获取Zookeeper注册的路径
     * @author: liuxuanming
     */
    public static String getRegisterPath() {
        return ZkConstant.ZNODE + "/" + ZkConstant.HOST_NAME_PREFIX + System.getenv().get("USER");
    }

    /**
     * @description: ZooKeeper重连接策略
     * @author: liuxuanming
     */
    public static ExponentialBackoffRetry getRetryStrategy() { return new ExponentialBackoffRetry(ZkConstant.ZK_BASE_SLEEP_TIME, ZkConstant.ZK_MAX_RETRIES); }
}
