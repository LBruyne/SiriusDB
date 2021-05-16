package com.siriusdb.master.rpc.server;

import com.siriusdb.thrift.model.QueryTableMetaInfoRequest;
import com.siriusdb.thrift.model.QueryTableMetaInfoResponse;
import com.siriusdb.thrift.service.MasterServerService;
import org.apache.thrift.TException;

/**
 * @Description: MasterServerService实现类，服务端实现其中的Iface接口，提供具体业务逻辑
 * @author: liuxuanming
 * @date: 2021/05/16 11:17 上午
 */
public class MasterServiceImpl implements MasterServerService.Iface {

    @Override
    public QueryTableMetaInfoResponse queryTableMeta(QueryTableMetaInfoRequest req) throws TException {
        return null;
    }
}
