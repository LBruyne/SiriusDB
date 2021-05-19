package com.siriusdb.master.rpc.server;

import com.siriusdb.common.UtilConstant;
import com.siriusdb.thrift.model.Base;
import com.siriusdb.thrift.model.BaseResp;
import com.siriusdb.thrift.model.QueryTableMetaInfoRequest;
import com.siriusdb.thrift.model.QueryTableMetaInfoResponse;
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

        // 对请求头进行验证
        // 这里简单的对hostname进行验证
        // TODO: 添加更多验证，如caller是否在Client列表中等
        String hostName = base.getHostName();
        if(!hostName.equals(UtilConstant.getHostname())) {
            log.warn("请求对象和本机不符合");
            return new QueryTableMetaInfoResponse()
                    .setMeta(null)
                    .setBaseResp(RpcResult.notFoundResp());
        }

        return MasterServiceServer.queryTableMeta(tables);
    }
}
