package com.siriusdb.enums;

/**
 * @Description: RPC请求结果状态枚举
 * @author: liuxuanming
 * @date: 2021/05/15 1:02 下午
 */
public enum RpcResultCodeEnum {

    /**
     * 成功请求
     */
    SUCCESS(0, "success"),

    /**
     * 参数校验失败，或者参数传递错误，或者服务处于异常状态
     */
    FAIL(-1, "fail"),

    /**
     * 表格、数据没有找到
     */
    NOT_FOUND(1, "not found"),

    /**
     * 表格、数据已经存在
     */
    HAS_EXISTED(2, "has existed"),

    ;

    /**
     * 错误编码，唯一
     */
    private final Integer code;

    /**
     * 错误描述
     */
    private final String desc;

    RpcResultCodeEnum(int code, String desc){
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
