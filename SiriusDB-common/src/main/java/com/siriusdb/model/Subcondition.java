package com.siriusdb.model;
import com.siriusdb.enums.PredicateManagerEnum;
import lombok.Data;

@Data
public class Subcondition<T extends Comparable> {
    public T firstOperand;
    public T SecondOperand;
    public PredicateManagerEnum opreator;
}