package com.siriusdb.master.biz.zk;

import com.siriusdb.common.UtilConstant;
import com.siriusdb.master.rpc.client.RegionServiceClient;
import com.siriusdb.model.HostUrl;
import com.siriusdb.thrift.model.Base;
import com.siriusdb.thrift.model.QueryTableMetaInfoRequest;
import com.siriusdb.thrift.service.RegionService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        getMetaInfoFromRegionServer(hostName, hostUrl);
    }

    public void deleteDataServer(String hostName) {
        log.warn("删除服务器节点：主机名 {}", hostName);
        dataServerInfo.remove(hostName);
    }

    public void updateDataServer(String hostName, String hostUrl) {
        log.warn("更新服务器节点：主机名 {}, 地址 {}", hostName, hostUrl);
        dataServerInfo.replace(hostName, hostUrl);
    }

    private void getMetaInfoFromRegionServer(String hostName, String hostUrl) {
        HostUrl targetUrl = HostUrl.parseHostUrl(hostUrl);
        RegionServiceClient client = new RegionServiceClient(RegionService.Client.class, targetUrl.getIp(), targetUrl.getPort());

        // 构造请求对象
        QueryTableMetaInfoRequest request = new QueryTableMetaInfoRequest();
        request.setBase(new Base()
                .setCaller(UtilConstant.getHostname())
                .setHostName(hostName)
                .setHostUrl(hostUrl))
               .setTableName(Stream.of(UtilConstant.ALL_TABLE).collect(Collectors.toList()));

        // 发起客户端调用，获取该服务器上所有表格的元数据
        client.getTableMetaInfo(request);
    }
}
