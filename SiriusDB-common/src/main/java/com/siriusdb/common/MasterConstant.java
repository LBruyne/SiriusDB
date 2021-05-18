package com.siriusdb.common;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * @Description: Master Server相关常量
 * @author: liuxuanming
 * @date: 2021/05/11 1:49 下午
 */
public class MasterConstant {

    /**
     * 发送RPC请求到Master Server需要访问的IP,
     * TODO：部署时修改为真实的
     */
    public static final String MASTER_SERVER_IP = "127.0.0.1";

    /**
     * 发送RPC请求到Master Server需要访问的Port
     */
    public static final Integer MASTER_SERVER_PORT = 2345;

    /**
     * Master Server自身监听的RPC端口
     */
    public static final Integer RPC_LISTEN_PORT = 2345;

    /**
     * 主副本机标识：没有对偶机器
     */
    public static final Integer NO_DUAL_SERVER = -1;
}
