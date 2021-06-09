package com.siriusdb.region.rpc;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.enums.DataServerStateEnum;
import com.siriusdb.enums.RpcOperationEnum;
import com.siriusdb.model.db.Table;
import com.siriusdb.model.db.TableMeta;
import com.siriusdb.model.master.DataServer;
import com.siriusdb.thrift.model.*;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.thrift.service.RegionService;
import com.siriusdb.utils.copy.CopyUtils;
import com.siriusdb.utils.rpc.RpcResult;
import org.apache.thrift.TException;
import com.siriusdb.model.region.FileServer;
import lombok.extern.slf4j.Slf4j;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Slf4j
public class RegionServiceImpl implements RegionService.Iface {
    //返回一系列特定的表格数据
    @Override
    public QueryTableDataResponse queryTableData(QueryTableDataRequest req) throws TException {
        //声明一个临时对象用来存储读取出来的数据
        List<VTable> vtables = new ArrayList<>();
        //获取表名称
        List<String> tableName = req.getTableNames();
        List<String> tableName1 = new ArrayList<String>();
        if (tableName.get(0).equals(UtilConstant.ALL_TABLE)) {
            File file1 = new File(this.getClass().getResource("/").getPath());
            File file2 = new File(file1.getParent());
            File file3 = new File(file2.getParent());
            File file = new File(file3.getParent());
            File[] tempList = file.listFiles();
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile()) {
                    tableName1.add(tempList[i].toString());
                }
                if (tempList[i].isDirectory()) {
                }
            }
        } else {
            tableName1.addAll(tableName);
        }
        //建立循环
        for (int i = 0; i < tableName1.size(); i++) {
            /*Table tableTmp = new Table();
            VTable vtableTmp = null;*/
            File file = new File(UtilConstant.getHostname() + tableName1.get(i) + ".dat");
            FileInputStream in;
            try {
                in = new FileInputStream(file);
                ObjectInputStream objIn = new ObjectInputStream(in);
                Table tableTmp = (Table) objIn.readObject();
                objIn.close();
                //将table的值赋值给vtable
                VTable vtableTmp = CopyUtils.tableToVTable(tableTmp);
                //将新的添加到末尾
                vtables.add(vtableTmp);
                log.warn("queryTableData中此次添加的表名为:{}",vtableTmp);
            } catch (Exception e) {
                log.warn("Query指令查询失败");
                return new QueryTableDataResponse().setBaseResp(RpcResult.failResp());
            }
        }
        return new QueryTableDataResponse()
                .setBaseResp(RpcResult.successResp())
                .setTables(vtables);
    }

    //Master告知某服务器状态变化,存储该变化
    @Override
    public NotifyStateResponse notifyStateChange(NotifyStateRequest req) throws TException {
        DataServer dataServer = DataServer.builder().hostUrl(req.getDualServerUrl()).hostName(req.getDualServerName()).state(DataServerStateEnum.PRIMARY).build();
        dataServer.parseHostUrl();
        String stateCode = Integer.toString(req.getStateCode());
        switch (stateCode) {
            case "1":
                dataServer.setState(DataServerStateEnum.PRIMARY);
                break;
            case "2":
                dataServer.setState(DataServerStateEnum.COPY);
                break;
            case "0":
                dataServer.setState(DataServerStateEnum.IDLE);
                break;
            case "-1":
                dataServer.setState(DataServerStateEnum.INVAILID);
                break;
        }
        log.warn("接收到Master通知，状态变更为{}，对偶机为{}:{}", stateCode, dataServer.getHostName(), dataServer.getHostUrl());

        File file = new File(UtilConstant.getHostname() + "dualMachine.dat");
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(dataServer);
            objOut.flush();
            objOut.close();
        } catch (Exception e) {
            return new NotifyStateResponse().setBaseResp(RpcResult.failResp());
        }
        List<String> tableName = new ArrayList<String>();
        File file1 = new File(this.getClass().getResource("/").getPath());
        File file2 = new File(file1.getParent());
        File file3 = new File(file2.getParent());
        File file4 = new File(file3.getParent());
        log.warn("此时的项目地址为:");
        System.out.println(file4);
        File[] tempList = file4.listFiles();
        log.warn("temp:{}",tempList[2]);
        if (dataServer.getState() == DataServerStateEnum.COPY && tempList != null && tempList.length != 0) {
            MasterServerClient masterServerClient = new MasterServerClient(MasterService.Client.class, MasterConstant.MASTER_SERVER_IP, MasterConstant.MASTER_SERVER_PORT);
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile() && tempList[i].getName().contains(UtilConstant.getHostname()) && !tempList[i].getName().contains("dualMachine")) {
                    tableName.add(tempList[i].getName());
                }
                if (tempList[i].isDirectory()) {
                }
            }
            log.warn(UtilConstant.getHostname());
            log.warn("本地文件有：{}",tableName);
            for (int i = 0; i < tableName.size(); i++) {
            /*Table tableTmp = new Table();
            VTable vtableTmp = null;*/
                File file5 = new File(tableName.get(i));
                FileInputStream in;
                Table tableTmp = new Table();
                try {
                    in = new FileInputStream(file5);
                    ObjectInputStream objIn = new ObjectInputStream(in);
                    tableTmp = (Table) objIn.readObject();
                    objIn.close();
                    tableTmp.getMeta().setLocatedServerName(dataServer.getHostName());
                    tableTmp.getMeta().setLocatedServerUrl(dataServer.getHostUrl());
                    VTableMeta vTableMeta = CopyUtils.tableMToVTableM(tableTmp.getMeta());
                    masterServerClient.notifyTableMetaChange(tableName.get(i), RpcOperationEnum.UPDATE.getCode(), vTableMeta, new Base().setCaller("SERVER"+UtilConstant.getHostname()).setReceiver(MasterConstant.MASTER_HOST_NAME));
                    log.warn("报告master主机地址已经改了：{}",tableTmp.getMeta().getLocatedServerName());
                } catch (Exception e) {
                    log.warn("state变更失败");
                }
                FileOutputStream out1;
                try {
                    out1 = new FileOutputStream(file5);
                    ObjectOutputStream objOut = new ObjectOutputStream(out1);
                    objOut.writeObject(tableTmp);
                    objOut.flush();
                    objOut.close();
                } catch (Exception e) {
                    log.warn("state存取失败");
                }
                //log.warn("本地的table更新完毕，更新后的locatedname为：{}",tableTmp.getMeta().getLocatedServerName());
            }
        }
        return new NotifyStateResponse()
                .setBaseResp(RpcResult.successResp());
    }

    //要给副机也复制一份
    @Override
    public NotifyTableChangeResponse notifyTableChange(NotifyTableChangeRequest req) throws TException {
        int operationCode = req.getOperationCode();
        List<String> tableNames1 = req.getTableNames();
        List<String> tableNames = new ArrayList<>();
        List<VTable> vTableList = req.getTables();
        if(tableNames1.get(0).equals(UtilConstant.ALL_TABLE)){
            for(int i = 0;i < vTableList.size();i++){
                tableNames.add(vTableList.get(i).getMeta().getName());
            }
        }
        else{
            tableNames.addAll(tableNames1);
        }
        File fileState = new File(UtilConstant.getHostname() + "dualMachine.dat");
        FileInputStream in;
        DataServer dataServerDual = null;
        try {
            in = new FileInputStream(fileState);
            ObjectInputStream objIn = new ObjectInputStream(in);
            dataServerDual = (DataServer) objIn.readObject();
            log.warn("notifyTableChange中接收到Master通知，状态变更为{}，对偶机为{}:{}", dataServerDual.getState().getCode(), dataServerDual.getHostName(), dataServerDual.getHostUrl());
            objIn.close();
        } catch (Exception e) {
            log.warn("没有副机文件");
        }
        DataServer dataServer = DataServer.builder().hostUrl(dataServerDual.getHostUrl()).hostName(dataServerDual.getHostName()).build();
        dataServer.parseHostUrl();
        String dualIp = dataServer.getIp();
        Integer dualPort = dataServer.getPort();
        RegionServerClient regionServerClient = new RegionServerClient(RegionService.Client.class, dualIp, dualPort);
        MasterServerClient masterServerClient = new MasterServerClient(MasterService.Client.class, MasterConstant.MASTER_SERVER_IP, MasterConstant.MASTER_SERVER_PORT);
        log.warn("notifyTableChange中数据表格有{}，操作为{},table为{}", req.getTableNames(), req.getOperationCode(),tableNames); //TODO
        if (dataServerDual.getState() == DataServerStateEnum.PRIMARY && !req.getBase().getCaller().contains("SERVER")) {
            /*读取文件状态文件，如果是主机要调用副机的，如果是副机直接执行*/
            regionServerClient.notifyTableChange(new NotifyTableChangeRequest()
                    .setBase(new Base().setReceiver(dataServerDual.getHostName()).setCaller("SERVER"+UtilConstant.getHostname()))
                    .setTableNames(req.getTableNames())
                    .setTables(req.getTables())
                    .setOperationCode(req.getOperationCode()));
        }
        if (operationCode == RpcOperationEnum.DELETE.getCode()) {
            for (int i = 0; i < tableNames.size(); i++) {
                if(dataServerDual.getState() == DataServerStateEnum.PRIMARY) {
                    masterServerClient.notifyTableMetaChange(tableNames.get(i), RpcOperationEnum.DELETE.getCode(), vTableList.get(i).getMeta(), new Base()
                            .setCaller("SERVER"+UtilConstant.getHostname())
                            .setReceiver(MasterConstant.MASTER_HOST_NAME));
                }
                File file = new File(UtilConstant.getHostname() + tableNames.get(i) + ".dat");
                if (file.exists()) {
                    file.delete();
                }
                log.warn("notifyTableChange删除操作，删除的表为:{}",UtilConstant.getHostname() + tableNames.get(i));
            }
        } else if (operationCode == RpcOperationEnum.UPDATE.getCode()) {
            for (int i = 0; i < tableNames.size(); i++) {
                if(dataServerDual.getState() == DataServerStateEnum.PRIMARY) {
                    masterServerClient.notifyTableMetaChange(tableNames.get(i), RpcOperationEnum.UPDATE.getCode(), vTableList.get(i).getMeta(), new Base()
                            .setCaller("SERVER"+UtilConstant.getHostname())
                            .setReceiver(MasterConstant.MASTER_HOST_NAME));
                }
                File file = new File(UtilConstant.getHostname() + tableNames.get(i) + ".dat");
                if (file.exists()) {
                    file.delete();
                }
                //先删除再写入
                VTable vTableTmp = vTableList.get(i);
                Table tableTmp = CopyUtils.vTableToTable(vTableTmp);
                FileOutputStream out;
                log.warn("notifyTableChange更新操作，需要更新的表为:{}",tableTmp);
                try {
                    out = new FileOutputStream(file);
                    ObjectOutputStream objOut = new ObjectOutputStream(out);
                    objOut.writeObject(tableTmp);
                    objOut.flush();
                    objOut.close();
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                    return new NotifyTableChangeResponse()
                            .setBaseResp(RpcResult.failResp());
                }
            }
            /*更新文件*/
        } else if (operationCode == RpcOperationEnum.CREATE.getCode()) {
            for (int i = 0; i < vTableList.size(); i++) {
                if(dataServerDual.getState() == DataServerStateEnum.PRIMARY) {
                    masterServerClient.notifyTableMetaChange(tableNames.get(i), RpcOperationEnum.CREATE.getCode(), vTableList.get(i).getMeta(), new Base()
                            .setCaller("SERVER"+UtilConstant.getHostname())
                            .setReceiver(MasterConstant.MASTER_HOST_NAME));
                }
                VTable vTableTmp = vTableList.get(i);
                File file = new File(UtilConstant.getHostname() + vTableTmp.getMeta().getName() + ".dat");
                Table tableTmp = CopyUtils.vTableToTable(vTableTmp);
                log.warn("notifyTableChange创建操作，需要创建的表为:{}",tableTmp);
                FileOutputStream out;
                try {
                    out = new FileOutputStream(file);
                    ObjectOutputStream objOut = new ObjectOutputStream(out);
                    objOut.writeObject(tableTmp);
                    objOut.flush();
                    objOut.close();
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                    return new NotifyTableChangeResponse()
                            .setBaseResp(RpcResult.failResp());
                }
            }
        }
        return new NotifyTableChangeResponse()
                .setBaseResp(RpcResult.successResp());
    }

    @Override
    //更新副机器
    public ExecTableCopyResponse execTableCopy(ExecTableCopyRequest req) throws TException {
        DataServer dataServer = DataServer.builder().hostUrl(req.getTargetUrl()).hostName(req.getTargetName()).build();
        dataServer.parseHostUrl();
        String targetIp = dataServer.getIp();
        Integer targetPort = dataServer.getPort();
        log.warn("接收到{}的执行数据复制请求，对象为{}，要复制的数据是{}", req.getBase().getCaller(), req.getTargetName(), req.getTableNames());
        RegionServerClient regionServerClient = new RegionServerClient(RegionService.Client.class, targetIp, targetPort);
        FileServer fileServer = FileServer.builder().fileList(req.getTableNames()).build();
        NotifyTableChangeRequest notifyTableChangeRequest = new NotifyTableChangeRequest()
                .setTableNames(req.getTableNames())
                .setOperationCode(RpcOperationEnum.CREATE.getCode())
                .setTables(fileServer.readFile(dataServer.getState(),dataServer.getHostName(),dataServer.getHostUrl()))
                .setBase(new Base()
                        .setCaller("SERVER"+UtilConstant.getHostname())
                        .setReceiver(req.getTargetName()));
        //遍历一下
        log.warn("向{}传递数据变更请求，数据表格有{}，操作为{},table为{}", req.getTargetName(), req.getTableNames(), RpcOperationEnum.CREATE.getCode(),fileServer.readFile(dataServer.getState(),dataServer.getHostName(),dataServer.getHostUrl())); //TODO
        regionServerClient.notifyTableChange(notifyTableChangeRequest);
        return new ExecTableCopyResponse()
                .setBaseResp(RpcResult.successResp());
    }
}
