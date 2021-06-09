package com.siriusdb.model.db;
import lombok.Data;

import java.util.List;

@Data
public class RecordManagerRow {
    private List<TableAttribute> attr;
    private Row data;
    RecordManagerRow(List<TableAttribute> ls, Row data){
        this.attr = ls;
        this.data = data;
    }

}
