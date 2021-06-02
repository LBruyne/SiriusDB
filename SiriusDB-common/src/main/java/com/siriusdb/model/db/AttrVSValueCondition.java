package com.siriusdb.model.db;

import com.siriusdb.enums.PredicateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttrVSValueCondition<T extends Comparable<T>> implements ICondition {
    //    private Table formerTable, latterTable;

    private TableAttribute formerAttribute;

    //where id = 123

    private Element<T> formerDataElement, latterDataElement;

    private Table formerTable, latterTable;

    private PredicateEnum condition;
    // = / != /
    //getter setter ctor...

    public int judge() {
        return formerDataElement.getData().compareTo(latterDataElement.getData());
    }
}
