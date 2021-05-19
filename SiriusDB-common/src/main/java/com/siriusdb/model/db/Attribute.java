package com.siriusdb.model.db;
import com.siriusdb.enums.DataTypeEnum;
import lombok.Data;

@Data
public class Attribute {

    private Integer id;

    private String name;

    private String type;

    private Boolean hasIndex;

    private Integer indexID; //type depends on Yang's definition

    private Table table;
}
