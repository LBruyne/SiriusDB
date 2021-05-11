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

/**
 * @Description: UserService客户端
 * @author: liuxuanming
 * @date: 2021/05/09 11:10 上午
 */
public class UserServiceClient extends BasicServiceClient<UserService.Iface> {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);

    /**
     * 外部方法调用 测试
     */
    public void testUserService() {
        try {
            System.out.println(client.getName(1));
            System.out.println(client.isExist("LIUXUANMING"));
        } catch (TException e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
