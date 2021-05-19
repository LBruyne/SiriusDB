package com.siriusdb.model.db;
import com.siriusdb.enums.DataTypeEnum;
import lombok.Data;

@Data
public class MetaAttribute {
    private String colName;
    private DataTypeEnum dataType;
    private boolean hasIndex;
    private int indexID;//type depends on Yang's definition
    private Table table;
}
