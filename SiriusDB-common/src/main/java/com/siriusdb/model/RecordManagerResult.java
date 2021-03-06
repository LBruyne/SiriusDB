package com.siriusdb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordManagerResult<T> {
    private T value;
    private String message;
    private Boolean status;
}