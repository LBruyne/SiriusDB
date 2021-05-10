package com.siriusdb.region.biz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: Region Server 主业务逻辑
 * @author: liuxuanming
 * @date: 2021/05/09 10:00 下午
 */
public class RegionServerManager {

    private final static Logger logger = LoggerFactory.getLogger(RegionServerManager.class);

    private BufferManager bufferManager;

    private ZkServiceManager zkServiceManager;

    public void run() {
        System.out.println("Hello Region!");

        // 线程1：在应用启动的时候自动将本机的Host信息注册到ZooKeeper，然后阻塞，直到应用退出的时候也同时退出
        zkServiceManager.serviceRegister();

        // 主线程：
    }
}
