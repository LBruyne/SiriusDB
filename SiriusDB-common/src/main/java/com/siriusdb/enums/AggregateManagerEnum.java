package com.siriusdb.enums;

public enum AggregateManagerEnum {
    COUNT(1, "count"),

    MAX(2, "max"),

    MIN(3, "min"),

    SUM(4, "sum"),

    AVG(5, "avg"),

    ;
    private final Integer code;

    private final String desc;

    AggregateManagerEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() { return desc; }
}
