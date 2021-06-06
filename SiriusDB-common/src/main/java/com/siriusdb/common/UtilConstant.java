package com.siriusdb.common;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * @Description: 公共常量写在这里
 * @author: liuxuanming
 * @date: 2021/04/29 4:37 下午
 */
@Slf4j
public class UtilConstant {

    public static final Integer INF = 10000000;
    /**
     * 本机主机地址
     * TODO：部署时修改为真实的
     */
    public static String HOST_URL = getHostAddress() + ":" + RegionConstant.port.toString();

    /**
     * 主机IP
     */
    public static String HOST_IP = getHostAddress();

    /**
     * 随机的本机主机名
     */
    public static String HOST_NAME = getHostname();

    /**
     * RPC超时时间
     */
    public static final Integer RPC_TIMEOUT = 10000;

    /**
     * 表格数据请求中，代表请求所有表格数据
     */
    public static final String ALL_TABLE = "ALL_TABLE";

    /**
     * 表格数据请求中，代表所有列数据
     */
    public static final String ALL_COLUMN = "ALL_TABLE";

    /**
     * 找不到
     */
    public static final Integer NOT_FOUND = -1;

    /**
     * 获得本机IP
     *
     * @return
     */
    private static String getHostAddress() {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("获取本机IP失败");
        }
        return ip;
    }


    /**
     * 随机获得本机名称
     *
     * @return
     */
    public static String getHostname() {
        if (HOST_NAME != null) return HOST_NAME;
        String basicCharSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(basicCharSet.length());
            sb.append(basicCharSet.charAt(number));
        }
        return sb.toString();
    }
}
