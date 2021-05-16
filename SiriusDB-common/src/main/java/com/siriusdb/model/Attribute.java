package com.siriusdb.model;

import lombok.Data;

@Data
public class Attribute<T> {
    private String name;
    private int colIndex;
    private T value;
    //setter getter compiler will do it
}
