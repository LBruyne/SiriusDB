package com.siriusdb.enums;

/**
 * @Description: 错误码枚举
 * @author: liuxuanming
 * @date: 2021/05/09 10:06 上午
 */
public enum ErrorCodeEnum {

    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    /**
     * 失败
     */
    FAIL(-1, "失败"),

    /**
     * 基础信息校验失败
     */
    BASIC_VALIDATION_FAILED(1001, "基础信息校验失败"),

    /**
     * 业务校验失败
     */
    BUSINESS_VALIDATION_FAILED(2001,"业务校验失败")

    ;

    /**
     * 错误编码，唯一
     */
    private final Integer code;

    /**
     * 错误描述
     */
    private final String desc;

    ErrorCodeEnum(int code, String desc){
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
