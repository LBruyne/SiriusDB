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
    public static final String MASTER_SERVER_IP = "192.168.43.82";

    /**
     * 发送RPC请求到Master Server需要访问的Port
     */
    public static final Integer MASTER_SERVER_PORT = 2345;

    /**
     * MASTER主机名
     */
    public static final String MASTER_HOST_NAME = "MASTER";

    /**
     * Master Server自身监听的RPC端口
     */
    public static final Integer RPC_LISTEN_PORT = 2345;

    /**
     * 主副本机标识：没有对偶机器
     */
    public static final Integer NO_DUAL_SERVER = -1;

    /**
     * meta数据本地保存的文件
     */
    public static final String META_INFO_STORAGE_FILE = "meta.dat";

    /**
     * 集群能够运行需要的最小服务器数量
     */
    public static final Integer MIN_VALID_DATA_SERVER = 3;

    /**
     * 闲置服务器大于2则可以重新分配一对
     */
    public static final Integer REALLOCATE_PAIR_LOW_BOUND = 2;

}
