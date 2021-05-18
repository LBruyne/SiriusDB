package com.siriusdb.model.db;

import com.siriusdb.model.db.Attribute;
import com.siriusdb.model.db.Index;
import com.siriusdb.model.db.Row;
import lombok.Data;

import java.util.List;

@Data
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