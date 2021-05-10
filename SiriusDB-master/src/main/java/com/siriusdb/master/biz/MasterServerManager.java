package com.siriusdb.master.biz;

import com.siriusdb.master.server.UserServiceServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: Master Server模块基本管理类
 * @author: liuxuanming
 * @date: 2021/05/07 10:39 下午
 */
public class MasterServerManager {

    private static final Logger logger = LoggerFactory.getLogger(MasterServerManager.class);

    private ZkServiceManager zkServiceManager;

    private RpcServiceManager rpcServiceManager;

    public void run() {
        System.out.println("Hello Master!");

        // UserService服务程序Demo
        UserServiceServer serviceServer = new UserServiceServer();
        serviceServer.startServer();

        // 第一个线程在启动时向ZooKeeper发送请求，获得ZNODE目录下的信息并且持续监控，如果发生了目录的变化则执行回调函数，处理相应策略。
        zkServiceManager.startMonitor();

        // 第二个线程负责监听RPC端口，接收客户端的请求，返回需要的元数据。
        rpcServiceManager.startService();
    }
}
