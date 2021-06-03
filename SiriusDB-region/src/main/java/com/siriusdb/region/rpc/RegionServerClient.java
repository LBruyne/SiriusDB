package com.siriusdb.region.rpc;

import com.siriusdb.thrift.model.NotifyTableChangeRequest;
import com.siriusdb.thrift.model.NotifyTableChangeResponse;
import com.siriusdb.thrift.model.QueryTableDataRequest;
import com.siriusdb.thrift.model.QueryTableDataResponse;
import com.siriusdb.thrift.service.RegionService;
import com.siriusdb.utils.rpc.DynamicThriftClient;
import org.apache.thrift.TException;

public class RegionServerClient extends DynamicThriftClient<RegionService.Client> {

    public RegionServerClient(Class<RegionService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

    public RegionServerClient(Class<RegionService.Client> ts) {
        super(ts);
    }

    public void notifyTableChange(NotifyTableChangeRequest notifyTableChangeRequest) throws TException{
        NotifyTableChangeResponse notifyTableChangeResponse = client.notifyTableChange(notifyTableChangeRequest);
    }
}
