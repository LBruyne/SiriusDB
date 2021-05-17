package com.siriusdb.model;
//import com.siriusdb.model.Attribute;
import lombok.Data;

import java.util.List;

@Data
public class Row {
    private List<Attribute<?>> colData;
}
