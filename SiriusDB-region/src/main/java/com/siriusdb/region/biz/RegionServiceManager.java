package com.siriusdb.region.biz;

import com.siriusdb.region.rpc.RegionServiceImpl;
import com.siriusdb.region.rpc.RegionServiceServer;
import com.siriusdb.thrift.service.RegionService;
import com.siriusdb.thrift.service.MasterService;
import com.siriusdb.thrift.service.UserService;

public class RegionServiceManager {

    public void startService() {
        try {
            RegionServiceServer regionServiceServer = new RegionServiceServer(
                    new RegionService.Processor<>(new RegionServiceImpl()));
            regionServiceServer.startServer();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
