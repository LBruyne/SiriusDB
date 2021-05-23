package com.siriusdb.model.db;
import com.siriusdb.enums.DataTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attribute implements Serializable {

    private Integer id;

    private String name;

    private String type;

}
