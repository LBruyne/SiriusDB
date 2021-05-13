package com.siriusdb.master.rpc.server;

import com.siriusdb.common.MasterServerConstant;
import com.siriusdb.master.rpc.server.UserServiceImpl;
import com.siriusdb.thrift.UserService;
import com.siriusdb.utils.rpc.DynamicThriftServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: UserService服务端
 * @author: liuxuanming
 * @date: 2021/05/09 11:07 上午
 */
@Slf4j
public class UserServiceServer extends DynamicThriftServer {
    public UserServiceServer(TProcessor processor) throws TTransportException {
        super(processor);
    }

    public UserServiceServer(TProcessor processor, Integer port) throws TTransportException {
        super(processor, port);
    }
}