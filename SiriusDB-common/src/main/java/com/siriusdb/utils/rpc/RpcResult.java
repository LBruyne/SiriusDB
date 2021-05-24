package com.siriusdb.utils.rpc;

import com.siriusdb.enums.RpcResultCodeEnum;
import com.siriusdb.thrift.model.BaseResp;

/**
 * @Description: 一些通用函数，用于返回RPC结果
 * @author: liuxuanming
 * @date: 2021/05/19 3:07 下午
 */
public class RpcResult {

    public static BaseResp failResp() {
        return new BaseResp()
                .setCode(RpcResultCodeEnum.FAIL.getCode())
                .setDesc(RpcResultCodeEnum.FAIL.getDesc());
    }

    public static BaseResp successResp() {
        return new BaseResp()
                .setCode(RpcResultCodeEnum.SUCCESS.getCode())
                .setDesc(RpcResultCodeEnum.SUCCESS.getDesc());
    }

    public static BaseResp notFoundResp() {
        return new BaseResp()
                .setCode(RpcResultCodeEnum.NOT_FOUND.getCode())
                .setDesc(RpcResultCodeEnum.NOT_FOUND.getDesc());
    }

    public static BaseResp hasExistedResp() {
        return new BaseResp()
                .setCode(RpcResultCodeEnum.HAS_EXISTED.getCode())
                .setDesc(RpcResultCodeEnum.HAS_EXISTED.getDesc());
    }


}
