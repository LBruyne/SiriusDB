package com.siriusdb.region.rpc;

import com.siriusdb.common.RegionConstant;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

public class RegionServiceImpl implements RegionService.Iface {
    //返回一系列特定的表格数据
    @Override
    public QueryTableDataResponse queryTableData(QueryTableDataRequest req) throws TException {
        //声明一个临时对象用来存储读取出来的数据
        List<VTable> vtables = null;
        //获取表名称
        List<String> tableName= req.getTableName();
        //建立循环
        for(int i=0;i<tableName.size();i++){
            //先读取到
            Table tableTmp = null;
            VTable vtableTmp = null;
            File file = new File(tableName.get(i) + ".dat");
            FileInputStream in;
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            tableTmp=objIn.readObject();
            objIn.close();
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
        BufferedWriter bw = new BufferedWriter(new FileWriter("dualmachine.txt"));
        String stateCode = Integer.toString(req.getStateCode());
        String dualServerUrl = req.getDualServerUrl();
        String dulServerName = req.getDualServerName();

        bw.write(stateCode);
        bw.newLine();
        bw.write(dulServerName);
        bw.newLine();
        bw.write(dualServerUrl);
        bw.newLine();
        bw.close();

        return null;
    }

    @Override
    public NotifyTableChangeResponse notifyTableChange(NotifyTableChangeRequest req) throws TException {
        int operationCode = req.getOperationCode();
        List<String> tableNames = req.getTableNames();
        List<VTable> vTableList = req.getTables();
        if( operationCode == 2){
            for(int i = 0;i < tableNames.size();i++){
                File file = new File(tableNames.get(i) + ".dat");
                if(file.exists()) {
                    file.delete();
                }
            }
        }
        else if(operationCode == 1){
            for(int i = 0 ; i < tableNames.size() ; i++){
                File file = new File(tableNames.get(i) + ".dat");
                if(file.exists()) {
                    file.delete();
                }
                //先删除再写入
                VTable vTableTmp = vTableList.get(i);
                Table tableTmp = CopyUtils.vTableToTable(vTableTmp);
                FileOutputStream out;
                out = new FileOutputStream(file);
                ObjectOutputStream objOut = new ObjectOutputStream(out);
                objOut.writeObject(tableTmp);
                objOut.flush();
                objOut.close();
            }
            /*更新文件*/
        }
        else if(operationCode == 0){
            for(int i = 0;i < vTableList.size(); i++){
                VTable vTableTmp = vTableList.get(i);
                File file = new File(vTableTmp.getMeta().getName() + ".dat");
                Table tableTmp = CopyUtils.vTableToTable(vTableTmp);
                FileOutputStream out;
                out = new FileOutputStream(file);
                ObjectOutputStream objOut = new ObjectOutputStream(out);
                objOut.writeObject(tableTmp);
                objOut.flush();
                objOut.close();
            }
        }
        return null;
    }

    @Override
    public ExecTableCopyResponse execTableCopy(ExecTableCopyRequest req) throws TException {
        TTransport transport = null;
        DataServer dataServer = new DataServer.builder().hostUrl(req.getTargetUrl()).hostName(req.getTargetName())build();
        String targetIp = dataServer.getIp();
        Integer targetPort = dataServer.getPort();
        transport = new TSocket(targetIp, targetPort, 30000);
        TProtocol protocol = new TBinaryProtocol(transport);
        QueryService.Client client = new QueryService.Client(protocol);
        transport.open();
        QueryTableDataRequest queryTableDataRequest = new QueryTableDataRequest().setTables(req.getTableNames()).setBaseResp(RpcResult.successResp());
        QueryTableDataResponse queryTableDataResponse = client.queryTableData(queryTableDataRequest);
        /*存文件*/
        List<VTable> vTableList = queryTableDataResponse.getTables();
        for(int i = 0;i < vTableList.size(); i++){
            Table tableTmp = CopyUtils.vTableToTable(vTableList.get(i));
            File file = new File(tableTmp.getMeta().getName() + ".dat");
            FileOutputStream out;
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(tableTmp);
            objOut.flush();
            objOut.close();
        }
        return null;
    }
}
