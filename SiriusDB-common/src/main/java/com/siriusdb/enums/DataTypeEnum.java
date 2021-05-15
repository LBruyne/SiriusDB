package com.siriusdb.enums;

/**
 * @Description: 数据库中数据类型枚举
 * @author: liuxuanming
 * @date: 2021/05/15 1:12 下午
 */
public enum DataTypeEnum {

    INTEGER(1, "Integer"),

    FLOAT(2, "Float"),

    STRING(3, "String"),

    ;

    /**
     * 数据类型编码
     */
    private final Integer code;

    /**
     * 数据类型名称
     */
    private final String type;

    DataTypeEnum(int code, String type){
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
