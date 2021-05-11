package com.siriusdb.client;

import com.siriusdb.client.rpc.client.BasicServiceClient;
import com.siriusdb.client.rpc.client.UserServiceClient;
import com.siriusdb.client.db.api.ServiceImpl;
import com.siriusdb.client.db.manager.IndexManager;

/**
 * @Description: 分布式数据库客户端入口
 * @author: liuxuanming
 * @date: 2021/04/29 3:43 下午
 */
public class ClientRunner {

    public static void main( String[] args ) {
        ServiceImpl.DemoMethod1();

        IndexManager im = new IndexManager();
        im.hello();

        UserServiceClient client = new UserServiceClient();
        client.testUserService();
    }
}
