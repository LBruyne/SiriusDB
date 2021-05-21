package com.siriusdb.model.db;
//import com.siriusdb.model.db.Attribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Row {

    private List<String> attributeValue;

    public void addAttributeValue(String attributeValue){
        this.attributeValue.add(attributeValue);
    }; // 添加一个属性值

    public String getAttributeValue(int index){
        return attributeValue.get(index);
    }; // 得到对应下标的属性值
}
