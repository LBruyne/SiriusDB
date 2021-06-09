package com.siriusdb.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Table implements Serializable {

    private TableMeta meta;

    /**
     * index的数量直接调indexes.size()获取，不要同时维护两个变量，容易出现遗漏
     */
    private List<Index> indexes;

    /**
     * 表格下的数据
     */
    private List<Row> data;

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof Table))
            return false;
        if (another == this)
            return true;
        return meta.equals(((Table) another).getMeta()) && data.equals(((Table) another).getData());
    }

    @Override
    public int hashCode() {
        return data.size();
    }

    public int fetchAttributeColID(String attributeName) {
        int ret = -1;
        for (int i = 0; i < meta.getAttributes().size(); i++) {
            if (meta.getAttributes().get(i).getName().equals(attributeName))
                ret = i;
        }
        return ret;
    }

}