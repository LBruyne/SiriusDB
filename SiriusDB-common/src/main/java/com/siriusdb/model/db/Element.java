package com.siriusdb.model.db;

import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.exception.BasicBusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 单个数据元素
 * @author: liuxuanming
 * @date: 2021/05/19 4:10 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Element<T> implements Serializable, Comparable<Element<T>> {

    private T data;

    private Integer columnId;

    private String type;

    @Override
    public int compareTo(Element<T> anotherElement) {
        if (data instanceof java.lang.Integer) {
            return ((Integer) data).compareTo((Integer) anotherElement.data);
        } else if (data instanceof java.lang.Float) {
            return ((Float) data).compareTo((Float) anotherElement.data);
        } else if (data instanceof java.lang.String) {
            return ((String) data).compareTo((String) anotherElement.data);
        } else {
            throw new BasicBusinessException(ErrorCodeEnum.BASIC_VALIDATION_FAILED.getCode(), "未定义的数据类型");
        }
    }
}
