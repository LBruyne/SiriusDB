package com.siriusdb.region;

import com.siriusdb.region.biz.RegionServerManager;

/**
 * @Description: Master服务启动入口
 * @author: liuxuanming
 * @date: 2021/04/29 5:05 下午
 */
public class RegionRunner {

    /**
     * DB Region Server
     * 实际上是一个数据服务器，需要承担的工作有：
     * 1. 启动时，与ZooKeeper建立连接，维持Session，以临时节点的形式把自己注册到Zookeeper的某节点下，并设置本机的IP和PORT
     * 2. 当发生异常或主动断开连接时，与Session断开连接，该临时节点被删除。
     * 3. BufferManager，收到RPC调用请求（这可能是来自于Client的数据请求，也有可能是Master执行策略时需要的请求）时进行相应调用并返回结果
     *
     * 解决方案设计：
     * 实现两个线程和若干方法。
     * 第一个线程在应用启动的时候自动将本机的Host信息注册到ZooKeeper，然后阻塞，直到应用退出的时候也同时退出。
     * 第二个线程（主线程）一直运行，作为BufferManager负责维护本地数据，在接收到RPC请求的时候执行相应逻辑并返回结果。
     * 其中第二个线程需要牵涉到thrift接口，需要提前写好接口文件。
     *
     * @param args
     */
    public static void main(String args[]) {
        RegionServerManager regionServerManager = new RegionServerManager();
        regionServerManager.run();
    }
}
