package com.siriusdb.utils.rpc;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @Description: 抽象类，Thrift客户端需要继承这个类
 * @author: liuxuanming
 * @date: 2021/05/11 2:29 下午
 */
@Slf4j
public abstract class DynamicThriftClient<T> {

    protected T client;

    protected TTransport transport;

    protected String ip;

    protected Integer port;

    public T getClient() {
        return client;
    }

    public TTransport getTransport() {
        return transport;
    }

    /**
     *
     * @param ts
     * @param ip
     * @param port
     */
    @SuppressWarnings("unchecked")
    public DynamicThriftClient(Class<T> ts, String ip, Integer port) {
        initClient(ts, ip, port);
    }

    /**
     *
     * @param ts
     */
    @SuppressWarnings("unchecked")
    public DynamicThriftClient(Class<T> ts) {
        initClient(ts, MasterConstant.MASTER_SERVER_IP, MasterConstant.MASTER_SERVER_PORT);
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    /**
     * 客户端使用完后进行关闭
     */
    public void close() {
        if (transport != null) {
            transport.close();
        }
    }

    /**
     * 在指定的IP和端口上初始化特定类型的服务端
     *
     * @param ts
     * @param ip
     * @param port
     */
    private void initClient(Class<T> ts, String ip, Integer port) {
        // 初始化客户端
        try {
            this.ip = ip;
            this.port = port;

            transport = new TSocket(ip, port, UtilConstant.RPC_TIMEOUT);
            // 协议要和服务端一致
            // 创建protocol
            TProtocol protocol = new TBinaryProtocol(transport);

            // 借助反射来产生需要生成的Service类型实例对象
            Class[] argsClass = new Class[]{
                    TProtocol.class
            };

            // 得到构造函数
            Constructor<T> cons = ts.getConstructor(argsClass);

            // 生成client对象
            client = cons.newInstance(protocol);

            // 开启数据传输
            transport.open();

            log.warn("RPC客户端创建: {}，连接到 {} : {}，开始传输数据", client.getClass(), ip, port);
        } catch (TException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.warn(e.getMessage(), e);
        }
    }
}
