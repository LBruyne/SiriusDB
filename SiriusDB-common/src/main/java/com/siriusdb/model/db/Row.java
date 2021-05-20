package com.siriusdb.model.db;
//import com.siriusdb.model.db.Attribute;
import lombok.Data;

import java.util.List;
import java.util.Vector;

@Data
public class Row {

    private Vector<String> attributeValue;

    public void add_attribute_value(String attributeValue){
        this.attributeValue.add(attributeValue);
    }; // 添加一个属性值

    public String get_attribute_value(int index){
        return attributeValue.get(index);
    }; // 得到对应下标的属性值
}
