package com.siriusdb.model.db;

import com.siriusdb.enums.PredicateEnum;
import lombok.Data;

@Data
public class AttrVSAttrCondition implements ICondition{
    private TableAttribute formerAttribute,latterAttribute;
    private PredicateEnum operator;
}
