package com.siriusdb.master.biz;

import com.siriusdb.master.server.UserServiceServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: RPC事务管理类
 * @author: liuxuanming
 * @date: 2021/05/10 4:10 下午
 */
@Slf4j
public class RpcServiceManager {

    /**
     * 启动应用时，开始等待，收到客户机的请求时返回相应数据。
     */
    public void startService() {
        log.warn("RPC服务启动：UserSeviceServer");
        UserServiceServer userServiceServer = new UserServiceServer();
        userServiceServer.startServer();
    }
}
