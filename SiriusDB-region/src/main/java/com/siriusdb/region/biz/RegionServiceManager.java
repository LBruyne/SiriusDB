package com.siriusdb.region.biz;

import com.siriusdb.common.RegionConstant;
import com.siriusdb.region.rpc.RegionServiceImpl;
import com.siriusdb.region.rpc.RegionServiceServer;
import com.siriusdb.thrift.service.RegionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegionServiceManager {

    public void startService() {
        try {
            RegionServiceServer regionServiceServer = new RegionServiceServer(
                    new RegionService.Processor<>(new RegionServiceImpl()), RegionConstant.port);
            regionServiceServer.startServer();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
