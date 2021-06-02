package com.siriusdb.model.db;

import lombok.Data;

@Data
public class TableAttribute {
    private Table table;
    private Attribute attribute;
}
