package com.siriusdb.model.db;

import lombok.Data;

import java.util.List;

@Data
public class Table {

    private String name;

    private List<Attribute> attributes;

    private List<Index> indexs;

    private List<Row> data;
    //...
    public int checkIfAttributesExist(Attribute toBeChecked) {
        //if exist return colIndex
        //else return -1
        return 1;
    }
}