package com.siriusdb.region.rpc;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.enums.DataServerStateEnum;
import com.siriusdb.enums.RpcOperationEnum;
import com.siriusdb.model.db.Table;
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
            File file = new File(this.getClass().getResource("").getPath());
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
        String stateCode = Integer.toString(req.getStateCode());
        switch (stateCode){
            case "1":dataServer.setState(DataServerStateEnum.PRIMARY);
            case "2":dataServer.setState(DataServerStateEnum.COPY);
            case "0":dataServer.setState(DataServerStateEnum.IDLE);
            case "-1":dataServer.setState(DataServerStateEnum.INVAILID);
        }
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
        return new NotifyStateResponse()
                .setBaseResp(RpcResult.successResp());
        /*try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(UtilConstant.getHostname() + "-dualmachine.txt"));
            String stateCode = Integer.toString(req.getStateCode());
            String dualServerUrl = req.getDualServerUrl();
            String dualServerName = req.getDualServerName();
            /*要先判断是否是否存在那个文件，如果存在就要更新，否则就直接写入*/
            /*bw.write(stateCode);
            bw.newLine();
            bw.write(dualServerName);
            bw.newLine();
            bw.write(dualServerUrl);
            bw.newLine();
            bw.close();
            log.warn("接收到master的提示，本机{}状态切换为{}, 对偶机为{}:{}", UtilConstant.getHostname(), stateCode, dualServerName, dualServerUrl);
        } catch (Exception e) {
            log.warn("Region状态变更失败");
            return new NotifyStateResponse().setBaseResp(RpcResult.failResp());
        }

        return new NotifyStateResponse()
                .setBaseResp(RpcResult.successResp());*/
    }

    //要给副机也复制一份
    @Override
    public NotifyTableChangeResponse notifyTableChange(NotifyTableChangeRequest req) throws TException {
        int operationCode = req.getOperationCode();
        List<String> tableNames = req.getTableNames();
        List<VTable> vTableList = req.getTables();
        /*File stateFile = new File(UtilConstant.getHostname() + "-dualmachine.txt");
        String stateCode = "";
        String dualServerName = "";
        String dualServerUrl = "";
        try {
            FileReader fr = new FileReader(stateFile);
            BufferedReader br = new BufferedReader(fr);
            stateCode = br.readLine();
            dualServerName = br.readLine();
            dualServerUrl = br.readLine();
            fr.close();
            br.close();
        } catch (Exception e) {
            log.warn("Notifytablechange读取文件失败");
            return new NotifyTableChangeResponse()
                    .setBaseResp(RpcResult.failResp());
        }*/
        File fileState = new File(UtilConstant.getHostname() + "dualmachine.dat");
        FileInputStream in;
        DataServer dataServerDual = null;
        try {
            in = new FileInputStream(fileState);
            ObjectInputStream objIn = new ObjectInputStream(in);
            dataServerDual = (DataServer) objIn.readObject();
            objIn.close();
        } catch (Exception e) {
            log.warn("没有副机文件");
        }
        DataServer dataServer = DataServer.builder().hostUrl(dataServerDual.getHostUrl()).hostName(dataServerDual.getHostName()).build();
        String dualIp = dataServer.getIp();
        Integer dualPort = dataServer.getPort();
        RegionServerClient regionServerClient = new RegionServerClient(RegionService.Client.class, dualIp, dualPort);
        MasterServerClient masterServerClient = new MasterServerClient(MasterService.Client.class, MasterConstant.MASTER_SERVER_IP, MasterConstant.MASTER_SERVER_PORT);
        if (dataServerDual.getState() == DataServerStateEnum.PRIMARY) {
            /*读取文件状态文件，如果是主机要调用副机的，如果是副机直接执行*/
            regionServerClient.notifyTableChange(req);
        }
        if (operationCode == RpcOperationEnum.DELETE.getCode()) {
            for (int i = 0; i < tableNames.size(); i++) {
                if(dataServerDual.getState() == DataServerStateEnum.PRIMARY) {
                    masterServerClient.notifyTableMetaChange(tableNames.get(i), RpcOperationEnum.DELETE.getCode(), vTableList.get(i).getMeta(), new Base()
                            .setCaller(UtilConstant.getHostname())
                            .setReceiver(MasterConstant.MASTER_HOST_NAME));
                }
                File file = new File(UtilConstant.getHostname() + tableNames.get(i) + ".dat");
                if (file.exists()) {
                    file.delete();
                }
            }
        } else if (operationCode == RpcOperationEnum.UPDATE.getCode()) {
            for (int i = 0; i < tableNames.size(); i++) {
                if(dataServerDual.getState() == DataServerStateEnum.PRIMARY) {
                    masterServerClient.notifyTableMetaChange(tableNames.get(i), RpcOperationEnum.UPDATE.getCode(), vTableList.get(i).getMeta(), new Base()
                            .setCaller(UtilConstant.getHostname())
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
                log.warn("notify:table:{},vtable:{}",tableTmp,vTableTmp);
                try {
                    out = new FileOutputStream(file);
                    ObjectOutputStream objOut = new ObjectOutputStream(out);
                    objOut.writeObject(tableTmp);
                    objOut.flush();
                    objOut.close();
                } catch (Exception e) {
                    return new NotifyTableChangeResponse()
                            .setBaseResp(RpcResult.failResp());
                }
            }
            /*更新文件*/
        } else if (operationCode == RpcOperationEnum.CREATE.getCode()) {
            for (int i = 0; i < vTableList.size(); i++) {
                if(dataServerDual.getState() == DataServerStateEnum.PRIMARY) {
                    masterServerClient.notifyTableMetaChange(tableNames.get(i), RpcOperationEnum.CREATE.getCode(), vTableList.get(i).getMeta(), new Base()
                            .setCaller(UtilConstant.getHostname())
                            .setReceiver(MasterConstant.MASTER_HOST_NAME));
                }
                VTable vTableTmp = vTableList.get(i);
                File file = new File(UtilConstant.getHostname() + vTableTmp.getMeta().getName() + ".dat");
                Table tableTmp = CopyUtils.vTableToTable(vTableTmp);
                log.warn("notify:table:{},vtable:{}",tableTmp,vTableTmp);
                FileOutputStream out;
                try {
                    out = new FileOutputStream(file);
                    ObjectOutputStream objOut = new ObjectOutputStream(out);
                    objOut.writeObject(tableTmp);
                    objOut.flush();
                    objOut.close();
                } catch (Exception e) {
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
                .setTables(fileServer.readFile())
                .setBase(new Base()
                        .setCaller(UtilConstant.getHostname())
                        .setReceiver(req.getTargetName()));

        log.warn("向{}传递数据变更请求，数据表格有{}，操作为{}", req.getTargetName(), req.getTableNames(), RpcOperationEnum.CREATE.getCode()); //TODO
        regionServerClient.notifyTableChange(notifyTableChangeRequest);
        return new ExecTableCopyResponse()
                .setBaseResp(RpcResult.successResp());
        /*regionServerClient.notifyTableChange(notifyTableChangeRequest);
        QueryTableDataRequest queryTableDataRequest = new QueryTableDataRequest().setTables(req.getTableNames()).setBaseResp(RpcResult.successResp());
        RegionServerClient regionServerClient = new RegionServerClient(RegionService.Client.class,targetIp,targetPort);
        QueryTableDataResponse queryTableDataResponse = regionServerClient.execTableCopy(queryTableDataRequest);
        /*存文件*/
        /*List<VTable> vTableList = queryTableDataResponse.getTables();
        for(int i = 0;i < vTableList.size(); i++){
            Table tableTmp = CopyUtils.vTableToTable(vTableList.get(i));
            File file = new File(tableTmp.getMeta().getName() + ".dat");
            FileOutputStream out;
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(tableTmp);
            objOut.flush();
            objOut.close();
        }*/
    }
}
