package com.siriusdb.client.db.manager;
import com.siriusdb.client.db.api.IRecordManager;
import com.siriusdb.model.db.RecordManagerResult;
import com.siriusdb.model.db.Condition;
import com.siriusdb.model.db.Table;

import java.util.List;

/**
 * @Description: module for record management.
 * @author: Hu Yangfan
 * @date: 2021/05/16 8:37 下午
 */
public class RecordManager implements IRecordManager{
    /*
    private Object data;

    public void ReacordManager() {

    }


    public RecordManagerResult<List<Attribute[]>> select(List<Attribute> selectedAttributes, List<Table> tables,
                                                         List<Condition> joinCondition, List<Condition> whereCondition, Attribute orderBy, boolean asc){return null;}
    //  select        studentName, ID          from students joins instructor on student.ID = instructor.ID  where studentName = "..." OR ID = "" order by ID asc;\
    //  调用select    selectedAttributes          tables                    joinCondition                           whereConditions           orderBy   asc
    //  升序则asc为True，否则为false
    //  返回的List<String[]> 中，每一个Attribute为被选中的属性的对象，对象里面有值
    //  用户没有输入order by则变量orderBy传入一个null即可，后面的boolean为未定义
    public RecordManagerResult<?> delete(Table table, List<Condition> whereCondition){return null;}

    public RecordManagerResult<?> update(Table table, Attribute<?> setWhich, Attribute<?> newValue,
                                  List<Condition> whereConditions){return null;}

    public RecordManagerResult<?> insert(Table table, List<Attribute<?>> values){return null;}

     */

}
