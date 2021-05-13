package com.siriusdb.master.biz.zk;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 根据ZooKeeper监视器得到的各种事件实行相应的策略
 * @author: liuxuanming
 * @date: 2021/05/13 12:52 下午
 */
@Slf4j
public class ServiceStrategyManager {

    private static Map<String, String> dataServerInfo = new HashMap<>();

    public static Map<String, String> getDataServerInfo() {
        return dataServerInfo;
    }

    public void addDataServer(String hostName, String hostUrl) {
        log.warn("新增服务器节点：主机名 {}, 地址 {}", hostName, hostUrl);
        dataServerInfo.put(hostName, hostUrl);
    }

    public void deleteDataServer(String hostName) {
        log.warn("删除服务器节点：主机名 {}", hostName);
        dataServerInfo.remove(hostName);
    }

    public void updateDataServer(String hostName, String hostUrl) {
        log.warn("更新服务器节点：主机名 {}, 地址 {}", hostName, hostUrl);
        dataServerInfo.replace(hostName, hostUrl);
    }
}
