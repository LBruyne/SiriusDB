package com.siriusdb.model.db;
import com.siriusdb.enums.PredicateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Condition<T extends Comparable> {
//    private Table formerTable, latterTable;
    private Element<T> formerDataAttribute, latterDataAttribute;

    private PredicateEnum condition;
    //getter setter ctor...

    public Element<T> judge(){
        return formerDataAttribute;
    }
}
