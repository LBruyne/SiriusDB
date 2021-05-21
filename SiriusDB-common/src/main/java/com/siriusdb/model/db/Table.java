package com.siriusdb.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Table {

    private String name;

    private String primaryKey;

    private Integer attributeNum;

    private Integer indexNum;

    private List<Attribute> attributes;

    private List<Index> indexes;

    public Table(String name, String primaryKey, List<Attribute> attributes, List<Index> indexes) {
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