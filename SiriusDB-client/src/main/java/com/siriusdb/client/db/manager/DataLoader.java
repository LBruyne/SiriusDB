package com.siriusdb.client.db.manager;

import com.siriusdb.client.rpc.client.MasterServiceClient;
import com.siriusdb.client.rpc.client.RegionServiceClient;
import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.enums.DataTypeEnum;
import com.siriusdb.model.db.*;


import com.siriusdb.model.master.DataServer;
import com.siriusdb.thrift.model.*;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.thrift.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.*;


@Slf4j
public class DataLoader {
    private static Table thisTable = null;

    private static List<QueryTableMetaInfoResponse> buffer = null;

    static {
        fakeInitTable();
        buffer = new LinkedList<>();
    }

    private static void fakeSetTable(Table newTable) {
        DataLoader.thisTable = newTable;
    }

    private static Table fakeGetTable() {
        return DataLoader.thisTable;
    }

    private static void fakeInitTable() {
        if (DataLoader.thisTable == null) {
            Table student = new Table();
            List<Attribute> attr = new LinkedList<>();
            List<Row> data = new LinkedList<>();
            TableMeta meta = new TableMeta();
            meta.setName("student");
            meta.setPrimaryKey("sid");
            attr.add(new Attribute(0, "sid", DataTypeEnum.INTEGER.getType()));
            attr.add(new Attribute(1, "sname", DataTypeEnum.STRING.getType()));
            attr.add(new Attribute(2, "sage", DataTypeEnum.INTEGER.getType()));
            attr.add(new Attribute(3, "sgender", DataTypeEnum.FLOAT.getType()));
            meta.setAttributes(attr);
            student.setMeta(meta);

            for (int i = 0; i < 5; i++) {
                List<Element> thisRow = new LinkedList<>();
                thisRow.add(new Element(i, 0, DataTypeEnum.INTEGER.getType()));
                thisRow.add(new Element("cb_" + i, 1, DataTypeEnum.STRING.getType()));
                thisRow.add(new Element(20 + i, 2, DataTypeEnum.INTEGER.getType()));
                thisRow.add(new Element(1.0 + i, 3, DataTypeEnum.FLOAT.getType()));
                data.add(new Row(thisRow));
            }
            student.setData(data);
            DataLoader.thisTable = student;
        }
    }

    public static Table getTable(Boolean isFakeGetTable, String tableName) {
        if (isFakeGetTable) {
            return fakeGetTable();
        } else {
            return getTable(tableName);
        }
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

        MasterServiceClient client1 = new MasterServiceClient(MasterService.Client.class, MasterConstant.MASTER_SERVER_IP, MasterConstant.MASTER_SERVER_PORT);
        QueryTableMetaInfoResponse res = client1.dropTable(oldTable, UtilConstant.HOST_NAME);

        if (res.getMeta() == null || res.getMeta().size() == 0) {
            log.warn("向Master请求{}表格失败，表格不存在", oldTable.getMeta().getName());
        } else {
            DataServer target = new DataServer();
            target.setHostName(res.getMeta().get(0).getLocatedServerName());
            target.setHostUrl(res.getMeta().get(0).getLocatedServerUrl());
            target.parseHostUrl();
            if (res.meta.get(0).locatedServerName.length() != 0) {
                oldTable.getMeta().setLocatedServerName(res.meta.get(0).locatedServerName);
                oldTable.getMeta().setLocatedServerUrl(res.meta.get(0).locatedServerUrl);
                RegionServiceClient client2 = new RegionServiceClient(RegionService.Client.class, target.getIp(), target.getPort());
                client2.trueDropTable(oldTable, res.meta.get(0).locatedServerName);
            }
        }
    }


    //real get table function, a is any integer
    public static Table getTable(String tableName) {

//        MasterServiceClient client1 = new MasterServiceClient(MasterService.Client.class, MasterConstant.MASTER_SERVER_IP, MasterConstant.MASTER_SERVER_PORT);
//        QueryTableMetaInfoResponse res = null;
//        try {
//            res = client1.getTable(tableName, UtilConstant.HOST_NAME);
//        } catch (TException e) {
//            e.printStackTrace();
//        }
//        Table ret = null;
//        DataServer target = new DataServer();
//        if (res.getMeta() == null || res.getMeta().size() == 0) {
//            log.warn("向Master请求{}表格失败，表格不存在", tableName);
        Table ret = null;
        QueryTableMetaInfoResponse res = realFindTable(tableName);
        DataServer target = new DataServer();

        VTableMeta thisTableMeta = res.getMeta().get(0);
        target.setHostName(thisTableMeta.getLocatedServerName());
        target.setHostUrl(thisTableMeta.getLocatedServerUrl());
        target.parseHostUrl();

        log.warn("向Master请求GET表格成功，表格现在被存储在主机{}:{}:{}", target.getHostName(), target.getIp(), target.getPort());

        if (thisTableMeta.getLocatedServerName().length() != 0) {
//                newTable.getMeta().setLocatedServerName(res.locatedServerName);
//                newTable.getMeta().setLocatedServerUrl(res.locatedServerUrl);
//                RegionServiceClient client2 = new RegionServiceClient(RegionService.Client.class, target.getIp(), target.getPort());
//                client2.trueCreateTable(newTable, res.locatedServerName);

            RegionServiceClient client2 = new RegionServiceClient(RegionService.Client.class, target.getIp(), target.getPort());
            try {
                ret = client2.trueGetTable(tableName, thisTableMeta.getLocatedServerName());
            } catch (TException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public static void alterTable(Table table) throws TException {
        String tableName = table.getMeta().getName();
//        MasterServiceClient client1 = new MasterServiceClient(MasterService.Client.class, MasterConstant.MASTER_SERVER_IP, MasterConstant.MASTER_SERVER_PORT);
//        QueryTableMetaInfoResponse res = client1.getTable(tableName, UtilConstant.HOST_NAME);
//        Table ret = null;
//        DataServer target = new DataServer();
//        if (res.getMeta() == null || res.getMeta().size() == 0) {
//            log.warn("Alter Table: 向Master请求{}表格失败，表格不存在", tableName);
//        } else {
        QueryTableMetaInfoResponse res = realFindTable(tableName);
        DataServer target = new DataServer();
        VTableMeta thisTableMeta = res.getMeta().get(0);
        target.setHostName(thisTableMeta.getLocatedServerName());
        target.setHostUrl(thisTableMeta.getLocatedServerUrl());
        target.parseHostUrl();

        log.warn("Alter Table: 向Master请求GET表格成功，表格现在被存储在主机{}:{}:{}", target.getHostName(), target.getIp(), target.getPort());

        if (thisTableMeta.getLocatedServerName().length() != 0) {
            RegionServiceClient client2 = new RegionServiceClient(RegionService.Client.class, target.getIp(), target.getPort());
            client2.trueRetransmitTable(table, thisTableMeta.getLocatedServerName());
        }

    }

    public static void alterTable(Boolean isFakeGetTable, Table table) throws TException {
        if (isFakeGetTable) {
            fakeSetTable(table);
        } else {
            alterTable(table);
        }
    }

    private static QueryTableMetaInfoResponse findTable(String tableName) {
        int metaID = findMetaID(tableName);
        QueryTableMetaInfoResponse res = null;
        if (findMetaID(tableName) != -1) {
            res = buffer.get(metaID);
            new Thread(new refreshBuffer(buffer, metaID)).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {

            MasterServiceClient client1 = new MasterServiceClient(MasterService.Client.class, MasterConstant.MASTER_SERVER_IP, MasterConstant.MASTER_SERVER_PORT);

            try {
                res = client1.getTable(tableName, UtilConstant.HOST_NAME);
            } catch (TException e) {
                e.printStackTrace();
            }
            if (res.getMeta() == null || res.getMeta().size() == 0) {
                log.warn("向Master请求{}表格失败，表格不存在", tableName);
            }
        }
        return res;
    }

    private static QueryTableMetaInfoResponse realFindTable(String tableName) {
        QueryTableMetaInfoResponse res = findTable(tableName);
        if (res == null)
            res = findTable(tableName);
        return res;
    }

    private static int findMetaID(String tableName) {
        for (int i = 0; i < buffer.size(); i++) {
            if (buffer.get(i).getMeta().get(0).getName().equals(tableName))
                return i;
        }
        return -1;
    }

}

class refreshBuffer implements Runnable {
    private List<QueryTableMetaInfoResponse> ls;
    private int renew_ID;

    refreshBuffer(List<QueryTableMetaInfoResponse> list, int id) {
        ls = list;
        renew_ID = id;
    }

    @Override
    public void run() {
        QueryTableMetaInfoResponse res = null;

        MasterServiceClient client1 = new MasterServiceClient(MasterService.Client.class, MasterConstant.MASTER_SERVER_IP, MasterConstant.MASTER_SERVER_PORT);

        try {
            res = client1.getTable(ls.get(renew_ID).getMeta().get(0).getName(), UtilConstant.HOST_NAME);
        } catch (TException e) {
            e.printStackTrace();
        }
        if (res.getMeta() == null || res.getMeta().size() == 0) {
//            log.warn("向Master请求{}表格失败，表格不存在", tableName);
            ls.remove(res);
        } else {
            ls.set(renew_ID, res);
        }


    }
}