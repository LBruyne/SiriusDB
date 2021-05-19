package com.siriusdb.model.db;

import lombok.Data;

/**
 * @Description: 单个数据元素
 * @author: liuxuanming
 * @date: 2021/05/19 4:10 下午
 */
@Data
public class Element<T> {

    private Integer columnId;

    private String type;

    private T data;
}
