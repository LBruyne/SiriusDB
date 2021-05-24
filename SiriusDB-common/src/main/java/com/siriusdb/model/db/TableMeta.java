package com.siriusdb.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 表格元数据数据结构
 * @author: liuxuanming
 * @date: 2021/05/17 8:11 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMeta implements Serializable {

    private String name;

    private String primaryKey;

    private String locatedServerName;

    private String locatedServerUrl;

    private List<Attribute> attributes;
}
