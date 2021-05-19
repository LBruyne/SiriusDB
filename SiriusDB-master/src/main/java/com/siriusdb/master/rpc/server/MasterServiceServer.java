package com.siriusdb.master.rpc.server;

import com.siriusdb.common.UtilConstant;
import com.siriusdb.master.biz.zk.ServiceStrategyExecutor;
import com.siriusdb.thrift.model.QueryTableMetaInfoResponse;
import com.siriusdb.thrift.model.VTableMeta;
import com.siriusdb.utils.rpc.DynamicThriftServer;
import com.siriusdb.utils.rpc.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TTransportException;

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

    public static QueryTableMetaInfoResponse queryTableMeta(List<String> tables) {
        QueryTableMetaInfoResponse response = null;
        // 这里是真正的业务逻辑
        // 对参数进行验证
        if (tables == null || tables.size() == 0) {
            log.warn("请求参数有误");
            response = new QueryTableMetaInfoResponse()
                    .setMeta(null)
                    .setBaseResp(RpcResult.failResp());
        } else if(tables.size() == 1 && tables.get(0) == UtilConstant.ALL_TABLE){
            log.warn("请求所有表格的元数据");
            List<VTableMeta> result = ServiceStrategyExecutor.DataHolder.tableMetaList.stream().map(table -> {
                VTableMeta item = new VTableMeta();

                return item;
            }).collect(Collectors.toList());
            response = new QueryTableMetaInfoResponse()
                    .setMeta(result)
                    .setBaseResp(RpcResult.successResp());
        } else {
            
        }
        return response;
    }

}
