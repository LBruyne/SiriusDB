package com.siriusdb.master.rpc.server;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.thrift.model.*;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.utils.rpc.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.List;

/**
 * @Description: MasterServerService实现类，服务端实现其中的Iface接口，提供具体业务逻辑
 * @author: liuxuanming
 * @date: 2021/05/16 11:17 上午
 */
@Slf4j
public class MasterServiceImpl implements MasterService.Iface {

    @Override
    public QueryTableMetaInfoResponse queryTableMeta(QueryTableMetaInfoRequest req) throws TException {
        // 获取请求中的数据
        Base base = req.getBase();
        List<String> tables = req.getName();
        log.warn("接收到对于表格元数据查询请求头{}，请求体{}", base, tables);

        if(!verifyBase(base)) {
            log.warn("请求对象和本机不符合");
            return new QueryTableMetaInfoResponse()
                    .setMeta(null)
                    .setBaseResp(RpcResult.failResp());
        }

        return MasterServiceServer.queryTableMeta(tables);
    }

    @Override
    public NotifyTableMetaChangeResponse notifyTableMetaChange(NotifyTableMetaChangeRequest req) throws TException {
        // 获取请求中的数据
        Base base = req.getBase();
        String tableName = req.getTableName();
        VTableMeta vTableMeta = req.getTableMeta();
        Integer operationCode = req.getOperationCode();
        log.warn("接收到对于表格元数据变更通知的请求头{}，目标表格{}，操作码{}", base, tableName, operationCode);

        if(!verifyBase(base)) {
            log.warn("请求对象和本机不符合");
            return new NotifyTableMetaChangeResponse()
                    .setBaseResp(RpcResult.failResp());
        }

        return MasterServiceServer.notifyTableMetaChange(tableName, vTableMeta, operationCode);
    }

    @Override
    public QueryCreateTableResponse queryCreateTable(QueryCreateTableRequest req) throws TException {
        // 获取请求中的数据
        Base base = req.getBase();
        String name = req.getName();
        log.warn("接收到对于表格新建请求的请求头{}，目标表格名{}", base, name);

        if(!verifyBase(base)) {
            log.warn("请求对象和本机不符合");
            return new QueryCreateTableResponse()
                    .setBaseResp(RpcResult.failResp());
        }

        return MasterServiceServer.queryCreateTable(name);
    }

    private Boolean verifyBase(Base base) {
        // 对请求头进行验证
        // 这里简单的对hostname进行验证
        // TODO: 添加更多验证，如caller是否在Client列表中等
        String receiver = base.getReceiver();
        if(!receiver.equals(MasterConstant.MASTER_HOST_NAME)) {
            return false;
        }
        return true;
    }
}
