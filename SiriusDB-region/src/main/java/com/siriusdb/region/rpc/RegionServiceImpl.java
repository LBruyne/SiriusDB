package com.siriusdb.region.rpc;


import com.siriusdb.common.UtilConstant;
import com.siriusdb.enums.RpcOperationEnum;
import com.siriusdb.model.db.Table;
import com.siriusdb.model.master.DataServer;
import com.siriusdb.thrift.model.*;
import com.siriusdb.thrift.service.RegionService;
import com.siriusdb.utils.copy.CopyUtils;
import com.siriusdb.utils.rpc.RpcResult;
import org.apache.thrift.TException;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class RegionServiceImpl implements RegionService.Iface {
    //返回一系列特定的表格数据
    @Override
    public QueryTableDataResponse queryTableData(QueryTableDataRequest req) throws TException {
        //声明一个临时对象用来存储读取出来的数据
        List<VTable> vtables = null;
        //获取表名称
        List<String> tableName= req.getTableNames();
        List<String> tableName1 = new ArrayList<String>();
        if(tableName.get(0) == "ALL_TABLE"){
            File file = new File(this.getClass().getResource("").getPath());
            File[] tempList = file.listFiles();
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile()) {
                    tableName1.add(tempList[i].toString());
                }
                if (tempList[i].isDirectory()) {
                }
            }
        }
        else{
            tableName1.addAll(tableName);
        }
        //建立循环
        for(int i=0;i<tableName1.size();i++){
            //先读取到
            Table tableTmp = null;
            VTable vtableTmp = null;
            File file = new File(tableName1.get(i) + ".dat");
            FileInputStream in;
            try {
                in = new FileInputStream(file);
                ObjectInputStream objIn=new ObjectInputStream(in);
                tableTmp=(Table) objIn.readObject();
                objIn.close();
            }
            catch (Exception e){
                return new QueryTableDataResponse().setBaseResp(RpcResult.failResp());
            }
            //将table的值赋值给vtable
            vtableTmp = CopyUtils.tableToVTable(tableTmp);
            //将新的添加到末尾
            vtables.add(vtableTmp);
        }
        return new QueryTableDataResponse()
                .setBaseResp(RpcResult.successResp())
                .setTables(vtables);
    }

    //Master告知某服务器状态变化,存储该变化
    @Override
    public NotifyStateResponse notifyStateChange(NotifyStateRequest req) throws TException {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("dualmachine.txt"));
            String stateCode = Integer.toString(req.getStateCode());
            String dualServerUrl = req.getDualServerUrl();
            String dulServerName = req.getDualServerName();
            /*要先判断是否是否存在那个文件，如果存在就要更新，否则就直接写入*/
            bw.write(stateCode);
            bw.newLine();
            bw.write(dulServerName);
            bw.newLine();
            bw.write(dualServerUrl);
            bw.newLine();
            bw.close();
        }
        catch (Exception e){
            return new NotifyStateResponse().setBaseResp(RpcResult.failResp());
        }
        
        return new NotifyStateResponse()
                .setBaseResp(RpcResult.successResp());
    }

    //要给副机也复制一份
    @Override
    public NotifyTableChangeResponse notifyTableChange(NotifyTableChangeRequest req) throws TException {
        int operationCode = req.getOperationCode();
        List<String> tableNames = req.getTableNames();
        List<VTable> vTableList = req.getTables();
        File stateFile = new File("dualmachine.txt");
        String stateCode = "";
        String dualServerName = "";
        String dualServerUrl = "";
        try{
            FileReader fr = new FileReader(stateFile);
            BufferedReader br = new BufferedReader(fr);
            stateCode = br.readLine();
            dualServerName = br.readLine();
            dualServerUrl = br.readLine();
            fr.close();
            br.close();
        }
        catch (Exception e){
            return new NotifyTableChangeResponse()
                    .setBaseResp(RpcResult.failResp());
        }
        DataServer dataServer = DataServer.builder().hostUrl(dualServerUrl).hostName(dualServerName).build();
        String dualIp = dataServer.getIp();
        Integer dualPort = dataServer.getPort();
        RegionServerClient regionServerClient = new RegionServerClient(RegionService.Client.class,dualIp,dualPort);

        if(stateCode == "0") {
            /*读取文件状态文件，如果是主机要调用副机的，如果是副机直接执行*/
            regionServerClient.notifyTableChange(req);
        }
            if (operationCode == RpcOperationEnum.DELETE.getCode()) {
                for (int i = 0; i < tableNames.size(); i++) {
                    File file = new File(tableNames.get(i) + ".dat");
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } else if (operationCode == RpcOperationEnum.UPDATE.getCode()) {
                for (int i = 0; i < tableNames.size(); i++) {
                    File file = new File(tableNames.get(i) + ".dat");
                    if (file.exists()) {
                        file.delete();
                    }
                    //先删除再写入
                    VTable vTableTmp = vTableList.get(i);
                    Table tableTmp = CopyUtils.vTableToTable(vTableTmp);
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(file);
                        ObjectOutputStream objOut = new ObjectOutputStream(out);
                        objOut.writeObject(tableTmp);
                        objOut.flush();
                        objOut.close();
                    }
                    catch (Exception e){
                        return new NotifyTableChangeResponse()
                                .setBaseResp(RpcResult.failResp());
                    }
                }
                /*更新文件*/
            } else if (operationCode == RpcOperationEnum.CREATE.getCode()) {
                for (int i = 0; i < vTableList.size(); i++) {
                    VTable vTableTmp = vTableList.get(i);
                    File file = new File(vTableTmp.getMeta().getName() + ".dat");
                    Table tableTmp = CopyUtils.vTableToTable(vTableTmp);
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(file);
                        ObjectOutputStream objOut = new ObjectOutputStream(out);
                        objOut.writeObject(tableTmp);
                        objOut.flush();
                        objOut.close();
                    }
                    catch (Exception e){
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
        String targetIp = dataServer.getIp();
        Integer targetPort = dataServer.getPort();
        RegionServerClient regionServerClient = new RegionServerClient(RegionService.Client.class,targetIp,targetPort);

        NotifyTableChangeRequest notifyTableChangeRequest = new NotifyTableChangeRequest()
                .setTableNames(req.getTableNames())
                .setOperationCode(1)
                .setTables(queryTableData(new QueryTableDataRequest()
                        .setTableNames(req.getTableNames())
                        .setBase(new Base()
                                .setCaller(UtilConstant.getHostname())//怎么得到自己的ip
                                .setReceiver(targetIp))).getTables())
                .setBase(new Base()
                        .setCaller(UtilConstant.getHostname())//怎么得到自己的ip
                        .setReceiver(targetIp));

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
