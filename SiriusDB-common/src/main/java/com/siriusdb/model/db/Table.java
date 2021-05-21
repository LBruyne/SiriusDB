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

}