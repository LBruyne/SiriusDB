package com.siriusdb.master.server;

import com.siriusdb.common.MasterServerConstant;
import com.siriusdb.master.rpc.server.UserServiceImpl;
import com.siriusdb.thrift.UserService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: UserService服务端
 * @author: liuxuanming
 * @date: 2021/05/09 11:07 上午
 */
public class UserServiceServer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void startServer() {
        UserService.Processor processor = new UserService.Processor<UserService.Iface>(new UserServiceImpl());
        try {
            TServerTransport transport = new TServerSocket(MasterServerConstant.RPC_LISTEN_PORT);
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(transport);
            tArgs.processor(processor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            tArgs.transportFactory(new TTransportFactory());
            tArgs.minWorkerThreads(10);
            tArgs.maxWorkerThreads(20);
            TServer server = new TThreadPoolServer(tArgs);
            server.serve();
        } catch (Exception e) {
            logger.warn("Thrift服务启动失败", e);
        }
    }
}