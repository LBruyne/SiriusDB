package com.siriusdb.exception;

import com.siriusdb.enums.ErrorCodeEnum;

/**
 * @Description: 项目通用基本异常，继承RuntimeException，其他异常可以继承这个异常
 * @author: liuxuanming
 * @date: 2021/05/09 10:05 上午
 */
public class BasicBusinessException extends RuntimeException {

    //业务异常编码 @see ErrorCodeEnum
    private int code;

    //业务异常信息
    private String message;

    public BasicBusinessException() {
        super();
    }

    public BasicBusinessException(String message) {
        super(message);
        this.code = ErrorCodeEnum.FAIL.getCode();
        this.message = message;
    }

    public BasicBusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public static BasicBusinessException fail(String message) {
        return new BasicBusinessException(message);
    }

    public static BasicBusinessException failOnBizValidation(String message) {
        return new BasicBusinessException(ErrorCodeEnum.BUSINESS_VALIDATION_FAILED.getCode(), message);
    }

    public static BasicBusinessException failOnBasicValidation(String message) {
        return new BasicBusinessException(ErrorCodeEnum.BASIC_VALIDATION_FAILED.getCode(), message);
    }

    public static BasicBusinessException fail(ErrorCodeEnum errorCode) {
        return new BasicBusinessException(errorCode.getCode(),errorCode.getDesc());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
