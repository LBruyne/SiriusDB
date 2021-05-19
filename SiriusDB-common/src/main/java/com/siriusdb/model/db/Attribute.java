package com.siriusdb.model.db;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 列属性元数据
 * @author: liuxuanming
 * @date: 2021/05/18 4:18 下午
 */
@Data
public class Attribute implements Serializable {

    private Integer id;

    private String name;

    private String type;
}
