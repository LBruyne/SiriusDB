package com.siriusdb.region.biz;

import com.siriusdb.region.rpc.RegionServiceImpl;
import com.siriusdb.region.rpc.RegionServiceServer;
import com.siriusdb.thrift.service.RegionService;


public class RegionServiceManager {

    public void startService() {
        try {
            RegionServiceServer regionServiceServer = new RegionServiceServer(
                    new RegionService.Processor<>(new RegionServiceImpl()));
            regionServiceServer.startServer();
        } catch (Exception e) {}
    }
}
