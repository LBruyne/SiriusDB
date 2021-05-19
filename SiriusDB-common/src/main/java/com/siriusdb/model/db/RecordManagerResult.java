package com.siriusdb.model.db;

import lombok.Data;

@Data
public class RecordManagerResult<T> {
    private T value;
    private String message;
    private boolean status;
}