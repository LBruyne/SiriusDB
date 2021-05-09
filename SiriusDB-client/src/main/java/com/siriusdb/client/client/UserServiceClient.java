package com.siriusdb.client.client;

import com.siriusdb.thrift.UserService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * @Description: UserService客户端
 * @author: liuxuanming
 * @date: 2021/05/09 11:10 上午
 */
public class UserServiceClient {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 2345; //Thrift server listening port
    private static final int TIMEOUT = 3000;

    public void startClient(String userName) {
        TTransport transport = null;
        try {
            transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
            // 协议要和服务端一致
            TProtocol protocol = new TBinaryProtocol(transport);
            UserService.Client client = new UserService.Client(protocol);
            transport.open();
            System.out.println(client.getName(1));
            System.out.println(client.isExist(userName));
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }
}
