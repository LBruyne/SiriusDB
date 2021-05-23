package com.siriusdb.enums;

/**
 * @Description: RPC调用操作类型枚举
 * @author: liuxuanming
 * @date: 2021/05/23 11:28 下午
 */
public enum RpcOperationEnum {

    CREATE(0),

    UPDATE(1),

    DELETE(2),

    ;

    /**
     * 操作编码，唯一
     */
    private final Integer code;

    RpcOperationEnum(int code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
