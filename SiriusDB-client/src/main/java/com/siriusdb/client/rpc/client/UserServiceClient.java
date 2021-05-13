package com.siriusdb.client.rpc.client;

import com.siriusdb.thrift.UserService;
import com.siriusdb.utils.rpc.DynamicThriftClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

/**
 * @Description: UserService客户端
 * @author: liuxuanming
 * @date: 2021/05/09 11:10 上午
 */
@Slf4j
public class UserServiceClient extends DynamicThriftClient<UserService.Client> {

    public UserServiceClient(Class<UserService.Client> ts) {
        super(ts);
    }

    public UserServiceClient(Class<UserService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

}
