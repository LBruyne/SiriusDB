package com.siriusdb.model.db;

import lombok.Data;

@Data
public class Attribute<T> {

    private String name;

    private int colIndex;

    private T value;

}
