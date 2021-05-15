package com.siriusdb.client;

import com.siriusdb.client.rpc.client.UserServiceClient;
import com.siriusdb.client.db.api.ServiceImpl;
import com.siriusdb.client.db.manager.IndexManager;
import com.siriusdb.thrift.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 分布式数据库客户端入口
 * @author: liuxuanming
 * @date: 2021/04/29 3:43 下午
 */
@Slf4j
public class ClientRunner {

    public static void main( String[] args ) {
        ServiceImpl.DemoMethod1();

        IndexManager im = new IndexManager();
        im.hello();

        // 像这样注册一个客户端
        UserServiceClient client = new UserServiceClient(UserService.Client.class);
        try {
            log.warn("getName 调用成功，得到结果 {}", client.getClient().getName(1));
            log.warn("isExist 调用成功，得到结果 {}", client.getClient().isExist("LIUXUANMING"));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
