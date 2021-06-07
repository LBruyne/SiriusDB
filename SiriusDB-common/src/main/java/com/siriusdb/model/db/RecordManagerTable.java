package com.siriusdb.model.db;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class RecordManagerTable {
    private Table table;
    private List<TableAttribute> attr;
    private List<Row> data;


//  given a TableAttribute object
//  need to find the index of the attribute in this.attr
    public int fetchColID(TableAttribute tosearch) {
        int ret = -1;
        for (int i = 0; i < attr.size(); i++) {
            if(tosearch.getTable().containsAll(attr.get(i).getTable()) && tosearch.getAttribute().getType().equals(attr.get(i).getAttribute().getType()) && tosearch.getAttribute().getName().equals(attr.get(i).getAttribute().getName())){
                ret = i;
            }
        }
        return ret;
    }

    public void addData(Row record) {
        if (record != null)
            data.add(record);
    }

}
