package com.siriusdb.model.master;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.enums.DataServerStateEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 数据服务器基本数据结构
 * @author: liuxuanming
 * @date: 2021/05/17 7:54 下午
 */
@Data
@Builder
public class DataServer implements Serializable {

    private Integer id;

    private String hostName;

    private String hostUrl;

    private DataServerStateEnum state;

    private Integer dualServerId;

    public void serverRecover() {
        setState(DataServerStateEnum.IDLE);
        setDualServerId(MasterConstant.NO_DUAL_SERVER);
    }

    public void serverInvalid() {
        setState(DataServerStateEnum.INVAILID);
        setDualServerId(MasterConstant.NO_DUAL_SERVER);
    }

}
