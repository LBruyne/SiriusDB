package com.siriusdb.model;

import java.util.List;

public class Table {
    private String name;
    private List<Attribute> attributes;
    private List<Index> indexesOwned;
    private List<Row> realData;
    //...
    public int checkIfAttributesExist(Attribute toBeChecked) {
        //if exist return colIndex
        //else return -1
        return 1;
    }
}