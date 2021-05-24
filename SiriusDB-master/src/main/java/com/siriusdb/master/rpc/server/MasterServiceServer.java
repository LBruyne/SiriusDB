package com.siriusdb.master.rpc.server;

import com.siriusdb.common.UtilConstant;
import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.enums.RpcOperationEnum;
import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.master.biz.zk.ServiceStrategyExecutor;
import com.siriusdb.model.db.TableMeta;
import com.siriusdb.model.master.DataServer;
import com.siriusdb.thrift.model.*;
import com.siriusdb.utils.copy.CopyUtils;
import com.siriusdb.utils.rpc.DynamicThriftServer;
import com.siriusdb.utils.rpc.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TTransportException;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: MasterServer的服务端
 * @author: liuxuanming
 * @date: 2021/05/16 11:24 上午
 */
@Slf4j
public class MasterServiceServer extends DynamicThriftServer {

    public MasterServiceServer(TProcessor processor, Integer port) throws TTransportException {
        super(processor, port);
    }

    public MasterServiceServer(TProcessor processor) throws TTransportException {
        super(processor);
    }

    /**
     * 根据请求的表格名返回相应的TableMeta数据
     *
     * @param names
     * @return
     */
    public static QueryTableMetaInfoResponse queryTableMeta(List<String> names) {
        QueryTableMetaInfoResponse response = null;

        // 这里是真正的业务逻辑
        if (ServiceStrategyExecutor.DataHolder.isServiceAbnormalState()) {
            log.warn("服务处于非正常状态");
            response = new QueryTableMetaInfoResponse()
                    .setMeta(null)
                    .setBaseResp(RpcResult.failResp());
        } else if (names == null || names.size() == 0) {
            // 对参数进行验证
            log.warn("请求参数有误");
            response = new QueryTableMetaInfoResponse()
                    .setMeta(null)
                    .setBaseResp(RpcResult.failResp());
        } else if (names.size() == 1 && names.get(0).equals(UtilConstant.ALL_TABLE)) {
            // 请求所有表格的元数据
            log.warn("请求所有表格的元数据");
            List<VTableMeta> result = ServiceStrategyExecutor.DataHolder.tableMetaList
                    .stream()
                    .map(table -> CopyUtils.tableMToVTableM(table)).collect(Collectors.toList());
            response = new QueryTableMetaInfoResponse()
                    .setMeta(result)
                    .setBaseResp(RpcResult.successResp());
        } else {
            // 请求特定表格的数据
            log.warn("请求表格{}的元数据", names);
            List<VTableMeta> result = new LinkedList<>();
            for (String name : names) {
                // 对每个表格名称进行查询
                TableMeta table = ServiceStrategyExecutor.DataHolder.findTable(name);
                if (table == null) {
                    // 发现不存在的表格名，返回错误信息
                    log.warn("查询表格{}失败，该表格不存在", name);
                    return new QueryTableMetaInfoResponse()
                            .setMeta(null)
                            .setBaseResp(RpcResult.failResp());
                } else {
                    // 该表格存在
                    log.warn("查询表格{}成功", name);
                    result.add(CopyUtils.tableMToVTableM(table));
                }
            }
            response = new QueryTableMetaInfoResponse()
                    .setMeta(result)
                    .setBaseResp(RpcResult.successResp());
        }
        return response;
    }

    /**
     * 接收RegionServer的告知，然后让TableMeta进行相应的变化
     *
     * @param tableName
     * @param vTableMeta
     * @param operationCode
     * @return
     */
    public static NotifyTableMetaChangeResponse notifyTableMetaChange(String tableName, VTableMeta vTableMeta, Integer operationCode) {
        if (ServiceStrategyExecutor.DataHolder.isServiceAbnormalState()) {
            log.warn("服务处于非正常状态");
            return new NotifyTableMetaChangeResponse()
                    .setBaseResp(RpcResult.failResp());
        } else if (operationCode.equals(RpcOperationEnum.CREATE.getCode())) {
            // 如果该表格已经存在
            if (ServiceStrategyExecutor.DataHolder.isTableExisted(tableName)) {
                return new NotifyTableMetaChangeResponse().setBaseResp(RpcResult.hasExistedResp());
            }
            // 否则添加数据
            TableMeta tableMeta = CopyUtils.vTableMToTableM(vTableMeta);
            ServiceStrategyExecutor.DataHolder.addTableData(tableMeta);
            log.warn("创建表格{}数据成功", tableName);
        } else if (operationCode.equals(RpcOperationEnum.DELETE.getCode())) {
            // 如果该表格不存在
            if (!ServiceStrategyExecutor.DataHolder.isTableExisted(tableName)) {
                return new NotifyTableMetaChangeResponse().setBaseResp(RpcResult.notFoundResp());
            }
            // 否则删除数据
            ServiceStrategyExecutor.DataHolder.removeTableMeta(tableName);
            log.warn("删除表格{}数据成功", tableName);
        } else if (operationCode.equals(RpcOperationEnum.UPDATE.getCode())) {
            // 如果该表格不存在
            if (!ServiceStrategyExecutor.DataHolder.isTableExisted(tableName)) {
                return new NotifyTableMetaChangeResponse().setBaseResp(RpcResult.notFoundResp());
            }
            TableMeta tableMeta = CopyUtils.vTableMToTableM(vTableMeta);
            ServiceStrategyExecutor.DataHolder.updateTableMeta(tableMeta);
            log.warn("更新表格{}数据成功", tableName);
        } else {
            throw new BasicBusinessException(ErrorCodeEnum.BUSINESS_VALIDATION_FAILED.getCode(), "未定义的操作码");
        }
        return new NotifyTableMetaChangeResponse().setBaseResp(RpcResult.successResp());
    }

    /**
     * 判断该表格是否存在，如果不存在，根据负载均衡选择一台服务器存储表格
     *
     * @param name
     * @return
     */
    public static QueryCreateTableResponse queryCreateTable(String name) {
        if (ServiceStrategyExecutor.DataHolder.isServiceAbnormalState()) {
            log.warn("服务处于非正常状态");
            return new QueryCreateTableResponse()
                    .setLocatedServerName(null)
                    .setLocatedServerUrl(null)
                    .setBaseResp(RpcResult.failResp());
        } else {
            if (ServiceStrategyExecutor.DataHolder.isTableExisted(name)) {
                log.warn("该表格已经存在");
                return new QueryCreateTableResponse()
                        .setLocatedServerName(null)
                        .setLocatedServerUrl(null)
                        .setBaseResp(RpcResult.hasExistedResp());
            } else {
                Integer serverId = ServiceStrategyExecutor.DataHolder.allocateLocatedServer();
                DataServer server = ServiceStrategyExecutor.DataHolder.getDataServerById(serverId);
                log.warn("选择服务器{}作为该表格存储的位置", server.getHostName());
                return new QueryCreateTableResponse()
                        .setLocatedServerUrl(server.getHostUrl())
                        .setLocatedServerName(server.getHostName())
                        .setBaseResp(RpcResult.successResp());
            }
        }
    }
}
