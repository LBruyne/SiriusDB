package com.siriusdb.region;

import com.siriusdb.region.zk.ServiceRegister;

import java.io.IOException;

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
     * 3. 当收到RPC调用请求（这可能是来自于Client的数据请求，也有可能是Master执行策略时需要的请求）时进行相应调用并返回结果
     *
     * @param args
     */
    public static void main(String args[]) {

        System.out.println("Hello Region!");

        try {
            new ServiceRegister().run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
