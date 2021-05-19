package com.siriusdb.enums;

/**
 * @Description: Master对机器执行策略的种类枚举
 * @author: liuxuanming
 * @date: 2021/05/18 6:02 下午
 */
public enum StrategyTypeEnum {

    DISCOVER(1),

    RECOVER(2),

    INVALID(3),

    ;

    /**
     * 策略类型编码
     */
    private final Integer code;

    StrategyTypeEnum(int code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
