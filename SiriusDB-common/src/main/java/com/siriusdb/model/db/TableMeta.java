package com.siriusdb.model.db;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 表格元数据数据结构
 * @author: liuxuanming
 * @date: 2021/05/17 8:11 下午
 */
@Data
public class TableMeta implements Serializable {

    private String name;

    private String primaryKey;

    private String locatedServerName;

    private List<Attribute> attributes;
}
