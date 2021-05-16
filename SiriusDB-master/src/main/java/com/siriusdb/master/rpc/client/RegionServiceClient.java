package com.siriusdb.master.rpc.client;

import com.siriusdb.thrift.service.RegionServerService;
import com.siriusdb.utils.rpc.DynamicThriftClient;

/**
 * @Description: MasterServer的RPC客户端
 * @author: liuxuanming
 * @date: 2021/05/16 11:22 上午
 */
public class RegionServiceClient extends DynamicThriftClient<RegionServerService.Client> {

    public RegionServiceClient(Class<RegionServerService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

    public RegionServiceClient(Class<RegionServerService.Client> ts) {
        super(ts);
    }
}
