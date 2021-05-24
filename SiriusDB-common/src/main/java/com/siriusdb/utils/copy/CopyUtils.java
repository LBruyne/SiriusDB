package com.siriusdb.utils.copy;

import com.siriusdb.model.db.Attribute;
import com.siriusdb.model.db.TableMeta;
import com.siriusdb.thrift.model.VAttribute;
import com.siriusdb.thrift.model.VTableMeta;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

/**
 * @Description: 通用的JavaBean之间相互转换用的工具类
 * @author: liuxuanming
 * @date: 2021/05/24 6:38 下午
 */
public class CopyUtils {

    /**
     * TableMeta -> VTableMeta
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
        return vtable;
    }

    /**
     * Attr -> VAttr
     * @param attribute
     * @return
     */
    public static VAttribute attrToVAttr(Attribute attribute) {
        VAttribute vAttribute = new VAttribute();
        BeanUtils.copyProperties(attribute, vAttribute);
        return vAttribute;
    }

    /**
     * VTableMeta -> TableMeta
     * @param vTableMeta
     * @return
     */
    public static TableMeta vTableMToTableM(VTableMeta vTableMeta) {
        TableMeta tableMeta = new TableMeta();
        BeanUtils.copyProperties(vTableMeta, tableMeta);
        tableMeta.setAttributes(vTableMeta.getAttributes()
                .stream()
                .map(vAttr -> vAttrToAttr(vAttr)).collect(Collectors.toList()));
        return tableMeta;
    }

    /**
     * VAttr -> Attr
     * @param vAttribute
     * @return
     */
    public static Attribute vAttrToAttr(VAttribute vAttribute) {
        Attribute attribute = new Attribute();
        BeanUtils.copyProperties(vAttribute, attribute);
        return attribute;
    }
}
