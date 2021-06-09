package com.siriusdb.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attribute implements Serializable {

    private Integer id;

    private String name;

    private String type;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof Attribute))
            return false;
        if (another == this)
            return true;
        return name.equals(((Attribute) another).getName()) && type.equals(((Attribute) another).getType());

    }


}
