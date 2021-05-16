package com.siriusdb.master.rpc.server;

import com.siriusdb.utils.rpc.DynamicThriftServer;
import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TTransportException;

/**
 * @Description: MasterServer的服务端
 * @author: liuxuanming
 * @date: 2021/05/16 11:24 上午
 */
public class MasterServiceServer extends DynamicThriftServer {
    public MasterServiceServer(TProcessor processor, Integer port) throws TTransportException {
        super(processor, port);
    }

    public MasterServiceServer(TProcessor processor) throws TTransportException {
        super(processor);
    }
}
