package com.siriusdb.master.biz.zk;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.enums.DataServerStateEnum;
import com.siriusdb.enums.StrategyTypeEnum;
import com.siriusdb.master.rpc.client.RegionServiceClient;
import com.siriusdb.model.HostUrl;
import com.siriusdb.model.master.DataServer;
import com.siriusdb.model.master.TableMeta;
import com.siriusdb.thrift.model.Base;
import com.siriusdb.thrift.model.QueryTableMetaInfoRequest;
import com.siriusdb.thrift.service.RegionService;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
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
@Slf4j
public class ServiceStrategyExecutor {

    public ServiceStrategyExecutor() {
        try {
            DataHolder.read();
        } catch (IOException | ClassNotFoundException e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * 根据类型执行不同的策略
     *
     * @param server
     * @param type
     */
    public void execStrategy(DataServer server, StrategyTypeEnum type) {
        switch (type) {
            case DISCOVER:
                execDiscoverStrategy(server);
                break;
            case RECOVER:
                execRecoverStrategy(server);
                break;
            case INVALID:
                execInvalidStrategy(server);
                break;
        }
    }

    private void execInvalidStrategy(DataServer server) {
    }

    private void execRecoverStrategy(DataServer server) {

    }

    private void execDiscoverStrategy(DataServer server) {

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
                .setName(Stream.of(UtilConstant.ALL_TABLE).collect(Collectors.toList()));

        // 发起客户端调用，获取该服务器上所有表格的元数据
        client.getTableMetaInfo(request);
    }

    public static class DataHolder {

        public static Map<String, DataServer> dataServers = new HashMap<>();

        public static List<TableMeta> tableMetaList = new LinkedList<>();

        public static void addServer(String hostName, String hostUrl) {
            dataServers.put(hostName,
                    DataServer.builder()
                            .hostName(hostName)
                            .hostUrl(hostUrl)
                            .id(dataServers.size())
                            .state(DataServerStateEnum.IDLE)
                            .dualServerId(MasterConstant.NO_DUAL_SERVER)
                            .build());
        }

        static Integer getPrimaryServerNum() {
            Integer num = 0;
            for (DataServer server : dataServers.values()) {
                if (server.getState() == DataServerStateEnum.PRIMARY) num++;
            }
            return num;
        }

        static Integer getCopyServerNum() {
            Integer num = 0;
            for (DataServer server : dataServers.values()) {
                if (server.getState() == DataServerStateEnum.COPY) num++;
            }
            return num;
        }

        static Integer getIdleServerNum() {
            Integer num = 0;
            for (DataServer server : dataServers.values()) {
                if (server.getState() == DataServerStateEnum.IDLE) num++;
            }
            return num;
        }

        static Integer getInvalidServerNum() {
            Integer num = 0;
            for (DataServer server : dataServers.values()) {
                if (server.getState() == DataServerStateEnum.INVAILID) num++;
            }
            return num;
        }

        static Integer getRunningServerNum() {
            return getPrimaryServerNum() + getCopyServerNum();
        }

        static Integer getValidServerNum() {
            return getIdleServerNum() + getRunningServerNum();
        }

        public static TableMeta findTableMetaInfo(String name) {
            TableMeta result = null;
            for (TableMeta item : DataHolder.tableMetaList) {
                if (item.getName().equals(name)) {
                    result = item;
                }
            }
            return result;
        }

        public static void read() throws IOException, ClassNotFoundException {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(MasterConstant.META_INFO_STORAGE_FILE));
            dataServers = (Map<String, DataServer>) in.readObject();
            tableMetaList = (List<TableMeta>) in.readObject();
            log.warn("从文件{}中读取对象{}和{}", MasterConstant.META_INFO_STORAGE_FILE, dataServers, tableMetaList);
            in.close();
        }

        public static void write() throws IOException {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(MasterConstant.META_INFO_STORAGE_FILE));
            out.writeObject(dataServers);
            out.writeObject(tableMetaList);
            log.warn("将对象{}和{}写入到文件{}中", dataServers, tableMetaList, MasterConstant.META_INFO_STORAGE_FILE);
            out.close();
        }
    }
}
