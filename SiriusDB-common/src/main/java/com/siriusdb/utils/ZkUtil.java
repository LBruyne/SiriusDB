package com.siriusdb.utils;

import com.siriusdb.common.UtilConstant;
import com.siriusdb.common.ZkConstant;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * @Description: 工具类，获取本机节点名等信息
 * @author: liuxuanming
 * @date: 2021/05/10 3:33 下午
 */
public class ZkUtil {

    /**
     * @description: 获取Zookeeper注册的路径
     * @author: liuxuanming
     */
    public static String getRegisterPath() {
        return ZkConstant.ZNODE + "/" + ZkConstant.HOST_NAME_PREFIX + System.getenv().get("USER");
    }
    
    /**
     * @description: 获取本机名称
     * @author: liuxuanming
     */
    public static String getHostname() {
        return System.getenv().get("USERNAME");
    }
}
