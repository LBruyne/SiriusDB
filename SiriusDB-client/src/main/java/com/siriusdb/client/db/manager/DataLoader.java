package com.siriusdb.client.db.manager;

import com.siriusdb.client.rpc.client.MasterServiceClient;
import com.siriusdb.client.rpc.client.RegionServiceClient;
import com.siriusdb.common.MasterConstant;
import com.siriusdb.model.db.Table;


import com.siriusdb.model.master.DataServer;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.thrift.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;


@Slf4j
public class DataLoader {

    public static Table getTable(String tableName){
        return null;
    }

    public static void createTable(Table newTable, DataServer server) throws TException {
        MasterServiceClient client1 = new MasterServiceClient(MasterService.Client.class, "127.0.0.1", 2345);
        RegionServiceClient client2 = new RegionServiceClient(RegionService.Client.class, server.getIp(), server.getPort());
        if(client1.createTable(newTable, server.getHostName())){
            client2.trueCreateTable(newTable, server.getHostName());
        }
    }

    public static void dropTable(Table newTable, DataServer server) throws TException {
        MasterServiceClient client1 = new MasterServiceClient(MasterService.Client.class, "127.0.0.1", 2345);
        RegionServiceClient client2 = new RegionServiceClient(RegionService.Client.class, server.getIp(), server.getPort());
        if(client1.dropTable(newTable, server.getHostName())){
            client2.trueDropTable(newTable, server.getHostName());
        }
    }
}
