package com.siriusdb.client.rpc.client;

import com.siriusdb.common.MasterServerConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.thrift.UserService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @Description: 基本服务客户端
 * @author: liuxuanming
 * @date: 2021/05/11 2:29 下午
 */
public class BasicServiceClient<T> {

    private static final Logger logger = LoggerFactory.getLogger(BasicServiceClient.class);

    T client;

    TTransport transport;

    /**
     * 初始化客户端
     * 默认使用TBinaryProtocol，连接对象为Master Server
     */
    public BasicServiceClient() {

        // 初始化客户端
        try {
            transport = new TSocket(MasterServerConstant.MASTER_SERVER_IP, MasterServerConstant.MASTER_SERVER_PORT, UtilConstant.RPC_TIMEOUT);
            // 协议要和服务端一致
            // TProtocol protocol = new TBinaryProtocol(transport);
            client = (T) new UserService.Client(new TBinaryProtocol(transport));
            // 开启数据传输
            transport.open();
        } catch (TException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * 重载，使用TBinaryProtocol，连接对象自定义
     *
     * @param ip
     * @param port
     */
    public BasicServiceClient(String ip, Integer port) {

        // 初始化客户端
        try {
            transport = new TSocket(ip, port, UtilConstant.RPC_TIMEOUT);
            // 协议要和服务端一致
            // TProtocol protocol = new TBinaryProtocol(transport);
            client = (T) new UserService.Client(new TBinaryProtocol(transport));
            // 开启数据传输
            transport.open();
        } catch (TException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * 重载，使用协议自定义，连接对象自定义
     *
     * @param ip
     * @param port
     */
    public BasicServiceClient(String ip, Integer port, TProtocol protocol) {

        // 初始化客户端
        try {
            transport = new TSocket(ip, port, UtilConstant.RPC_TIMEOUT);
            // 协议要和服务端一致
            client = (T) new UserService.Client(protocol);
            // 开启数据传输
            transport.open();
        } catch (TException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * 客户端使用完后进行关闭
     */
    public void close() {
        if(transport != null) {
            transport.close();
        }
    }

}
