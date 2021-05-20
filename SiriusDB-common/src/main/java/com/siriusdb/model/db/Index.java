package com.siriusdb.model.db;

import lombok.Data;

/**
 * @Description: 索引数据结构
 * @author: ylx
 * @date: 2021/05/20 2：49 下午
 */
@Data
public class Index {
    
    private String indexName;
    
    private String tableName;

    private String attributeName;

    public Index(String indexName, String tableName, String attributeName) {
        this.setIndexName(indexName);
        this.setTableName(tableName);
        this.setAttributeName(attributeName);
    }
}