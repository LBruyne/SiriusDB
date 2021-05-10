package com.siriusdb.master;

import com.siriusdb.master.server.UserServiceServer;

/**
 * @Description: Master服务启动入口
 * @author: liuxuanming
 * @date: 2021/04/29 5:05 下午
 */
public class MasterRunner {

    /**
     * DB Master Server
     * 实际上是一个检索服务器，需要承担的工作有：
     * 1. 启动时，和ZooKeeper集群进行通信，加载对应节点数据，获取了目前所有能提供服务的数据服务器列表，并且加载到内存中
     * 2. 在1完成后，与每个RegionServer进行通信，获取到每个服务器上保存的表格元数据，并储存到本地
     * 3. 一直监听节点，当节点内容发生变化，即某Server上线或者某Server下线时，获得通知，重新加载节点数据，刷新内存中数据服务器列表
     * 4. 接收客户端的请求，返回需要的元数据
     * 5. (Optional)在3中获得消息后，与相应的服务器进行联系，执行容错容灾、副本复制等策略
     *
     * @param args
     */
    public static void main(String args[]) {
        System.out.println("Hello Master!");

        UserServiceServer serviceServer = new UserServiceServer();
        serviceServer.startServer();
    }
}
