package com.siriusdb.client.db.manager;

import com.siriusdb.client.rpc.client.MasterServiceClient;
import com.siriusdb.client.rpc.client.RegionServiceClient;
import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.model.db.*;


import com.siriusdb.model.master.DataServer;
import com.siriusdb.thrift.model.QueryCreateTableResponse;
import com.siriusdb.thrift.model.QueryTableMetaInfoResponse;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.thrift.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.*;


@Slf4j
public class DataLoader {

    public static Table getTable(String tableName) {
        Table student = new Table();
        List<Attribute> attr = new LinkedList<>();
        List<Row> data = new LinkedList<>();
        TableMeta meta = new TableMeta();
        meta.setName("student");
        meta.setPrimaryKey("sno");
        attr.add(new Attribute(1,"sno","string"));
        attr.add(new Attribute(2,"sname","string"));
        attr.add(new Attribute(3,"sage","int"));
        attr.add(new Attribute(4,"sgender","float") );
        meta.setAttributes(attr);
        student.setMeta(meta);

        for(int i=0;i<5;i++){
            List<Element> thisRow = new LinkedList<>();
            thisRow.add(new Element("318010001"+i,1,"string"));
            thisRow.add(new Element("cb_"+i,2,"string"));
            thisRow.add(new Element(20+i,3,"int"));
            thisRow.add(new Element(1.0+i,4,"float"));
            data.add(new Row(thisRow));
        }
        student.setData(data);
        return student;
    }

    public static void createTable(Table newTable) throws TException {

        MasterServiceClient client1 = new MasterServiceClient(MasterService.Client.class, MasterConstant.MASTER_SERVER_IP, MasterConstant.MASTER_SERVER_PORT);
        QueryCreateTableResponse res = client1.createTable(newTable, UtilConstant.HOST_NAME);

        DataServer target = new DataServer();
        target.setHostName(res.getLocatedServerName());
        target.setHostUrl(res.getLocatedServerUrl());
        target.parseHostUrl();
        log.warn("向Master请求创建表格成功，表格将被存储在主机{}:{}:{}", target.getHostName(), target.getIp(), target.getPort());

        if (res.locatedServerName.length() != 0) {
            newTable.getMeta().setLocatedServerName(res.locatedServerName);
            newTable.getMeta().setLocatedServerUrl(res.locatedServerUrl);
            RegionServiceClient client2 = new RegionServiceClient(RegionService.Client.class, target.getIp(), target.getPort());
            client2.trueCreateTable(newTable, res.locatedServerName);
        }

    }

    public static void dropTable(Table oldTable) throws TException {

        MasterServiceClient client1 = new MasterServiceClient(MasterService.Client.class, "127.0.0.1", 2345);
        QueryTableMetaInfoResponse res = client1.dropTable(oldTable, UtilConstant.HOST_NAME);

        if (res.meta.get(0).locatedServerName.length() != 0) {
            oldTable.getMeta().setLocatedServerName(res.meta.get(0).locatedServerName);
            oldTable.getMeta().setLocatedServerUrl(res.meta.get(0).locatedServerUrl);
            RegionServiceClient client2 = new RegionServiceClient(RegionService.Client.class);
            client2.trueDropTable(oldTable, res.meta.get(0).locatedServerName);
        }

    }


    public static void getTestTable() {
        Table student = new Table();

    }
}
