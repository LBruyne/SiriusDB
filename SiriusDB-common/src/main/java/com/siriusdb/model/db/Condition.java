package com.siriusdb.model.db;
import com.siriusdb.enums.PredicateManagerEnum;
import lombok.Data;

@Data
public class Condition {
    private String formerTableName, latterTableName;
    private Attribute formAttribute, latterAttribute;
    private PredicateManagerEnum condition;
    //getter setter ctor...
}
