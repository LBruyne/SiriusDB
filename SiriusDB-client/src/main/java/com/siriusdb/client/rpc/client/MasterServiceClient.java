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

    public QueryCreateTableResponse createTable(Table newTable, String caller) throws TException {
        QueryCreateTableRequest req = new QueryCreateTableRequest()
                .setName(newTable.getMeta().getName())
                .setBase(new Base()
                        .setCaller(caller)
                        .setReceiver(MasterConstant.MASTER_HOST_NAME));
        QueryCreateTableResponse res = client.queryCreateTable(req);

        if (res.getBaseResp().getCode() == RpcResultCodeEnum.HAS_EXISTED.getCode()) {
            log.warn("向{}请求创建表格已存在", caller);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求创建表格已存在");
        }
        else if (res.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("向{}创建表格请求成功", caller);
            return res;
        }
        else {
            log.warn("向{}创建表格请求失败", caller);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "创建表格请求失败");
        }
    }

    public QueryTableMetaInfoResponse dropTable(Table oldTable, String caller) throws TException {
        List<String> list = new ArrayList();
        list.add(oldTable.getMeta().getName());
        QueryTableMetaInfoRequest req = new QueryTableMetaInfoRequest()
                .setName(list)
                .setBase(new Base()
                        .setCaller(caller)
                        .setReceiver(MasterConstant.MASTER_HOST_NAME));
        QueryTableMetaInfoResponse res = client.queryTableMeta(req);

        if (res.getBaseResp().getCode() == RpcResultCodeEnum.NOT_FOUND.getCode()) {
            log.warn("向{}请求删除的表格不存在", caller);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求删除的表格不存在");
        }
        else if (res.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("向{}删除表格请求成功", caller);
            return res;
        }
        else {
            log.warn("向{}删除表格请求失败", caller);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "删除表格请求失败");
        }
    }
}