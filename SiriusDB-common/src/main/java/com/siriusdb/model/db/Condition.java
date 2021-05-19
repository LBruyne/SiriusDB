package com.siriusdb.model.db;
import com.siriusdb.enums.PredicateEnum;
import lombok.Data;

@Data
public class Condition<T extends Comparable> {
//    private Table formerTable, latterTable;
    private Element<T> formerDataAttribute, latterDataAttribute;
    private PredicateEnum condition;
    //getter setter ctor...

    public Element<T> judge(){
        return formerDataAttribute;
    }
}
