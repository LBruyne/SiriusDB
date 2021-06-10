package com.siriusdb.master.biz.zk;

import com.siriusdb.common.ZkConstant;
import com.siriusdb.utils.zk.CuratorClientHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;

/**
 * @Description: ZooKeeper的节点监视器，实现Path和Node两种Cache的监视器接口，将发生的事件进行处理，
 * @author: liuxuanming
 * @date: 2021/05/13 12:46 下午
 */
@Slf4j
public class ZkServiceMonitor implements PathChildrenCacheListener {

    private ServiceStrategyManager serviceStrategyManager;

    private CuratorClientHolder client;

    public ZkServiceMonitor(CuratorClientHolder curatorClientHolder) {
        this.serviceStrategyManager = new ServiceStrategyManager();
        this.client = curatorClientHolder;
    }

    @Override
    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
        String eventPath = pathChildrenCacheEvent.getData() != null ? pathChildrenCacheEvent.getData().getPath() : null;

        // 接收到事件，对事件类型进行判断并执行相应策略
        switch (pathChildrenCacheEvent.getType()) {
            case CHILD_ADDED:
                log.warn("服务器目录新增节点: " + pathChildrenCacheEvent.getData().getPath());
                serviceStrategyManager.eventServerAppear(
                        eventPath.replaceFirst(ZkConstant.ZNODE + "/", ""),
                        client.getData(eventPath));
                break;
            case CHILD_REMOVED:
                log.warn("服务器目录删除节点: " + pathChildrenCacheEvent.getData().getPath());
                serviceStrategyManager.eventServerDisappear(
                        eventPath.replaceFirst(ZkConstant.ZNODE + "/", ""));
                break;
            case CHILD_UPDATED:
                log.warn("服务器目录更新节点: " + pathChildrenCacheEvent.getData().getPath());
                serviceStrategyManager.eventServerUpdate(
                        eventPath.replaceFirst(ZkConstant.ZNODE + "/", ""),
                        client.getData(eventPath));
                break;
            default:
        }
    }


}
