package com.siriusdb.client.db.manager;
import com.siriusdb.client.db.api.IRecordManager;
import com.siriusdb.model.RecordManagerResult;
import com.siriusdb.model.db.*;

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

    public RecordManagerResult<List<Row>> select(List<TableAttribute> selectedAttributes, List<Table> tables, List<ICondition> joinCondition, List<ICondition> whereCondition, boolean isAnd){
        return null;
    }
    /*
     * Q:为什么要有两种Condition？
     * A：where tableA.attributeA = 123 和 where tableA.attributeA = tableB.attributeB 是两种不同的类型
     *      前者对应了AttrVSAttrCondition        后者对应了AttrVSValueCondition
     *      因此列表中泛型参数填一个ICondition即可
     *
     *
     * 举例：
     * select student.studentID, grades.studentGrade from student join grades on student.studentID = grades.studentID where student.studentID = 123 and grades.courseName = "NLP";
     * selectedAttributes： List<Element<?>> (len = 2), 每一个element内部需要指明table，否则不知道是哪个table的属性了
     * tables: List<Table> (len = 2)
     * joinCondition: List<ICondition> (len = 1) JoinCondition类内部有两个TableAttribute，每个TableAttribute内部需要设置好这个属性的表是哪个，不然找不到啊qwq
     * whereCondition: List<ICondition> (len = 2) whereCondition类内部还是需要将它的Element中的Table设置好，不然不知道是哪个表了
     * isAnd = true 表示where 后面的每个condition中间用and连接，如果是or就传入false | 不支持and和or混合
     * */


    public RecordManagerResult delete(Table table, List<AttrVSValueCondition<?>> cons){
        return null;
    }

    public RecordManagerResult insert(Table table, List<Element<?>> values){
        return null;
    }

    public RecordManagerResult update(Table table, List<AttrVSValueCondition<?>> setCondition, List<AttrVSValueCondition<?>> attrVSValueCondition){
        return null;
    }

}
