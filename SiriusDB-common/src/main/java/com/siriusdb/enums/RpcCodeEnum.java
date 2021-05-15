package com.siriusdb.enums;

/**
 * @Description: RPC请求结果状态枚举
 * @author: liuxuanming
 * @date: 2021/05/15 1:02 下午
 */
public enum RpcCodeEnum {

    SUCCESS(0, "success"),

    FAIL(-1, "fail"),

    NOT_FOUND(1, "not found")

    ;

    /**
     * 错误编码，唯一
     */
    private final Integer code;

    /**
     * 错误描述
     */
    private final String desc;

    RpcCodeEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
