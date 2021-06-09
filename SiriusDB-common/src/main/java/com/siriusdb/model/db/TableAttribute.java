package com.siriusdb.model.db;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class TableAttribute {
    private Set<Table> table;
    private Attribute attribute;
    public TableAttribute(Set<Table> tab, Attribute attr){
        this.table = tab;
        this.attribute = attr;
    }

    @Override
    public boolean equals(Object another){
        if(! (another instanceof  TableAttribute))
            return false;
        if(another == this)
            return true;
        return table.containsAll(((TableAttribute) another).getTable()) && attribute.equals(((TableAttribute) another).getAttribute());
    }
}
