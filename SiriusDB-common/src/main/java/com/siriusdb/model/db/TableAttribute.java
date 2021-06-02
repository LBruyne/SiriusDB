package com.siriusdb.model.db;

import lombok.Data;
import java.util.Set;
@Data
public class TableAttribute {
    private Set<Table> table;
    private Attribute attribute;
    public TableAttribute(Set<Table> tab, Attribute attr){
        this.table = tab;
        this.attribute = attr;
    }
}
