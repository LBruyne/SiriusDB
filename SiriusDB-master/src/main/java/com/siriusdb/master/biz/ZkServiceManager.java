package com.siriusdb.master.biz;

import com.siriusdb.master.zk.ZkServiceExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: Master服务器的ZooKeeper管理
 * @author: liuxuanming
 * @date: 2021/05/10 10:05 下午
 */
public class ZkServiceManager {

    private static final Logger logger = LoggerFactory.getLogger(ZkServiceManager.class);

    /**
     * 启动应用时，开始监听ZooKeeper对应目录，获取服务器信息。
     */
    public void startMonitor() {
        try {
            new ZkServiceExecutor().run();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
