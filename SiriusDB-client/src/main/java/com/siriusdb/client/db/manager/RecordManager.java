package com.siriusdb.client.db.manager;

import com.siriusdb.client.db.api.IRecordManager;
import com.siriusdb.enums.PredicateEnum;
import com.siriusdb.model.RecordManagerResult;
import com.siriusdb.model.db.*;
import com.sun.org.apache.bcel.internal.generic.LUSHR;

import javax.swing.text.TableView;
import java.util.*;

import com.siriusdb.enums.DataTypeEnum;
import org.apache.thrift.TException;

/**
 * @Description: module for record management.
 * @author: Hu Yangfan
 * @date: 2021/05/16 8:37 下午
 */
public class RecordManager implements IRecordManager {
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


    //  √√√√√√√√
    private RecordManagerTable convertFrom(Table table) {
        if (table == null)
            return null;
        RecordManagerTable ret = new RecordManagerTable();
        ret.setTable(table);
        ret.setData(table.getData());
        for (Row eachRow : table.getData()) {
            for (Element eachElement : eachRow.getElements()) {
                if (eachElement.getType().equals(DataTypeEnum.STRING.getType())) {
                    eachElement.setData(((String) eachElement.getData()).replaceAll("'", ""));
                }
            }
        }
        List<TableAttribute> attr = new LinkedList<>();
        for (Attribute attribute : table.getMeta().getAttributes()) {
            Set<Table> set = new HashSet<>();
            set.add(table);
            attr.add(new TableAttribute(set, attribute));
        }
        ret.setAttr(attr);
        return ret;
    }

    private Table convertTo(RecordManagerTable table) {
        return null;
    }


    private Row Merge2Row(RecordManagerTable table1, RecordManagerTable table2, Row line_1, Row line_2, List<AttrVSAttrCondition> joinConditions) {
//        Called only during inner join
        int firstTableColID, secondTableColID;
        List<Element> ls = new LinkedList<>();
        boolean select = true;
        List<TableAttribute> skipSecond = new LinkedList<>();
        for (AttrVSAttrCondition con : joinConditions) {
            skipSecond.add(con.getLatterAttribute());
            firstTableColID = table1.fetchColID(con.getFormerAttribute());
            secondTableColID = table2.fetchColID(con.getLatterAttribute());
            if (firstTableColID != -1 && secondTableColID != -1) {
                Object value1 = line_1.getElements(firstTableColID).getData();
                String type1 = line_1.getElements(firstTableColID).getType();
                Object value2 = line_2.getElements(secondTableColID).getData();
                String type2 = line_2.getElements(secondTableColID).getType();
                if (type1.equals(type2)) {
                    if (type1.equals(DataTypeEnum.STRING.getType()))
                        select = select && judge((String) value1, (String) value2, con.getOperator());
                    if (type1.equals(DataTypeEnum.INTEGER.getType()))
                        select = select && judge(Integer.parseInt((String) value1), Integer.parseInt((String) value2), con.getOperator());
                    if (type1.equals(DataTypeEnum.FLOAT.getType()))
                        select = select && judge(Float.parseFloat((String) value1), Float.parseFloat((String) value2), con.getOperator());
                } else
                    select = false;
            } else {
                // attr doesn't exist at all!
                select = false;
            }
        }

        if (select) {
            // this line is to be merged and returned.
            Row newRow = new Row(line_1.getElements());
            for (int i = 0; i < table2.getAttr().size(); i++) {
                if (!skipSecond.contains(table2.getAttr().get(i)))
                    newRow.addElement(line_2.getElements(i));
            }
            return newRow;
        } else {
            return null;
        }

    }

    private <T extends Comparable<T>> Boolean judge(T first, T second, PredicateEnum op) {
        int res = first.compareTo(second);
        boolean ret = false;
        switch (op.getCode()) {
            case 1:
                ret = res > 0;
                break;
            case 2:
                ret = res < 0;
                break;
            case 3:
                ret = !(res < 0);
                break;
            case 4:
                ret = !(res > 0);
                break;
            case 5:
                ret = res == 0;
                break;
            case 6:
                ret = res != 0;
                break;
        }
        return ret;
    }

    private List<TableAttribute> getMergedAttributeOf2Table(RecordManagerTable table1, RecordManagerTable table2, List<AttrVSAttrCondition> joinConditions) {
        List<TableAttribute> merged = new LinkedList<>();
        for (AttrVSAttrCondition each : joinConditions) {
            // tableA.attr  <operator> tableB.attr
            assert (each.getOperator() == PredicateEnum.EQUAL);
            Set<Table> set = new HashSet<>();
            set.addAll(each.getFormerAttribute().getTable());
            set.addAll(each.getLatterAttribute().getTable());
            TableAttribute thisAttr = new TableAttribute(set, each.getFormerAttribute().getAttribute());
            merged.add(thisAttr);
        }
        return merged;
    }

    private RecordManagerTable joinTables(List<Table> tables, List<AttrVSAttrCondition> joinCondition) {
        List<RecordManagerTable> workTables = new LinkedList<>();
        for (Table each : tables) {
            RecordManagerTable temp = convertFrom(each);
            if (temp != null)
                workTables.add(temp);
        }
        RecordManagerTable firstTable = workTables.remove(0);
        RecordManagerTable ret = firstTable;
        RecordManagerTable workTable = new RecordManagerTable();

        if (workTables.size() != 0) {
            // we have many tables to join together( at least do join once
            while (workTables.size() != 0) {
                //get second table
                RecordManagerTable secondTable = workTables.remove(0);

                //get adn set the attribute list of the resulted table
                List<TableAttribute> resultAttribute = getMergedAttributeOf2Table(firstTable, secondTable, joinCondition);
                workTable.setAttr(resultAttribute);

                // iterate all records of the 2 table and manage to concatenate all rows, all derived rows are added to workTable
                for (Row firstTableLine : firstTable.getData()) {
                    for (Row secondTableLine : secondTable.getData()) {
                        Row combined = Merge2Row(firstTable, secondTable, firstTableLine, secondTableLine, joinCondition);
                        if (combined != null)
                            workTable.getData().add(combined);
                    }
                }

                // finished generating a new table

                firstTable = workTable;
                if (workTables.size() != 0)
                    workTable = new RecordManagerTable();
                // in the next iteration, we join first table with the second again.
                // previous workTable is deprecated
            }
//            return ret;
        } else {
            // actually executing selection from a single table
//            return convertFrom(tables.get(0));

            // do sth
        }
        return ret;


    }

    private Row judgeRow(RecordManagerTable targetTable, List<ICondition> whereConditions, boolean isAnd, Row thisLine) {
        int firstColID, secondColID;
        boolean decide = true;
        for (ICondition each : whereConditions) {
            if (each instanceof AttrVSAttrCondition) {
                AttrVSAttrCondition thisCon = (AttrVSAttrCondition) each;
                firstColID = targetTable.fetchColID(thisCon.getFormerAttribute());
                secondColID = targetTable.fetchColID(thisCon.getLatterAttribute());
                Element first = thisLine.getElements(firstColID);
                Element second = thisLine.getElements(secondColID);
                if (first.getType().equals(second.getType())) {
                    if (first.getType().equals(DataTypeEnum.STRING.getType())) {
                        decide = decide && (judge((String) (first.getData()), (String) (second.getData()), thisCon.getOperator()));
                    }
                    if (first.getType().equals(DataTypeEnum.INTEGER.getType()))
                        decide = decide && (judge((Integer) (first.getData()), (Integer) (second.getData()), thisCon.getOperator()));
                    if (first.getType().equals(DataTypeEnum.FLOAT.getType()))
                        decide = decide && (judge((Float) (first.getData()), (Float) (second.getData()), thisCon.getOperator()));

                }
            } else if (each instanceof AttrVSValueCondition) {
                AttrVSValueCondition thisCon = (AttrVSValueCondition) each;
                firstColID = targetTable.fetchColID(thisCon.getFormerAttribute());
                Element first = thisLine.getElements(firstColID);
                Element second = thisCon.getLatterDataElement();
                if (first.getType().equals(second.getType())) {
                    if (first.getType().equals(DataTypeEnum.STRING.getType())) {
                        thisCon.getLatterDataElement().setData(((String) thisCon.getLatterDataElement().getData()).replaceAll("'", ""));
                        decide = decide && (judge((String) (first.getData()), (String) (second.getData()), thisCon.getCondition()));
                    }
                    if (first.getType().equals(DataTypeEnum.INTEGER.getType()))
                        decide = decide && (judge((Integer) (first.getData()), Integer.parseInt((String) second.getData()), thisCon.getCondition()));
                    if (first.getType().equals(DataTypeEnum.FLOAT.getType()))
                        decide = decide && (judge((Float) (first.getData()), Float.parseFloat((String) second.getData()), thisCon.getCondition()));
//


//                    decide = decide && judge(first,second,thisCon.getCondition());
                }
            } else {
                // impossible
            }
        }
        if (isAnd) {
            if (decide)
                return thisLine;
            else return null;
        } else {
            if (decide)
                return null;
            else
                return thisLine;
        }
    }

    private void inverseCondition(AttrVSAttrCondition con) {
        switch (con.getOperator().getCode()) {
            case 1:
                con.setOperator(PredicateEnum.SMALLERorEQUAL);
                break;
            case 2:
                con.setOperator(PredicateEnum.LARGERorEQUAL);
                break;
            case 3:
                con.setOperator(PredicateEnum.SMALLER);
                break;
            case 4:
                con.setOperator(PredicateEnum.LARGER);
                break;
            case 5:
                con.setOperator(PredicateEnum.notEQUAL);
                break;
            case 6:
                con.setOperator(PredicateEnum.EQUAL);
                break;
        }
    }

    private void inverseCondition(AttrVSValueCondition con) {
        switch (con.getCondition().getCode()) {
            case 1:
                con.setCondition(PredicateEnum.SMALLERorEQUAL);
                break;
            case 2:
                con.setCondition(PredicateEnum.LARGERorEQUAL);
                break;
            case 3:
                con.setCondition(PredicateEnum.SMALLER);
                break;
            case 4:
                con.setCondition(PredicateEnum.LARGER);
                break;
            case 5:
                con.setCondition(PredicateEnum.notEQUAL);
                break;
            case 6:
                con.setCondition(PredicateEnum.EQUAL);
                break;
        }
    }

    private RecordManagerTable filterRows(RecordManagerTable targetTable, List<ICondition> whereConditions, boolean isAnd) {
        List<Row> resRows = new LinkedList<>();
        for (Row each : targetTable.getData()) {
            Row temp = judgeRow(targetTable, whereConditions, isAnd, each);
            if (temp != null)
                resRows.add(temp);
        }
        RecordManagerTable ret = new RecordManagerTable();
        ret.setAttr(targetTable.getAttr());
        ret.setTable(targetTable.getTable());
        ret.setData(resRows);
        return ret;
    }

    private Row setRow(RecordManagerTable table, List<ICondition> setCon, Row thisLine) {
        for (ICondition eachCon : setCon) {
            if (eachCon instanceof AttrVSAttrCondition) {
                AttrVSAttrCondition workCon = (AttrVSAttrCondition) eachCon;
                int firstColID = table.fetchColID(workCon.getFormerAttribute());
                int secondColID = table.fetchColID(workCon.getLatterAttribute());
                thisLine.replaceElements(firstColID, thisLine.getElements(secondColID));
            } else if (eachCon instanceof AttrVSValueCondition) {
                AttrVSValueCondition workCon = (AttrVSValueCondition) eachCon;
                int firstColID = table.fetchColID(workCon.getFormerAttribute());
                thisLine.replaceElements(firstColID, workCon.getLatterDataElement());
            } else {
                // impossible to reach here!
            }
        }
        return thisLine;
    }

    private void filterB4Return(List<TableAttribute> selectedAttributes, RecordManagerTable res) {
        List<Integer> selectedAttributeIndex = new LinkedList<>();
        Set<String> selectedStrings = new HashSet<>();
        for (TableAttribute attr : selectedAttributes) {
            selectedStrings.add(attr.getAttribute().getName());
        }
        for (int i = 0; i < res.getAttr().size(); i++) {
            if (!selectedStrings.contains(res.getAttr().get(i).getAttribute().getName())) {
                selectedAttributeIndex.add(i);
            }
        }
        Collections.reverse(selectedAttributeIndex);
        for (Integer eachIndex : selectedAttributeIndex) {
            res.getAttr().remove(eachIndex.intValue());
            for (Row each : res.getData()) {
                each.getElements().remove(eachIndex.intValue());
            }
        }
    }

    public RecordManagerResult<RecordManagerTable> select(List<TableAttribute> selectedAttributes, List<Table> tables, List<AttrVSAttrCondition> joinCondition, List<ICondition> whereCondition, boolean isAnd) {
//
        // preCondition:
        // joinCondition 里面的都只能是 =
        RecordManagerTable targetTable = joinTables(tables, joinCondition);
//        do joins first, a derived table is generated
        if (!isAnd) {
            for (ICondition con : whereCondition) {
                if (con instanceof AttrVSValueCondition)
                    inverseCondition((AttrVSValueCondition) con);
                if (con instanceof AttrVSAttrCondition)
                    inverseCondition((AttrVSAttrCondition) con);
            }
        }
        RecordManagerTable res = filterRows(targetTable, whereCondition, isAnd);
//        filter Rows one by one , collect selected rows

        filterB4Return(selectedAttributes, res);

        RecordManagerResult<RecordManagerTable> ret = new RecordManagerResult<>();

        ret.setValue(res);
        ret.setMessage("查询成功");
        ret.setStatus(true);

        return ret;

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
     * 没有任何条件麻烦也传一个true
     * */


    public RecordManagerResult delete(Table table, List<ICondition> cons, boolean isAnd) {
        // 可能 delete .. from tableA where tableA.attrA != tableA.attrB
        RecordManagerTable workTable = convertFrom(table);
        RecordManagerResult ret= new RecordManagerResult();
        if (cons == null || cons.size() == 0) {
            table.setData(new LinkedList<>());
            ret.setMessage("成功删除"+workTable.getData().size()+"条记录.");
            ret.setStatus(true);
        } else {
            List<Integer> st = new LinkedList<>();
            for (int i = 0; i < workTable.getData().size(); i++) {
                if (judgeRow(workTable, cons, isAnd, workTable.getData().get(i)) != null) {
                    st.add(i);
                }
            }
            Collections.reverse(st);
            for (Integer each : st)
                workTable.getData().remove(each.intValue());
            ret.setMessage("成功删除"+st.size()+"条记录.");
            ret.setStatus(false);
            try {
                DataLoader.alterTable(table);
            } catch (TException e) {
                e.printStackTrace();
            }
        }
        return ret;

    }

    public RecordManagerResult insert(Table table, List<Element> values) {
        RecordManagerResult ret = new RecordManagerResult();
        int fuck=0;
        for (Element each : values) {
            if (each.getType().equals(DataTypeEnum.STRING.getType())) {
                each.setData(each.getData().toString().replaceAll("'", ""));
                each.setColumnId(fuck++);
            }
        }


        RecordManagerTable workTable = convertFrom(table);
        int primaryKeyCol = table.fetchAttributeColID(table.getMeta().getPrimaryKey());
        if (primaryKeyCol == -1) {
            // sth wrong
            ret.setStatus(false);
            ret.setMessage("找不到primary key！");
        } else {
            for (Row eachRow : workTable.getData()) {
                boolean duplicate = false;
                if (eachRow.getElements(primaryKeyCol).getType().equals(DataTypeEnum.INTEGER.getType())) {
                    duplicate = judge(Integer.parseInt((String) eachRow.getElements(primaryKeyCol).getData()), Integer.parseInt((String) values.get(primaryKeyCol).getData()), PredicateEnum.EQUAL);
                }
                if (eachRow.getElements(primaryKeyCol).getType().equals(DataTypeEnum.FLOAT.getType())) {
                    duplicate = judge(Float.parseFloat((String) eachRow.getElements(primaryKeyCol).getData()), Float.parseFloat((String) values.get(primaryKeyCol).getData()), PredicateEnum.EQUAL);
                }
                if (eachRow.getElements(primaryKeyCol).getType().equals(DataTypeEnum.STRING.getType())) {
                    duplicate = judge((String) eachRow.getElements(primaryKeyCol).getData(), (String) values.get(primaryKeyCol).getData(), PredicateEnum.EQUAL);
                }
                if (duplicate) {
                    // primary key duplicate
                    ret.setMessage("主键已存在，重复插入！");
                    ret.setStatus(true);
                    return ret;
                }


            }
            List<Row> rows = table.getData();
            Row thisLine = new Row();

            if (values.size() != table.getMeta().getAttributes().size()) {
                ret.setStatus(false);
                ret.setMessage("输入属性数量和表格不匹配！");
            } else {
                thisLine.setElements(new LinkedList<Element>());
                thisLine.getElements().addAll(values);
                rows.add(thisLine);
            }
            ret.setStatus(true);
            ret.setMessage("插入成功！");
            try {
                DataLoader.alterTable(table);
            } catch (TException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public RecordManagerResult update(Table table, List<ICondition> setCondition, List<ICondition> whereConditions, boolean isAnd) {
        RecordManagerTable workTable = convertFrom(table);
        RecordManagerResult ret= new RecordManagerResult();
        int updateCount=0;
        List<Row> rows = workTable.getData();
        for (int i = 0; i < rows.size(); i++) {
            Row thisLine = rows.get(i);
            if (judgeRow(workTable, whereConditions, isAnd, thisLine) != null) {
                rows.set(i, setRow(workTable, setCondition, thisLine));
                updateCount++;
            }
        }
        ret.setStatus(true);
        ret.setMessage("成功更新"+updateCount+"条记录！");
        if(updateCount>0) {
            try {
                DataLoader.alterTable(table);
            } catch (TException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

}


// from a join b on .... join c on ... where a.sss = 123 and b.cc = aa