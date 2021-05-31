package com.siriusdb.model.db;

import com.siriusdb.enums.PredicateEnum;

public class AttrVSAttrCondition implements ICondition{
    private TableAttribute formerAttribute,latterAttribute;
    private PredicateEnum operator;
}
