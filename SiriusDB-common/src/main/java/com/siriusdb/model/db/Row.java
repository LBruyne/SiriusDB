package com.siriusdb.model.db;
//import com.siriusdb.model.db.Attribute;
import lombok.Data;

import java.util.List;

@Data
public class Row {

    private List<Element<?>> elements;

}
