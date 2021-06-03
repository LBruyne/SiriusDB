package com.siriusdb.client.db.api;

import com.siriusdb.model.RecordManagerResult;
import com.siriusdb.model.db.*;

import java.util.List;

public interface IRecordManager {

    /*
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
    */

    RecordManagerResult<RecordManagerTable> select(List<TableAttribute> selectedAttributes, List<Table> tables, List<AttrVSAttrCondition> joinCondition, List<ICondition> whereCondition, boolean isAnd);
    /*
     * 举例：
     * select student.studentID, grades.studentGrade from student join grades on student.studentID = grades.studentID where student.studentID = 123 and grades.courseName = "NLP";
     * selectedAttributes： List<Element<?>> (len = 2), 每一个element内部需要指明table，否则不知道是哪个table的属性了
     * tables: List<Table> (len = 2)
     * joinCondition: List<Condition<?>> (len = 1) Condition类内部还是需要将它的Element中的Table设置好，不然不知道是哪个表了
     * whereCondition: List<Condition<?>> (len = 2) Condition类内部还是需要将它的Element中的Table设置好，不然不知道是哪个表了
     * isAnd = true 表示where 后面的每个condition中间用and连接，如果是or就传入false | 不支持and和or混合
     * */


    RecordManagerResult delete(Table table, List<ICondition> cons, boolean isAnd);

    RecordManagerResult insert(Table table, List<Element> values);

    RecordManagerResult update(Table table, List<ICondition> setCondition, List<ICondition> whereCondition, boolean isAnd);

}



