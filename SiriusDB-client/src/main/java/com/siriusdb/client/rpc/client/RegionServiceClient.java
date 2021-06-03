package com.siriusdb.client.rpc.client;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.enums.RpcOperationEnum;
import com.siriusdb.enums.RpcResultCodeEnum;
import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.model.db.Table;
import com.siriusdb.thrift.model.*;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.thrift.service.RegionService;
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

    public RegionServiceClient(Class<RegionService.Client> ts) { super(ts); }

    public RegionServiceClient(Class<RegionService.Client> ts, String ip, Integer port) { super(ts, ip, port); }

    public void trueCreateTable(Table newTable, String receiver) throws TException {
        List<String> list1 = new ArrayList<>();
        list1.add(newTable.getMeta().getName());
        List<VTable> list2 = new ArrayList<VTable>();
        VTable vtable = new VTable();
        BeanUtils.copyProperties(newTable, vtable);
        list2.add(vtable);
        NotifyTableChangeRequest req = new NotifyTableChangeRequest()
                .setTables(list2)
                .setTableNames(list1)
                .setOperationCode(RpcOperationEnum.CREATE.getCode())
                .setBase(new Base()
                        .setCaller(MasterConstant.MASTER_HOST_NAME)
                        .setReceiver(receiver));
        NotifyTableChangeResponse res = client.notifyTableChange(req);

        if (res.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("创建表格成功", receiver);
        }
        else {
            log.warn("创建表格失败", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "创建表格失败");
        }
    }

    public void trueDropTable(Table newTable, String receiver) throws TException {
        List<String> list1 = new ArrayList<>();
        list1.add(newTable.getMeta().getName());
        List<VTable> list2 = new ArrayList<VTable>();
        VTable vtable = new VTable();
        BeanUtils.copyProperties(newTable, vtable);
        list2.add(vtable);
        NotifyTableChangeRequest req = new NotifyTableChangeRequest()
                .setTables(list2)
                .setTableNames(list1)
                .setOperationCode(RpcOperationEnum.DELETE.getCode())
                .setBase(new Base()
                        .setCaller(MasterConstant.MASTER_HOST_NAME)
                        .setReceiver(receiver));
        NotifyTableChangeResponse res = client.notifyTableChange(req);

        if (res.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("删除表格成功", receiver);
        }
        else {
            log.warn("删除表格失败", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "删除表格失败");
        }
    }
}
