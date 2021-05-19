package com.siriusdb.master.rpc.client;

import com.siriusdb.thrift.model.QueryTableMetaInfoRequest;
import com.siriusdb.thrift.model.QueryTableMetaInfoResponse;
import com.siriusdb.thrift.service.RegionService;
import com.siriusdb.utils.rpc.DynamicThriftClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

/**
 * @Description: MasterServer的RPC客户端
 * @author: liuxuanming
 * @date: 2021/05/16 11:22 上午
 */
@Slf4j
public class RegionServiceClient extends DynamicThriftClient<RegionService.Client> {

    public RegionServiceClient(Class<RegionService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

    public RegionServiceClient(Class<RegionService.Client> ts) {
        super(ts);
    }

    public void getTableMetaInfo(QueryTableMetaInfoRequest request) {
        try {
            QueryTableMetaInfoResponse response = client.queryTableMetaInfo(request);
            // TODO 处理获取到的数据
        } catch (TException e) {
            log.warn(e.getMessage(), e);
        }
    }
}
