package com.siriusdb.utils.copy;

import com.siriusdb.enums.DataTypeEnum;
import com.siriusdb.model.db.*;
import com.siriusdb.thrift.model.*;
import org.springframework.beans.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;

/**
 * @Description: 通用的JavaBean之间相互转换用的工具类
 * @author: liuxuanming
 * @date: 2021/05/24 6:38 下午
 */
@Slf4j
public class CopyUtils {

    /**
     * TableMeta -> VTableMeta
     *
     * @param table
     * @return
     */
    public static VTableMeta tableMToVTableM(TableMeta table) {
//        // 比较复杂的办法，一个个属性复制
//        VTableMeta vtable = new VTableMeta();
//        // 复制name属性
//        vtable.setName(table.getName());
//        // 复制primaryKey属性
//        vtable.setPrimaryKey(table.getPrimaryKey());
//        // 复制locatedServerName属性
//        vtable.setLocatedServerName(table.getLocatedServerName());
//        // 复制attributes属性
//        List<VAttribute> vAttributes = new LinkedList<>();
//        for (Attribute attribute : table.getAttributes()) {
//            VAttribute vAttribute = new VAttribute();
//            vAttribute.setId(attribute.getId());
//            vAttribute.setName(attribute.getName());
//            vAttribute.setType(attribute.getType());
//            vAttributes.add(vAttribute);
//        }
//        vtable.setAttributes(vAttributes);
//        return vtable;

        // 比较简单的办法，用BeanUtils进行属性拷贝
        VTableMeta vtable = new VTableMeta();
        // 公共属性复制：要求属性类型、属性名称一致
        BeanUtils.copyProperties(table, vtable);
        // 复制attributes属性
        vtable.setAttributes(table.getAttributes().stream().map(attribute -> attrToVAttr(attribute)).collect(Collectors.toList()));
        log.warn("此次将tablemeta{}赋值给vtblemeta{}",table,vtable);
        return vtable;
    }

    /**
     * VTableMeta -> TableMeta
     *
     * @param vTableMeta
     * @return
     */
    public static TableMeta vTableMToTableM(VTableMeta vTableMeta) {
        TableMeta tableMeta = new TableMeta();
        BeanUtils.copyProperties(vTableMeta, tableMeta);
        tableMeta.setAttributes(vTableMeta.getAttributes()
                .stream()
                .map(vAttr -> vAttrToAttr(vAttr)).collect(Collectors.toList()));
        log.warn("此次将vtablemeta:{}赋值给tablemeta:{}",vTableMeta,tableMeta);
        return tableMeta;
    }

    /**
     * VTable -> Table
     *
     * @param vTable
     * @return
     */
    public static Table vTableToTable(VTable vTable) {
        Table table = new Table();
        BeanUtils.copyProperties(vTable, table);
        table.setMeta(vTableMToTableM(vTable.getMeta()));
        table.setIndexes(vTable.getIndexes()
                .stream()
                .map(vIndex -> vIndexToIndex(vIndex)).collect(Collectors.toList()));
        table.setData(vTable.getData()
                .stream()
                .map(vRow -> vRowToRow(vRow)).collect(Collectors.toList()));
        log.warn("此次将vtable:{}赋值给table:{}",vTable,table);
        return table;
    }

    /**
     * Table -> VTable
     *
     * @param table
     * @return
     */
    public static VTable tableToVTable(Table table) {
        VTable vTable = new VTable();
        BeanUtils.copyProperties(table, vTable);
        vTable.setMeta(tableMToVTableM(table.getMeta()));
        vTable.setIndexes(table.getIndexes()
                .stream()
                .map(index -> indexToVIndex(index)).collect(Collectors.toList()));
        vTable.setData(table.getData()
                .stream()
                .map(row -> rowToVRow(row)).collect(Collectors.toList()));
        log.warn("此次将table{}赋值给vtble{}",table,vTable);
        return vTable;
    }


    /**
     * Index -> VIndex
     *
     * @param index
     * @return
     */
    public static VIndex indexToVIndex(Index index) {
        VIndex vIndex = new VIndex();
        BeanUtils.copyProperties(index, vIndex);
        return vIndex;
    }

    /**
     * VIndex -> Index
     *
     * @param vIndex
     * @return
     */
    public static Index vIndexToIndex(VIndex vIndex) {
        Index index = new Index();
        BeanUtils.copyProperties(vIndex, index);
        return index;
    }


    /**
     * VAttr -> Attr
     *
     * @param vAttribute
     * @return
     */
    public static Attribute vAttrToAttr(VAttribute vAttribute) {
        Attribute attribute = new Attribute();
        BeanUtils.copyProperties(vAttribute, attribute);
        return attribute;
    }

    /**
     * Attr -> VAttr
     *
     * @param attribute
     * @return
     */
    public static VAttribute attrToVAttr(Attribute attribute) {
        VAttribute vAttribute = new VAttribute();
        BeanUtils.copyProperties(attribute, vAttribute);
        return vAttribute;
    }


    /**
     * VElement -> Element
     *
     * @param vElement
     * @return
     */
    public static Element vElementToElement(VElement vElement) {
        Element element = new Element();
        BeanUtils.copyProperties(vElement, element);
        if(vElement.getType().equals(DataTypeEnum.INTEGER.getType())){
            element.setData(Integer.parseInt(vElement.getData()));
        }
        if(vElement.getType().equals(DataTypeEnum.FLOAT.getType())){
            element.setData(Float.parseFloat(vElement.getData()));
        }
        return element;
    }

    /**
     * Element -> VElement
     *
     * @param element
     * @return
     */
    public static VElement elementToVElement(Element element) {
        VElement vElement = new VElement();
        BeanUtils.copyProperties(element, vElement);
        return vElement;
    }

    /**
     * Row -> VRow
     *
     * @param row
     * @return
     */
    public static VRow rowToVRow(Row row) {
        VRow vRow = new VRow();
        BeanUtils.copyProperties(row, vRow);
        vRow.setElements(row.getElements().stream().map(element -> elementToVElement(element)).collect(Collectors.toList()));
        return vRow;
    }

    /**
     * VRow -> Row
     *
     * @param vRow
     * @return
     */
    public static Row vRowToRow(VRow vRow) {
        Row row = new Row();
        BeanUtils.copyProperties(vRow, row);
        row.setElements(vRow.getElements().stream().map(vElement -> vElementToElement(vElement)).collect(Collectors.toList()));
        return row;
    }

}
