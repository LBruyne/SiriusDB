package com.siriusdb.client.rpc.client;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.enums.RpcResultCodeEnum;
import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.model.db.Table;
import com.siriusdb.thrift.model.*;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.utils.rpc.DynamicThriftClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: ylx
 * @Date: 2021/06/03/0:23
 * @Description: Thrift Interfaces for client to Master
 */
@Slf4j
public class MasterServiceClient extends DynamicThriftClient<MasterService.Client> {

    public MasterServiceClient(Class<MasterService.Client> ts) { super(ts); }

    public MasterServiceClient(Class<MasterService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

    public boolean createTable(Table newTable, String receiver) throws TException {
        QueryCreateTableRequest req = new QueryCreateTableRequest()
                .setName(newTable.getMeta().getName())
                .setBase(new Base()
                        .setCaller(MasterConstant.MASTER_HOST_NAME)
                        .setReceiver(receiver));
        QueryCreateTableResponse res = client.queryCreateTable(req);

        if (res.getBaseResp().getCode() == RpcResultCodeEnum.HAS_EXISTED.getCode()) {
            log.warn("向{}请求创建表格已存在", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求创建表格已存在");
        }
        else if (res.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("向{}请求创建表格成功", receiver);
            return true;
        }
        else {
            log.warn("向{}请求创建表格失败", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求创建表格失败");
        }
    }

    public boolean dropTable(Table newTable, String receiver) throws TException {
        List<String> list = new ArrayList();
        list.add(newTable.getMeta().getName());
        QueryTableMetaInfoRequest req = new QueryTableMetaInfoRequest()
                .setName(list)
                .setBase(new Base()
                        .setCaller(MasterConstant.MASTER_HOST_NAME)
                        .setReceiver(receiver));
        QueryTableMetaInfoResponse res = client.queryTableMeta(req);

        if (res.getBaseResp().getCode() == RpcResultCodeEnum.NOT_FOUND.getCode()) {
            log.warn("向{}请求删除表格不存在", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求删除表格不存在");
        }
        else if (res.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("向{}请求删除表格成功", receiver);
            return true;
        }
        else {
            log.warn("向{}请求删除表格失败", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求删除表格失败");
        }
    }
}