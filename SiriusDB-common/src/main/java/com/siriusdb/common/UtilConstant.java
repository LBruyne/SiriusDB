package com.siriusdb.common;

/**
 * @Description: 公共常量写在这里
 * @author: liuxuanming
 * @date: 2021/04/29 4:37 下午
 */
public class UtilConstant {

    /**
     * 本机主机地址
     * TODO：部署时修改为真实的
     */
    public static final String HOST_URL = "192.168.0.1:2345";

    /**
     * RPC超时时间
     */
    public static final Integer RPC_TIMEOUT = 3000;

    /**
     * 表格数据请求中，代表请求所有表格数据
     */
    public static final String ALL_TABLE = "ALL";

    /**
     * 表格数据请求中，代表所有列数据
     */
    public static final String ALL_COLUMN = "ALL";

    /**
     * 获得本机名称
     * @return
     */
    public static String getHostname() {
        return System.getenv().get("USER");
    }

}
