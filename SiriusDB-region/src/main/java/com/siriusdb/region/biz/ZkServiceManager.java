package com.siriusdb.region.biz;

import com.siriusdb.region.zk.HostInfoRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: ZooKeeper 服务管理逻辑
 * @author: liuxuanming
 * @date: 2021/05/10 3:25 下午
 */
public class ZkServiceManager {

    public static final Logger logger = LoggerFactory.getLogger(ZkServiceManager.class);

    public void serviceRegister() {
        try {
            new HostInfoRegister().run();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
