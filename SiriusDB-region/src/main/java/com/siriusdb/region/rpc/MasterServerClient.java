package com.siriusdb.region.rpc;

import com.siriusdb.thrift.model.*;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.thrift.service.RegionService;
import com.siriusdb.utils.rpc.DynamicThriftClient;
import com.siriusdb.utils.rpc.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

@Slf4j
public class MasterServerClient extends DynamicThriftClient<MasterService.Client> {

    public MasterServerClient(Class<MasterService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

    public MasterServerClient(Class<MasterService.Client> ts) {
        super(ts);
    }

    public NotifyTableMetaChangeResponse notifyTableMetaChange(String tableName,int operationCode,VTableMeta vTableMeta,Base base) throws TException {
        NotifyTableMetaChangeRequest notifyTableMetaChangeRequest = new NotifyTableMetaChangeRequest()
                .setTableMeta(vTableMeta)
                .setTableName(tableName)
                .setOperationCode(operationCode)
                .setBase(base);
        log.warn("mster接收到了meta变更，变更后的locatedname为：{}",vTableMeta.getLocatedServerName());
        return client.notifyTableMetaChange(notifyTableMetaChangeRequest);
    }
}
