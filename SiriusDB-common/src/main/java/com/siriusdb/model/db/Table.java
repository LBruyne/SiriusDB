package com.siriusdb.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Vector;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Table {

    private String name;

    private String primaryKey;

    /**
     * attributes的数量直接调attributes.size()获取，不要同时维护两个变量，容易出现遗漏
     */
    private List<Attribute> attributes;

    /**
     * index的数量直接调indexes.size()获取，不要同时维护两个变量，容易出现遗漏
     */
    private List<Index> indexes;

    /**
     * 表格下的数据
     */
    private List<Row> data;

    public Table(String name, String primaryKey, List<Attribute> attributes, List<Index> indexes) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.attributes = attributes;
        this.indexes = indexes;
    }

    public int checkIfAttributesExist(Element toBeChecked) {
        //if exist return colIndex
        //else return -1
        return 1;
    }
}