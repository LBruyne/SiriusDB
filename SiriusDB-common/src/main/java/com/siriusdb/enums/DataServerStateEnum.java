package com.siriusdb.enums;

/**
 * @Description: 数据服务器状态枚举
 * @author: liuxuanming
 * @date: 2021/05/17 7:55 下午
 */
public enum DataServerStateEnum {

    PRIMARY(1, "主件机"),

    COPY(2, "副本机"),

    IDLE(0, "空闲中"),

    INVAILID(-1, "失效中"),

    ;

    /**
     * 数据类型编码
     */
    private final Integer code;

    /**
     * 数据类型名称
     */
    private final String type;

    DataServerStateEnum(int code, String type){
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
