package com.siriusdb.enums;

public enum PredicateEnum {
    LARGER(1, ">"),

    SMALLER(2, "<"),

    LARGERorEQUAL(3, ">="),

    SMALLERorEQUAL(4, "<="),

    EQUAL(5, "=="),

    notEQUAL(6, "!="),

    ;
    private final Integer code;

    private final String desc;

    PredicateEnum(int code, String desc){
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
