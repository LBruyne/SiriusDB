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
            boolean x1=tosearch.getTable().containsAll(attr.get(i).getTable());
            boolean x2=tosearch.getAttribute().getType().equals(attr.get(i).getAttribute().getType());
            boolean x3=tosearch.getAttribute().getName().equals(attr.get(i).getAttribute().getName());
//            System.out.println(tosearch.getAttribute().getName());
//            System.out.println("X1 = "+x1 +"\t X2 = "+x2+"\t X3 = "+x3);
            if(x1 && x2 && x3 ){
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
