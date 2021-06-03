package com.siriusdb.region.rpc;

import com.siriusdb.thrift.model.*;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.thrift.service.RegionService;
import com.siriusdb.utils.rpc.DynamicThriftClient;
import com.siriusdb.utils.rpc.RpcResult;
import org.apache.thrift.TException;

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
        return client.notifyTableMetaChange(notifyTableMetaChangeRequest);
    }
}
