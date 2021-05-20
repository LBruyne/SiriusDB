package com.siriusdb.client.db.api;

import com.siriusdb.model.Attribute;
import com.siriusdb.model.Condition;
import com.siriusdb.model.Table;
import com.siriusdb.model.RecordManagerResult;
import java.util.List;

public interface IRecordManager {

    RecordManagerResult<List<Attribute[]>> select(List<Attribute> selectedAttributes, List<Table> tables,
                                                                  List<Condition> joinCondition, List<Condition> whereCondition, Attribute orderBy, boolean asc);
    //  select        studentName, ID          from students joins instructor on student.ID = instructor.ID  where studentName = "..." OR ID = "" order by ID asc;\
    //  调用select    selectedAttributes          tables                    joinCondition                           whereConditions           orderBy   asc
    // 升序则asc为True，否则为false
    // 返回的List<String[]> 中，每一个Attribute为被选中的属性的对象，对象里面有值
    // 用户没有输入order by则变量orderBy传入一个null即可，后面的boolean为未定义
    RecordManagerResult<?> delete(Table table, List<Condition> whereCondition);

    RecordManagerResult<?> update(Table table, Attribute<?> setWhich, Attribute<?> newValue,
                                                  List<Condition> whereConditions);

    RecordManagerResult<?> insert(Table table, List<Attribute<?>> values);
}



