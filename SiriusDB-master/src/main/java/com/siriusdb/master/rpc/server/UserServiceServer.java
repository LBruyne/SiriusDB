package com.siriusdb.master.rpc.server;

import com.siriusdb.utils.rpc.DynamicThriftServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TTransportException;

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