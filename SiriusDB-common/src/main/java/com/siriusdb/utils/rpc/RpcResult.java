package com.siriusdb.utils.rpc;

import com.siriusdb.enums.RpcCodeEnum;
import com.siriusdb.thrift.model.BaseResp;

import java.io.Serializable;

/**
 * @Description: 一些通用函数，用于返回RPC结果
 * @author: liuxuanming
 * @date: 2021/05/19 3:07 下午
 */
public class RpcResult {

    public static BaseResp failResp() {
        return new BaseResp()
                .setCode(RpcCodeEnum.FAIL.getCode())
                .setDesc(RpcCodeEnum.FAIL.getDesc());
    }

    public static BaseResp successResp() {
        return new BaseResp()
                .setCode(RpcCodeEnum.SUCCESS.getCode())
                .setDesc(RpcCodeEnum.SUCCESS.getDesc());
    }

    public static BaseResp notFoundResp() {
        return new BaseResp()
                .setCode(RpcCodeEnum.NOT_FOUND.getCode())
                .setDesc(RpcCodeEnum.NOT_FOUND.getDesc());
    }

}
