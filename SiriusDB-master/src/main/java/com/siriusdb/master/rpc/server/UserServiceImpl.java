package com.siriusdb.master.rpc.server;

import com.siriusdb.thrift.service.UserService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: UserService实现类，服务端要必须实现UserService中的UserService.Iface接口，为其提供具体的业务逻辑
 * @author: liuxuanming
 * @date: 2021/05/09 11:04 上午
 */
public class UserServiceImpl implements UserService.Iface {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String NAME = "LIUXUANMING";

    @Override
    public String getName(int id) throws TException {
        logger.warn("方法 getName 接收到数据, id = {}:", id);
        return NAME;
    }

    @Override
    public boolean isExist(String name) throws TException {
        logger.warn("方法 isExist 接收到数据, name = {}", name);
        return NAME.equals(name);
    }
}
