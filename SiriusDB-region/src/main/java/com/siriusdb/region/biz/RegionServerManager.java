package com.siriusdb.region.biz;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: Region Server 主业务逻辑
 * @author: liuxuanming
 * @date: 2021/05/09 10:00 下午
 */
@Slf4j
public class RegionServerManager {

    private RegionServiceManager regionServiceManager;

    private ZkServiceManager zkServiceManager;

    public RegionServerManager() {
        regionServiceManager = new RegionServiceManager();
        zkServiceManager = new ZkServiceManager();
    }

    public void run() {
        // 线程1：在应用启动的时候自动将本机的Host信息注册到ZooKeeper，然后阻塞，直到应用退出的时候也同时退出
        Thread zkServiceThread = new Thread(zkServiceManager);
        zkServiceThread.start();

        // 主线程：
        regionServiceManager.startService();
    }
}
