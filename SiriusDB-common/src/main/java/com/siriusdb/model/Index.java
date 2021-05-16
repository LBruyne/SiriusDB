package com.siriusdb.model;

public class Index {
    //.. 描述一张表格的一个索引的情况，更多具体的内容请IndexManager编写人员负责补充完善
    //此处只列举RecordManager必需的
    private String tableName;// 这个索引是哪张表的
    private int index; //这个属性在表中是第几列？ || 不考虑组合索引？
    //...
    //emmm我怎么感觉这东西要做成B+树的聚集索引。。
}