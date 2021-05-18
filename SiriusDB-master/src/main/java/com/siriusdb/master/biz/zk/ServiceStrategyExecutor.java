package com.siriusdb.master.biz.zk;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.enums.DataServerStateEnum;
import com.siriusdb.enums.StrategyTypeEnum;
import com.siriusdb.master.rpc.client.RegionServiceClient;
import com.siriusdb.model.HostUrl;
import com.siriusdb.model.master.DataServer;
import com.siriusdb.model.master.TableMetaInfo;
import com.siriusdb.thrift.model.Base;
import com.siriusdb.thrift.model.QueryTableMetaInfoRequest;
import com.siriusdb.thrift.service.RegionService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 对服务器进行策略执行
 * @author: liuxuanming
 * @date: 2021/05/18 6:14 下午
 */
public class ServiceStrategyExecutor {

    public void exceStrategy(DataServer server, StrategyTypeEnum type) {
        switch (type) {
            case NEW_COME:
                break;
            case RECOVER:
                break;
            case INVALID:
                break;
        }
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

    static class DataHolder {

        static Integer dataServerNum = 0;

        static Map<String, DataServer> dataServerInfo = new HashMap<>();

        static List<TableMetaInfo> tableMetaInfoList = new LinkedList<>();

        static void addServer(String hostName, String hostUrl) {
            dataServerInfo.put(hostName,
                    DataServer.builder()
                            .hostName(hostName)
                            .hostUrl(hostUrl)
                            .id(dataServerNum++)
                            .state(DataServerStateEnum.IDLE)
                            .dualServerId(MasterConstant.NO_DUAL_SERVER)
                            .build());
        }

        static TableMetaInfo findTableMetaInfo(String name) {
            TableMetaInfo result = null;
            for(TableMetaInfo item : DataHolder.tableMetaInfoList) {
                if(item.getName().equals(name)) {
                    result = item;
                }
            }
            return result;
        }
    }
}
