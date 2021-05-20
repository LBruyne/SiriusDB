package com.siriusdb.model.db;
import com.siriusdb.enums.DataTypeEnum;
import lombok.Data;

@Data
public class Attribute {

    private Integer id;

    private String name;

    private DataTypeEnum type;

    private Boolean hasIndex;

    private Boolean isUnique;

    private String indexName;

    public Attribute(int id, String name, DataTypeEnum type, boolean hasIndex, boolean isUnique, String indexName) {
        this.setId(id);
        this.setName(name);
        this.setType(type);
        this.setHasIndex(hasIndex);
        this.setIsUnique(isUnique);
        this.setIndexName(indexName);
    }

    //private Table table;
}
