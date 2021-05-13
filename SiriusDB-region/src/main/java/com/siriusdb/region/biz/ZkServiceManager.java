package com.siriusdb.region.biz;

import com.siriusdb.common.UtilConstant;
import com.siriusdb.utils.zk.CuratorClientHolder;
import com.siriusdb.utils.zk.ZkUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;

/**
 * @Description: ZooKeeper 服务管理逻辑
 * @author: liuxuanming
 * @date: 2021/05/10 3:25 下午
 */
@Slf4j
public class ZkServiceManager implements Runnable {

    @Override
    public void run() {
        this.serviceRegister();
    }

    private void serviceRegister() {
        try {
            // 向ZooKeeper注册临时节点
            CuratorClientHolder curatorClientHolder = new CuratorClientHolder();
            curatorClientHolder.createNode(ZkUtil.getRegisterPath(), UtilConstant.HOST_URL, CreateMode.EPHEMERAL);

            // 阻塞该线程，直到发生异常或者主动退出
            synchronized (this) {
                wait();
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
