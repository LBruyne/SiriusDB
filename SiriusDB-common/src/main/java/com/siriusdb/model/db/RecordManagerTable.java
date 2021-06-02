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

    public int fetchColID(TableAttribute tosearch) {
        int ret = -1;
        for (int i = 0; i < attr.size(); i++) {
            if(tosearch.getTable().containsAll(attr.get(i).getTable())){
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
