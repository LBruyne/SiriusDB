package com.siriusdb.client.rpc.client;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.enums.RpcOperationEnum;
import com.siriusdb.enums.RpcResultCodeEnum;
import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.model.db.Table;
import com.siriusdb.thrift.model.*;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.thrift.service.RegionService;
import com.siriusdb.utils.copy.CopyUtils;
import com.siriusdb.utils.rpc.DynamicThriftClient;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: ylx
 * @Date: 2021/06/03/10:10
 * @Description: Thrift Interfaces for client to Region
 */
@Slf4j
public class RegionServiceClient extends DynamicThriftClient<RegionService.Client> {

    public RegionServiceClient(Class<RegionService.Client> ts) {
        super(ts);
    }

    public RegionServiceClient(Class<RegionService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

    public void trueCreateTable(Table newTable, String receiver) throws TException {
        List<String> list1 = new ArrayList<>();
        list1.add(newTable.getMeta().getName());
        List<VTable> list2 = new ArrayList<>();
        VTable vtable = CopyUtils.tableToVTable(newTable);
        list2.add(vtable);
        NotifyTableChangeRequest req = new NotifyTableChangeRequest()
                .setTables(list2)
                .setTableNames(list1)
                .setOperationCode(RpcOperationEnum.CREATE.getCode())
                .setBase(new Base()
                        .setCaller(UtilConstant.HOST_NAME)
                        .setReceiver(receiver));
        log.warn("将向服务器{}传递创建表格数据，创建表格{}，操作码{}", receiver, list1, RpcOperationEnum.CREATE.getCode());

        NotifyTableChangeResponse res = client.notifyTableChange(req);

        if (res.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("向{}创建表格成功", receiver);
        } else {
            log.warn("向{}创建表格失败", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "创建表格失败");
        }
    }

    public void trueDropTable(Table oldTable, String receiver) throws TException {
        List<String> list1 = new ArrayList<>();
        list1.add(oldTable.getMeta().getName());
        List<VTable> list2 = new ArrayList<>();
        VTable vtable = CopyUtils.tableToVTable(oldTable);
        list2.add(vtable);
        NotifyTableChangeRequest req = new NotifyTableChangeRequest()
                .setTables(list2)
                .setTableNames(list1)
                .setOperationCode(RpcOperationEnum.DELETE.getCode())
                .setBase(new Base()
                        .setCaller(UtilConstant.HOST_NAME)
                        .setReceiver(receiver));
        NotifyTableChangeResponse res = client.notifyTableChange(req);

        if (res.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("向{}删除表格成功", receiver);
        } else {
            log.warn("向{}删除表格失败", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "删除表格失败");
        }
    }

    public Table trueGetTable(String tableName, String receiver) throws TException {
        List<String> list1 = new ArrayList<>();
        list1.add(tableName);
//        List<VTable> list2 = new ArrayList<VTable>();
//        VTable vtable = CopyUtils.tableToVTable(newTable);
//        list2.add(vtable);
        QueryTableDataRequest req = new QueryTableDataRequest()
                .setTableNames(list1)
                .setBase(new Base()
                        .setCaller(UtilConstant.HOST_NAME)
                        .setReceiver(receiver));
        log.warn("将向服务器{}传递GET表格请求，GET表格{}", receiver, list1);

        QueryTableDataResponse res = client.queryTableData(req);

        Table ret;

        if (res.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("GET表格{}成功", tableName);
            if (res.getTables() != null) {
                if (res.getTables().size() != 0) {
                    ret = CopyUtils.vTableToTable(res.getTables().get(0));
                } else {
                    log.warn("Region Server: {} 回传的表格列表没有元素!", receiver);
                    throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "GET表格失败");
                }
            } else {
                log.warn("Region Server: {} 回传的表格列表为Null!", receiver);
                throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "GET表格失败");
            }
        } else {
            log.warn("GET表格{}失败", tableName);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "GET表格失败");
        }
        return ret;
    }

    public void trueRetransmitTable(Table table, String receiver) throws TException {
        List<String> list1 = new ArrayList<>();
        list1.add(table.getMeta().getName());
        List<VTable> list2 = new ArrayList<>();
        VTable vtable = CopyUtils.tableToVTable(table);
        list2.add(vtable);
        NotifyTableChangeRequest req = new NotifyTableChangeRequest()
                .setTables(list2)
                .setTableNames(list1)
                .setOperationCode(RpcOperationEnum.UPDATE.getCode())
                .setBase(new Base()
                        .setCaller(UtilConstant.HOST_NAME)
                        .setReceiver(receiver));
        log.warn("Alter table: 将向服务器{}传递更新的表格数据，更新表格{}，操作码{}", receiver, list1, RpcOperationEnum.UPDATE.getCode());

        NotifyTableChangeResponse res = client.notifyTableChange(req);

        if (res.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("向{}更新表格成功", receiver);
        } else {
            log.warn("向{}更新表格失败", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "更新表格失败");
        }
    }

}
