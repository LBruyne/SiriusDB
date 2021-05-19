package com.siriusdb.master.biz;

import com.siriusdb.master.rpc.server.MasterServiceImpl;
import com.siriusdb.master.rpc.server.MasterServiceServer;
import com.siriusdb.master.rpc.server.UserServiceImpl;
import com.siriusdb.master.rpc.server.UserServiceServer;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.thrift.service.UserService;
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
        try {
            UserServiceServer userServiceServer = new UserServiceServer(
                    new UserService.Processor<>(new UserServiceImpl()));
            userServiceServer.startServer();

            MasterServiceServer masterServiceServer = new MasterServiceServer(
                    new MasterService.Processor<>(new MasterServiceImpl()));
            masterServiceServer.startServer();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
