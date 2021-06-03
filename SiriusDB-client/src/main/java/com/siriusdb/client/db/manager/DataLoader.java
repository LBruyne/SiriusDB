package com.siriusdb.client.db.manager;

import com.siriusdb.client.rpc.client.MasterServiceClient;
import com.siriusdb.client.rpc.client.RegionServiceClient;
import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.model.db.Table;


import com.siriusdb.model.master.DataServer;
import com.siriusdb.thrift.model.QueryCreateTableResponse;
import com.siriusdb.thrift.model.QueryTableMetaInfoResponse;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.thrift.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;


@Slf4j
public class DataLoader {

    public static Table getTable(String tableName){
        return null;
    }

    public static void createTable(Table newTable) throws TException {

        MasterServiceClient client1 = new MasterServiceClient(MasterService.Client.class, "127.0.0.1", 2345);
        QueryCreateTableResponse res = client1.createTable(newTable, UtilConstant.HOST_NAME);

        if(res.locatedServerName.length()!=0){
            newTable.getMeta().setLocatedServerName(res.locatedServerName);
            newTable.getMeta().setLocatedServerUrl(res.locatedServerUrl);
            RegionServiceClient client2 = new RegionServiceClient(RegionService.Client.class);
            client2.trueCreateTable(newTable, res.locatedServerName);
        }

    }

    public static void dropTable(Table oldTable) throws TException {

        MasterServiceClient client1 = new MasterServiceClient(MasterService.Client.class, "127.0.0.1", 2345);
        QueryTableMetaInfoResponse res = client1.dropTable(oldTable, UtilConstant.HOST_NAME);

        if(res.meta.get(0).locatedServerName.length()!=0){
            oldTable.getMeta().setLocatedServerName(res.meta.get(0).locatedServerName);
            oldTable.getMeta().setLocatedServerUrl(res.meta.get(0).locatedServerUrl);
            RegionServiceClient client2 = new RegionServiceClient(RegionService.Client.class);
            client2.trueDropTable(oldTable, res.meta.get(0).locatedServerName);
        }

    }
}
