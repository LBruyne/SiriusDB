package com.siriusdb.model.db;

import lombok.Data;

import java.util.List;
import java.util.Vector;

@Data
public class Table {

    private String name;

    private String primaryKey;

    private Integer attributeNum;

    private Integer indexNum;

    private Vector<Attribute> attributes;

    private Vector<Index> indexes;

    public Table(String name, String primaryKey, Vector<Attribute> attributes, Vector<Index> indexes) {
        this.setName(name);
        this.setPrimaryKey(primaryKey);
        this.setAttributes(attributes);
        this.setIndexes(indexes);
        this.attributeNum = attributes.size();
        this.indexNum = indexes.size();
    }

    //private List<Row> data;

    public int checkIfAttributesExist(Element toBeChecked) {
        //if exist return colIndex
        //else return -1
        return 1;
    }
}