package com.siriusdb.utils.rpc;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.exception.BasicBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

/**
 * @Description: 抽象类，Thrift服务端需要继承这个类
 * @author: liuxuanming
 * @date: 2021/05/13 7:09 下午
 */
@Slf4j
public abstract class DynamicThriftServer {

    protected TProcessor processor = null;

    protected TServer server = null;

    protected Integer port = null;

    public TServer getServer() {
        return server;
    }

    public Integer getPort() {
        return port;
    }

    /**
     *
     * @param processor
     * @param port
     * @throws TTransportException
     */
    public DynamicThriftServer(TProcessor processor, Integer port) throws TTransportException {
        initServer(processor, port);
    }

    /**
     *
     * @param processor
     * @throws TTransportException
     */
    public DynamicThriftServer(TProcessor processor) throws TTransportException {
        initServer(processor, MasterConstant.RPC_LISTEN_PORT);
    }

    /**
     * 在指定的端口上初始化特定类型的服务端
     *
     * @param processor
     * @param port
     */
    private void initServer(TProcessor processor, Integer port) {
        try {
            this.port = port;

            TServerTransport transport = new TServerSocket(port);

            // 生成server对象
            server = new TThreadPoolServer(new TThreadPoolServer.Args(transport)
                    .processor(processor)
                    .maxWorkerThreads(20)
                    .minWorkerThreads(10)
                    .protocolFactory(new TBinaryProtocol.Factory())
                    .transportFactory(new TTransportFactory()));
            log.warn("RPC服务端创建：{}", processor.getClass());
        } catch (TTransportException e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * 启动客户端，开始监听端口
     */
    public void startServer() {
        if(server == null) {
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "服务端还未初始化");
        } else {
            server.serve();
            log.warn("RPC服务端创建：{} 完毕，并且开始监听端口 {}", processor.getClass(), port);
        }
    }
}
