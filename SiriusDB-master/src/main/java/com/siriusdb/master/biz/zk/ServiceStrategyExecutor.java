package com.siriusdb.master.biz.zk;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.enums.DataServerStateEnum;
import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.enums.StrategyTypeEnum;
import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.master.rpc.client.RegionServiceClient;
import com.siriusdb.model.master.DataServer;
import com.siriusdb.model.db.TableMeta;
import com.siriusdb.thrift.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

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

    @Override
    protected void finalize() throws Throwable {
        DataHolder.write();
        super.finalize();
    }

    /**
     * 根据类型执行不同的策略
     *
     * @param server
     * @param type
     */
    public void execStrategy(DataServer server, StrategyTypeEnum type) {
        try {
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
            DataHolder.write();
        } catch (IOException | TException e) {
            log.warn(e.getMessage(), e);
        }
    }

    private void execInvalidStrategy(DataServer server) {
        server.serverInvalid();
    }

    private void execRecoverStrategy(DataServer server) {
        server.serverRecover();
    }

    private void execDiscoverStrategy(DataServer server) throws TException {
        Integer validServerNum = DataHolder.getValidServerNum();

        if(validServerNum < MasterConstant.MIN_VALID_DATA_SERVER) {
            // 如果目前能够运行的服务器小于3台，提示无法完成服务
            log.warn("目前数据服务器数量少于{}，服务无法完成", MasterConstant.MIN_VALID_DATA_SERVER);
            DataServer serverNotInPair = DataHolder.getServerNotInPair();
            if(serverNotInPair == null) {
                // 没有机器不成对存在，设置这台机器成为备用机器
                server.serverIdle();
                log.warn("当前没有机器不成对存在，服务器{}成为备用机器", server.getHostName());
            } else {
                // 和未结对的机器进行结对
                DataHolder.remakeServerPair(server, serverNotInPair);
                log.warn("服务器{}和服务器{}重新结对", server.getHostName(), serverNotInPair.getHostName());
            }
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "目前数据服务器数量不足,服务处于非正常状态");
        } else {
            // 服务器数量足够
            // 如果目前数据服务器不成对存在，与不成对的那一台服务器结对
            log.warn("当前服务器数量充足");
            DataServer serverNotInPair = DataHolder.getServerNotInPair();
            if(serverNotInPair != null) {
                // 发现有服务器不成对存在，与未结对的机器进行结对
                DataHolder.remakeServerPair(server, serverNotInPair);
                log.warn("服务器{}和服务器{}重新结对", server.getHostName(), serverNotInPair.getHostName());
            }
            else {
                // 没有服务器不成对存在，成为备用服务器
                server.setState(DataServerStateEnum.IDLE);
                log.warn("服务器{}被设置为备用服务器", server);
                if(DataHolder.getIdleServerNum() > MasterConstant.REALLOCATE_PAIR_LOW_BOUND) {
                    // 备用机数量过多，可以重新分配一对备用机进行结对
                    DataHolder.makeServerPairFromIdleServer();
                }
            }
        }
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
            dataServers.get(hostName).parseHostUrl();
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

        static DataServer getServerNotInPair() {
            for(DataServer server: dataServers.values()) {
                // 运行中
                if(server.getState() == DataServerStateEnum.PRIMARY || server.getState() == DataServerStateEnum.COPY) {
                    // 但没有对偶机器
                    if(server.getDualServerId() == MasterConstant.NO_DUAL_SERVER) {
                        log.warn("查询到目前数据服务器{}处于未结对状态", server);
                        return server;
                    }
                }
            }
            return null;
        }

        /**
         * 从闲置服务器中选择两台结对
         */
        public static void makeServerPairFromIdleServer() {

            // TODO 通知机器
        }

        /**
         * 让一台服务器和一台已经结对过但配偶失效的服务器重新结对
         * @param server
         * @param serverNotInPair
         */
        static void remakeServerPair(DataServer server, DataServer serverNotInPair) throws TException {
            // 状态改变
            server.remakePair(serverNotInPair);

            // TODO 通知机器
            
            // 数据复制
            // 建立一个目标是serverNotInPair服务器的客户端
            RegionServiceClient client = new RegionServiceClient(RegionService.Client.class, serverNotInPair.getIp(), serverNotInPair.getPort());
            client.execTableCopy(Stream.of(UtilConstant.ALL_TABLE).collect(Collectors.toList()), server, serverNotInPair.getHostName());
        }

        public static TableMeta findTable(String name) {
            TableMeta result = null;
            for (TableMeta item : DataHolder.tableMetaList) {
                if (item.getName().equals(name)) {
                    result = item;
                }
            }
            return result;
        }

        public static void read() throws IOException, ClassNotFoundException {
            File file = new File(MasterConstant.META_INFO_STORAGE_FILE);
            if(!file.exists()) {
                dataServers = new HashMap<>();
                tableMetaList = new LinkedList<>();
                log.warn("服务器数据存储文件不存在，已重新为存储对象进行内存分配");
                return ;
            }
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
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
