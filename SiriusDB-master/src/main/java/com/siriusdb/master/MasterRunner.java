package com.siriusdb.master;

import com.siriusdb.master.biz.BaseManager;
import com.siriusdb.master.server.UserServiceServer;

import javax.annotation.Resource;

/**
 * @Description: Master服务启动入口
 * @author: liuxuanming
 * @date: 2021/04/29 5:05 下午
 */
public class MasterRunner {

    public static void main(String args[]) {
        System.out.println("Hello Master!");

        UserServiceServer serviceServer = new UserServiceServer();
        serviceServer.startServer();
    }
}
