package com.siriusdb.model.db;
import com.siriusdb.enums.DataTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {

    private Integer id;

    private String name;

    private String type;

    private Boolean hasIndex;

    private Boolean isUnique;

    private String indexName;

    //private Table table;
}
