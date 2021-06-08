package com.siriusdb.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Row implements Serializable {

    private List<Element> elements;

    public void addElement(Element element){
        this.elements.add(element);
    }; // 添加一个属性值

    public Element getElements(int index){
        return elements.get(index);
    }; // 得到对应下标的属性值

    public void replaceElements(int indexToBeReplaced, Element newEle){
        elements.remove(indexToBeReplaced);
        elements.add(indexToBeReplaced,newEle);
    }

/*    Row row = new Row();
    Element<String> element = new Element<String>();
        element.setType(DataTypeEnum.STRING.getType());
        element.setData("123");
        element.setColumnId(1);

    Element<Integer> element1 = new Element<Integer>();
        element1.setColumnId(2);
        element1.setType(DataTypeEnum.INTEGER.getType());
        element1.setData(211);

    List<Element> elementList = new ArrayList<>();
        elementList.add(element);
        elementList.add(element1);
        row.setElements(elementList);*/

}
