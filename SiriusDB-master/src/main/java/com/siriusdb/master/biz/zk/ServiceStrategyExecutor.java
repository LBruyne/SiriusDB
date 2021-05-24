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
import java.sql.Array;
import java.util.*;
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
                case RECOVER:
                    execRecoverStrategy(server);
                    break;
                case DISCOVER:
                    execDiscoverStrategy(server);
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

    private void execInvalidStrategy(DataServer server) throws TException {
        if (server.getState() == DataServerStateEnum.PRIMARY || server.getState() == DataServerStateEnum.COPY) {
            // 该机器为主件机或者副本机
            if (DataHolder.getIdleServerNum() == 0) {
                // 没有备用机器，非正常状态
                DataHolder.getDataServerById(server.getDualServerId()).setDualServerId(MasterConstant.NO_DUAL_SERVER);
                server.serverInvalid();
                throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "服务器失效后没有备用机器，系统非正常状态");
            } else {
                // 有备用机器，进行一次结对
                DataServer serverNotInPair = DataHolder.getDataServerById(server.getDualServerId()); // 孤单的服务器
                serverNotInPair.setDualServerId(MasterConstant.NO_DUAL_SERVER);
                DataServer idleServer = DataHolder.getOneServerInIdle();                             // 闲置的服务器
                DataHolder.remakeServerPair(idleServer, serverNotInPair);
                server.serverInvalid();
            }
        } else if (server.getState() == DataServerStateEnum.IDLE) {
            if (DataHolder.getIdleServerNum() > 1) {
                // 当前不只有一台空闲机，直接失效
                server.serverInvalid();
            } else {
                server.serverInvalid();
                throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "空闲机数量不足，系统非正常状态");
            }
        } else {
            server.serverInvalid();
            throw new BasicBusinessException(ErrorCodeEnum.BASIC_VALIDATION_FAILED.getCode(), "失效的服务器原本的状态不合法");
        }
    }

    private void execRecoverStrategy(DataServer server) throws TException {
        server.serverRecover();
        execDiscoverStrategy(server);
    }

    private void execDiscoverStrategy(DataServer server) throws TException {
        Integer validServerNum = DataHolder.getValidServerNum();

        if (validServerNum < MasterConstant.MIN_VALID_DATA_SERVER) {
            // 如果目前能够运行的服务器小于3台，提示无法完成服务
            log.warn("目前数据服务器数量少于{}，服务无法完成", MasterConstant.MIN_VALID_DATA_SERVER);
            DataServer serverNotInPair = DataHolder.getServerNotInPair();
            if (serverNotInPair == null) {
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
            if (serverNotInPair != null) {
                // 发现有服务器不成对存在，与未结对的机器进行结对
                DataHolder.remakeServerPair(server, serverNotInPair);
                log.warn("服务器{}和服务器{}重新结对", server.getHostName(), serverNotInPair.getHostName());
            } else {
                // 没有服务器不成对存在，成为备用服务器
                server.setState(DataServerStateEnum.IDLE);
                log.warn("服务器{}被设置为备用服务器", server);
                if (DataHolder.getIdleServerNum() > MasterConstant.REALLOCATE_PAIR_LOW_BOUND) {
                    // 备用机数量过多，可以重新分配一对备用机进行结对
                    DataHolder.makeServerPairFromIdleServer(server);
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

        public static DataServer getDataServerById(Integer id) {
            for (DataServer server : dataServers.values()) {
                if (server.getId().equals(id)) {
                    return server;
                }
            }
            return null;
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
            for (DataServer server : dataServers.values()) {
                // 运行中
                if (server.getState() == DataServerStateEnum.PRIMARY || server.getState() == DataServerStateEnum.COPY) {
                    // 但没有对偶机器
                    if (server.getDualServerId() == MasterConstant.NO_DUAL_SERVER) {
                        log.warn("查询到目前数据服务器{}处于未结对状态", server);
                        return server;
                    }
                }
            }
            return null;
        }

        /**
         * 从闲置服务器中选择一台和当前服务器不一样的
         *
         * @param server
         * @return
         */
        static DataServer getOneServerInIdle(DataServer server) {
            for (DataServer dataServer : dataServers.values()) {
                if (dataServer.getState() == DataServerStateEnum.IDLE && dataServer != server) {
                    return dataServer;
                }
            }
            return null;
        }

        /**
         * 从闲置服务器中选择一台
         *
         * @return
         */
        static DataServer getOneServerInIdle() {
            for (DataServer dataServer : dataServers.values()) {
                if (dataServer.getState() == DataServerStateEnum.IDLE) {
                    return dataServer;
                }
            }
            return null;
        }

        /**
         * 从闲置服务器中选择两台结对
         */
        public static void makeServerPairFromIdleServer(DataServer server) throws TException {
            DataServer server2 = getOneServerInIdle(server);
            if (server2 == null)
                throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "不存在其他闲置服务器");

            server.makePair(server2);

            // 建立一个目标是server服务器的客户端
            RegionServiceClient client = new RegionServiceClient(RegionService.Client.class, server.getIp(), server.getPort());
            // 通知机器状态变化
            client.notifyStateChange(server, server.getHostName());

            // 建立一个目标是server2服务器的客户端
            client = new RegionServiceClient(RegionService.Client.class, server2.getIp(), server2.getPort());
            // 通知机器状态变化
            client.notifyStateChange(server2, server2.getHostName());
            log.warn("两台闲置服务器完成结对：主机{}，副机{}", server.getHostName(), server2.getHostName());
        }

        /**
         * 让一台服务器和一台已经结对过但配偶失效的服务器重新结对
         *
         * @param server
         * @param serverNotInPair
         */
        static void remakeServerPair(DataServer server, DataServer serverNotInPair) throws TException {
            // 状态改变
            server.remakePair(serverNotInPair);

            // 建立一个目标是server服务器的客户端
            RegionServiceClient client = new RegionServiceClient(RegionService.Client.class, server.getIp(), server.getPort());
            // 通知机器状态变化
            client.notifyStateChange(serverNotInPair, serverNotInPair.getHostName());

            // 建立一个目标是serverNotInPair服务器的客户端
            client = new RegionServiceClient(RegionService.Client.class, serverNotInPair.getIp(), serverNotInPair.getPort());
            // 通知机器状态变化
            client.notifyStateChange(serverNotInPair, serverNotInPair.getHostName());
            // 通知机器数据复制
            client.execTableCopy(Stream.of(UtilConstant.ALL_TABLE).collect(Collectors.toList()), server, serverNotInPair.getHostName());
            log.warn("一台闲置服务器和一台孤单的机器完成结对：{}，{}", server.getHostName(), serverNotInPair.getHostName());
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

        public static boolean isServiceAbnormalState() {
            if (getRunningServerNum() < 3) return false;         // 条件1：数量充足
            else if (getServerNotInPair() != null) return false; // 条件2：完成结对
            else if (getIdleServerNum() == 0) return false;      // 条件3：有空闲机备用
            else return true;
        }

        public static Boolean isTableExisted(String name) {
            for (TableMeta tableMeta : tableMetaList) {
                if (tableMeta.getName().equals(name)) return true;
            }
            return false;
        }

        public static void removeTableMeta(String tableName) {
            tableMetaList.removeIf(tableMeta -> tableMeta.getName().equals(tableName));
        }

        public static void addTableData(TableMeta tableMeta) {
            if (isTableExisted(tableMeta.getName()) || tableMetaList.contains(tableMeta)) {
                throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "该表格已经存在");
            } else {
                tableMetaList.add(tableMeta);
            }
        }

        public static void updateTableMeta(TableMeta tableMeta) {
            int i;
            for (i = 0; i < tableMetaList.size(); i++) {
                if (tableMetaList.get(i).getName().equals(tableMeta.getName())) break;
            }
            if (i == tableMetaList.size()) {
                throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "该表格不存在");
            } else {
                tableMetaList.set(i, tableMeta);
            }
        }

        /**
         * 根据负载均衡返回合适的dataServer的ID
         *
         * @return
         */
        public static Integer allocateLocatedServer() {
            // 得到主件机组成的列表
            List<DataServer> primaryServers = dataServers.values()
                    .stream()
                    .filter(server -> server.getState() == DataServerStateEnum.PRIMARY).collect(Collectors.toList());

            // 得到每一个主件机拥有的表格数量列表
            List<Integer> counts = primaryServers.stream().map(server -> {
                int count = 0;
                for(TableMeta tableMeta : tableMetaList) {
                    if(tableMeta.getLocatedServerName().equals(server.getHostName())) {
                        count++;
                    }
                }
                return count;
            }).collect(Collectors.toList());

            // 查找最小值对应的ID
            return counts.indexOf(Collections.min(counts));
        }

        public static void read() throws IOException, ClassNotFoundException {
            File file = new File(MasterConstant.META_INFO_STORAGE_FILE);
            if (!file.exists()) {
                dataServers = new HashMap<>();
                tableMetaList = new LinkedList<>();
                log.warn("服务器数据存储文件不存在，已重新为存储对象进行内存分配");
                return;
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
