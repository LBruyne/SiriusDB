package com.siriusdb.master.rpc.client;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.enums.RpcResultCodeEnum;
import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.master.biz.zk.ServiceStrategyExecutor;
import com.siriusdb.model.master.DataServer;
import com.siriusdb.thrift.model.*;
import com.siriusdb.thrift.service.RegionService;
import com.siriusdb.utils.rpc.DynamicThriftClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.List;

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

    public void execTableCopy(List<String> tableNames, DataServer targetServer, String receiver) throws TException {
        if (tableNames == null || tableNames.size() == 0)
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求复制的表格名字列表为空");
        ExecTableCopyRequest request = new ExecTableCopyRequest()
                .setBase(new Base()
                        .setCaller(MasterConstant.MASTER_HOST_NAME)
                        .setReceiver(receiver))
                .setTargetName(targetServer.getHostName())
                .setTargetUrl(targetServer.getHostUrl())
                .setTableNames(tableNames);

        log.warn("请求服务器{}向服务器{}复制表格{}的数据", receiver, targetServer.getHostName(), tableNames);
        ExecTableCopyResponse response = client.execTableCopy(request);

        if (response == null || response.getBaseResp() == null) {
            log.warn("向{}请求表格复制结果为空", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求表格复制结果为空");
        } else if (response.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("向{}请求表格复制成功", receiver);
        } else {
            log.warn("向{}请求表格复制失败", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求表格复制失败");
        }
    }

    public void notifyStateChange(DataServer server, String receiver) throws TException {
        DataServer dualServer = ServiceStrategyExecutor.DataHolder.getDataServerById(server.getDualServerId());
        if (dualServer == null)
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "该服务器不存在对偶服务器");
        NotifyStateRequest request = new NotifyStateRequest()
                .setBase(new Base()
                        .setCaller(MasterConstant.MASTER_HOST_NAME)
                        .setReceiver(receiver))
                .setDualServerName(dualServer.getHostName())
                .setDualServerUrl(dualServer.getHostUrl())
                .setStateCode(server.getState().getCode());

        log.warn("通知服务器{}它的对偶服务器是{}:{}，它的状态是{}", server.getHostName(), dualServer.getHostName(),
                dualServer.getHostUrl(), server.getState().getCode());
        NotifyStateResponse response = client.notifyStateChange(request);

        if (response == null || response.getBaseResp() == null) {
            log.warn("向{}通知状态变化结果为空", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "通知状态变化结果为空");
        } else if (response.getBaseResp().getCode() == RpcResultCodeEnum.SUCCESS.getCode()) {
            log.warn("向{}通知状态变化成功", receiver);
        } else {
            log.warn("向{}通知状态变化失败", receiver);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "通知状态变化失败");
        }
    }
}
