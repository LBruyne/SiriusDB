package com.siriusdb.model.master;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.enums.DataServerStateEnum;
import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.exception.BasicBusinessException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Description: 数据服务器基本数据结构
 * @author: liuxuanming
 * @date: 2021/05/17 7:54 下午
 */
@Data
@Builder
@Slf4j
public class DataServer implements Serializable {

    private Integer id;

    private String ip;

    private Integer port;

    private String hostName;

    private String hostUrl;

    private DataServerStateEnum state;

    private Integer dualServerId;

    private static final String HOST_URL_REGEX = "(\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+)";

    public void parseHostUrl() {
        if (hostUrl == null || hostUrl.equals("")) {
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "URL不存在或不合法");
        } else if (!hostUrl.matches(HOST_URL_REGEX)) {
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "URL不存在或不合法");
        } else {
            setIp(hostUrl.split(":")[0]);
            setPort(Integer.parseInt(hostUrl.split(":")[1]));
        }
    }

    /**
     * 设置机器从失效状态中恢复
     */
    public void serverRecover() {
        setState(DataServerStateEnum.IDLE);
        setDualServerId(MasterConstant.NO_DUAL_SERVER);
        log.warn("服务器{}恢复", getHostName());
    }

    /**
     * 设置机器为失效状态
     */
    public void serverInvalid() {
        setState(DataServerStateEnum.INVAILID);
        setDualServerId(MasterConstant.NO_DUAL_SERVER);
        log.warn("服务器{}失效", getHostName());
    }

    /**
     * 设置机器为备用状态
     */
    public void serverIdle() {
        setState(DataServerStateEnum.IDLE);
        setDualServerId(MasterConstant.NO_DUAL_SERVER);
        log.warn("服务器{}空闲", getHostName());
    }

    /**
     * 设置和一台已经结对过的机器进行结对
     *
     * @param server
     */
    public void remakePair(DataServer server) {
        if (server.getState() == DataServerStateEnum.IDLE ||
                server.getState() == DataServerStateEnum.INVAILID) {
            throw new BasicBusinessException(ErrorCodeEnum.BUSINESS_VALIDATION_FAILED.getCode(), "不能和失效或者空闲状态的机器结对");
        } else if (server.getDualServerId() != MasterConstant.NO_DUAL_SERVER) {
            throw new BasicBusinessException(ErrorCodeEnum.BUSINESS_VALIDATION_FAILED.getCode(), "不能和还处于结对状态的机器结对");
        } else {
            if (server.getState() == DataServerStateEnum.PRIMARY) {
                // 如果没有结对的机器是主件机，则让这台机器成为副本机
                this.setState(DataServerStateEnum.COPY);
            } else if (server.getState() == DataServerStateEnum.COPY) {
                // 如果没有结对的机器是副本机，则让这台机器成为主件机
                this.setState(DataServerStateEnum.PRIMARY);
            }
            // 设置dualServerId
            this.setDualServerId(server.getId());
            server.setDualServerId(this.getId());
            log.warn("{}的配偶是{}", this.getHostName(), server.getHostName());
        }
    }

    /**
     * 设置和一台闲置机器结对
     *
     * @param server
     */
    public void makePair(DataServer server) {
        if (server.getState() != DataServerStateEnum.IDLE || this.getState() != DataServerStateEnum.IDLE) {
            throw new BasicBusinessException(ErrorCodeEnum.BUSINESS_VALIDATION_FAILED.getCode(), "不能和不为空闲状态的机器结对");
        } else {
            this.setState(DataServerStateEnum.PRIMARY);
            server.setState(DataServerStateEnum.COPY);
            this.setDualServerId(server.getDualServerId());
            server.setDualServerId(this.getDualServerId());
            log.warn("{}的配偶是{}", this.getHostName(), server.getHostName());
        }
    }

}
