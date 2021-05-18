package com.siriusdb.master.biz.zk;

import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.enums.StrategyTypeEnum;
import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.model.master.DataServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 根据ZooKeeper监视器得到的各种事件实行相应的策略
 * @author: liuxuanming
 * @date: 2021/05/13 12:52 下午
 */
@Slf4j
public class ServiceStrategyManager {

    ServiceStrategyExecutor strategyExecutor;

    public ServiceStrategyManager() {
        strategyExecutor = new ServiceStrategyExecutor();
    }

    /**
     * 处理服务器节点出现事件
     *
     * @param hostName
     * @param hostUrl
     */
    public void eventServerAppear(String hostName, String hostUrl) {
        log.warn("新增服务器节点：主机名 {}, 地址 {}", hostName, hostUrl);
        DataServer thisServer;
        if (ServiceStrategyExecutor.DataHolder.dataServerInfo.get(hostName) != null) {
            // 该服务器已经存在，即从失效状态中恢复
            thisServer = ServiceStrategyExecutor.DataHolder.dataServerInfo.get(hostName);
            thisServer.serverRecover();
            log.warn("对该服务器{}执行恢复策略", hostName);
            strategyExecutor.exceStrategy(thisServer, StrategyTypeEnum.RECOVER);
        } else {
            // 新发现的服务器，新增一份数据
            ServiceStrategyExecutor.DataHolder.addServer(hostName, hostUrl);
            thisServer = ServiceStrategyExecutor.DataHolder.dataServerInfo.get(hostName);
            log.warn("对该服务器{}执行新增策略", hostName);
            strategyExecutor.exceStrategy(thisServer, StrategyTypeEnum.NEW_COME);
        }
    }

    /**
     * 处理服务器节点失效事件
     *
     * @param hostName
     */
    public void eventServerDisappear(String hostName) {
        log.warn("删除服务器节点：主机名 {}", hostName);
        DataServer thisServer;
        if (ServiceStrategyExecutor.DataHolder.dataServerInfo.get(hostName) == null) {
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "需要删除信息的服务器不存在于服务器列表中");
        } else {
            // 更新并处理下线的服务器
            thisServer = ServiceStrategyExecutor.DataHolder.dataServerInfo.get(hostName);
            strategyExecutor.exceStrategy(thisServer, StrategyTypeEnum.INVALID);
            thisServer.serverInvalid();
        }
    }

    /**
     * 处理服务器节点更新事件
     *
     * @param hostName
     * @param hostUrl
     */
    public void eventServerUpdate(String hostName, String hostUrl) {
        log.warn("更新服务器节点：主机名 {}, 地址 {}", hostName, hostUrl);
        DataServer thisServer;
        if (ServiceStrategyExecutor.DataHolder.dataServerInfo.get(hostName) == null) {
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "需要更新信息的服务器不存在于服务器列表中");
        } else {
            // 更新服务器的URL
            thisServer = ServiceStrategyExecutor.DataHolder.dataServerInfo.get(hostName);
            thisServer.setHostUrl(hostUrl);
        }
    }
}
