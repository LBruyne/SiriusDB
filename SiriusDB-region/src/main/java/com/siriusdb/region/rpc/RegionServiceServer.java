package com.siriusdb.region.rpc;

import com.siriusdb.utils.rpc.DynamicThriftServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TTransportException;

public class RegionServiceServer extends DynamicThriftServer{
    public RegionServiceServer(TProcessor processor) throws  TTransportException {
        super(processor);
    }

    public RegionServiceServer(TProcessor processor,Integer port) throws TTransportException {
        super(processor,port);
    }
}
