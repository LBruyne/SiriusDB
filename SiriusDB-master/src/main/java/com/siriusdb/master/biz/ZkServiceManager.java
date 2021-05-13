package com.siriusdb.master.biz;

import com.siriusdb.common.ZkConstant;
import com.siriusdb.master.biz.zk.ZkServiceMonitor;
import com.siriusdb.utils.zk.CuratorClientHolder;
import com.siriusdb.utils.zk.ZkUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description: Master服务器的ZooKeeper管理
 * @author: liuxuanming
 * @date: 2021/05/10 10:05 下午
 */
@Slf4j
public class ZkServiceManager implements Runnable {

    private List<String> servers;

    @Override
    public void run() {
        this.startMonitor();
    }

    /**
     * 启动应用时，开始监听ZooKeeper对应目录，获取服务器信息。
     */
    public void startMonitor() {
        try {
            // 开启一个连接
            CuratorClientHolder curatorClientHolder = new CuratorClientHolder(ZkConstant.ZOOKEEPER_HOST);

            log.warn("服务器目录下有子节点：" + curatorClientHolder.getChildren(ZkUtil.getRegisterHolderPath()));

            // 开始监听服务器目录，如果有节点的变化，则处理相应事件
            curatorClientHolder.monitorChildrenNodes(ZkUtil.getRegisterHolderPath(), new ZkServiceMonitor(curatorClientHolder));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
