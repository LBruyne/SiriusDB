
package com.siriusdb.client.db.interpreter;

/**
 * @Description: module for interpreter.
 * @author: Lluvia
 * @date: 2021/06/01 2:30 下午
 */

import com.siriusdb.client.db.manager.DataLoader;
import com.siriusdb.client.db.manager.RecordManager;
import com.siriusdb.enums.PredicateEnum;
import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.model.db.*;
import com.siriusdb.enums.DataTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.io.*;
import java.util.*;

import static com.siriusdb.enums.PredicateEnum.*;

@Slf4j
public class Interpreter {
    public static void initial() {
        try {
            welcome();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            interpreter(reader);
        } catch (BasicBusinessException e) {
            log.warn(e.getMessage(), e);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    public static void welcome() throws BasicBusinessException {
        System.out.println("Welcome to SiriusDB.");
    }

    public static void interpreter(BufferedReader reader) throws IOException {
        while (true) {
            int index;
            int reset = 0;
            String line ;
            StringBuilder input = new StringBuilder();
            // 处理输入
            while (true) {
                line = reader.readLine();
                if (line.equals("quit")) {
                    System.out.println("See You Again.");
                    return;
                } else if (line.equals("reset")) {
                    System.out.println("Reset Already.");
                    input.setLength(0);
                    reset = 1;
                    break;
                }
                if (line.contains(";")) {
                    index = line.indexOf(";");
                    input.append(line, 0, index);
                    break;
                } else
                    input.append(line);
            }
            if (reset == 1)
                continue;

            String query = input.toString().trim().replaceAll("\\s+", " ").replaceAll("’","'");
            System.out.println("Your input: " + query + ";");
            String[] qaq = query.split(" ");

            // 筛选分流
            try {
                if (qaq.length == 1 && qaq[0].equals(""))
                    throw new BasicBusinessException("Default error: No Input!");
                else if (qaq.length == 1)
                    throw new BasicBusinessException("Input error: Invalid query!");
                switch (qaq[0]) {
                    case "create":
                        switch (qaq[1]) {
                            case "table":
                                createTable(query);
                                break;
                            case "index":
                                createIndex(query);
                                break;
                            default:
                                throw new BasicBusinessException("Create error: Invalid creation!");
                        }
                        break;
                    case "drop":
                        switch (qaq[1]) {
                            case "table":
                                dropTable(query);
                                break;
                            case "index":
                                dropIndex(query);
                                break;
                            default:
                                throw new BasicBusinessException("Drop error: Invalid creation!");
                        }
                        break;
                    case "insert":
                        insert(query);
                        break;
                    case "delete":
                        delete(query);
                        break;
                    case "select":
                        select(query);
                        break;
                    case "update":
                        update(query);
                        break;
                    default:
                        throw new BasicBusinessException("Input error: Invalid query!");
                }
            } catch (BasicBusinessException e) {
                log.warn(e.getMessage(), e);
            }
        }
    }

    public static void createTable(String query) throws BasicBusinessException {
        System.out.println("Creating table ...");
        query = query.replaceAll(" *\\( *", " (").replaceAll(" *\\) *", ") ");
        query = query.replaceAll(" *, *", ",");
        query = query.trim();
        query = query.replaceAll("^create table", "").trim();

        if(query.equals(""))
            throw new BasicBusinessException("createTable error: Empty query!");

        int index;
        index = query.indexOf(" ");
        if(index == -1)
            throw new BasicBusinessException("createTable error: No attribute values!");
        String tableName = query.substring(0,index);

        if (!query.substring(index+1).matches("^\\(.*\\)$"))
            throw new BasicBusinessException("createTable error: Format mismatch!");


        String[] attrDefines;
        String primaryKey = "";
        List<Attribute> attributes = new ArrayList<>();
        attrDefines = query.substring(index+2).split(",");
        int id = 0;
        for (int i = 0; i < attrDefines.length; i++) {
            String[] attrDefine;
            Attribute attr = new Attribute();
            String attrName, attrType;
            if (i == attrDefines.length - 1) {
                attrDefine = attrDefines[i].trim().substring(0, attrDefines[i].length() - 1).split(" ");
            } else {
                attrDefine = attrDefines[i].trim().split(" ");
            }

            if (attrDefine[0].equals(""))
                throw new BasicBusinessException("createTable error: No attributes in your table!");
            if(attrDefine[0].equals("primary")){
                if (attrDefine.length != 3 || !attrDefine[1].equals("key"))
                    throw new BasicBusinessException("createTable error: Primary key set error!");
                if (!attrDefine[2].matches("^\\(.*\\)$"))
                    throw new BasicBusinessException("createTable error: Please enter the primary key in brackets'()' !");
                if (!primaryKey.equals(""))
                    throw new BasicBusinessException("createTable error: Have already set primary key!");
                primaryKey = attrDefine[2].substring(1,attrDefine[2].length()-1);
            }
            else{
                if (attrDefine.length ==1)
                    throw new BasicBusinessException("createTable error: Only define the attribute name!");
                attrName = attrDefine[0];
                attrType = attrDefine[1];

                for (int j = 0; j < attributes.size(); j++) {
                    if (attrName.equals(attributes.get(j).getName()))
                        throw new BasicBusinessException("createTable error: The attribute name "+attrName+" has existed!");
                }
                attr.setName(attrName);
                if (!attrType.equals(DataTypeEnum.INTEGER.getType()) && !attrType.equals(DataTypeEnum.FLOAT.getType()) && !attrType.equals(DataTypeEnum.STRING.getType()))
                    throw new BasicBusinessException("createTable error: Only support 'int' 'float' and 'string' !");
                attr.setType(attrType);
                attr.setId(id);

                if (attrDefine.length >= 3)
                    throw new BasicBusinessException("createTable error: Invalid attribute!");

                attributes.add(attr);
                id +=1;
            }
        }

//        for (int i = 0; i < attributes.size(); i++) {
//            System.out.println(attributes.get(i));
//        }

        if (primaryKey.equals(""))
            throw new BasicBusinessException("createTable error: No primary key!");

        //TODO: 接口
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName(tableName);
        tableMeta.setPrimaryKey(primaryKey);
        tableMeta.setAttributes(attributes);
        Table table = new Table();
        table.setData(new ArrayList<>());
        table.setIndexes(new ArrayList<>());
        table.setMeta(tableMeta);

        try {
            DataLoader.createTable(table);
        } catch (TException e) {
            e.printStackTrace();
        }

        System.out.println("Success: Table " + tableName + " has been created!");
    }

    public static void createIndex(String query) throws BasicBusinessException{
        System.out.println("Creating index ...");
        query = query.replaceAll("\\s+", " ");
        query = query.replaceAll(" *\\( *", " (").replaceAll(" *\\) *", ") ");
        query = query.trim();
        String[] qaq = query.split(" ");

        if(qaq.length == 2)
            throw new BasicBusinessException("createIndex error: No index name!");
        if(qaq.length == 3 || !qaq[3].equals("on") || qaq.length == 4)
            throw new BasicBusinessException("createIndex error: Please enter tableName after keyword 'on'!");
        if(qaq.length == 5)
            throw new BasicBusinessException("createIndex error: Please enter attribute name!");
        if(qaq.length != 6)
            throw new BasicBusinessException("createIndex error: Extra input!");

        String indexName = qaq[2];
        String tableName = qaq[4];
        String attrName = qaq[5];
        if(!attrName.matches("^\\(.*\\)$"))
            throw new BasicBusinessException("createIndex error: Invalid attribute name!");
        attrName = attrName.substring(1, attrName.length() - 1);

        // TODO: attrName and tableName valid or not

        // TODO: API for createIndex

        System.out.println("Success: Index " + indexName + " has been created!");
    }

    public static void dropTable(String query) throws BasicBusinessException{
        System.out.println("Dropping table ...");
        String[] qaq = query.split(" ");
        if (qaq.length == 2)
            throw new BasicBusinessException("dropTable error: Not specify table name!");
        if (qaq.length != 3)
            throw new BasicBusinessException("dropTable error: Invalid query!");

        String tableName = qaq[2];
        // TODO: API for dropTable

        System.out.println("Success: Table " + tableName + " has been dropped!");
    }

    public static void dropIndex(String query) throws BasicBusinessException{
        System.out.println("Dropping index ...");
        String[] qaq = query.split(" ");
        if (qaq.length == 2)
            throw new BasicBusinessException("dropIndex error: Not specify table name!");
        if (qaq.length != 3)
            throw new BasicBusinessException("dropIndex error: Invalid query!");

        String indexName = qaq[2];
        // TODO: API for dropTable

        System.out.println("Success: Index " + indexName + " has been dropped!");
    }

    public static void insert(String query) throws BasicBusinessException{
        System.out.println("Inserting values ...");
        query = query.replaceAll(" *\\( *", " (").replaceAll(" *\\) *", ") ");
        query = query.replaceAll(" *, *", ",");
        query = query.trim();

        String[] qaq = query.split(" ");

        if(!qaq[1].equals("into"))
            throw new BasicBusinessException("insert error: Please enter 'into' after 'insert' !");
        if(qaq.length == 2)
            throw new BasicBusinessException("insert error: No table name!");
        if(qaq.length == 3)
            throw new BasicBusinessException("insert error: Please enter 'values' and the value!");
        if(qaq.length == 4)
            throw new BasicBusinessException("insert error: Please enter the value!");
        if(!qaq[3].equals("values"))
            throw new BasicBusinessException("insert error: Please enter 'values' after the table name !");

        String tableName = qaq[2];

        if(!qaq[4].matches("^\\(.*\\)$"))
            throw new BasicBusinessException(("insert error: Format mismatch!"));


        String[] values = qaq[4].split(",");
        for (int i = 0; i < values.length ; i++) {
//            System.out.println(values[i]);
        }

        // TODO: 判断tableName

        // TODO: 判断数量并插入

        System.out.println(query + ";");
    }

    public static void delete(String query) throws BasicBusinessException{
        System.out.println("Deleting values ...");
        query = query.replaceAll(" *\\( *", " (").replaceAll(" *\\) *", ") ");
        query = query.replaceAll(" *, *", ",");
        query = query.trim();

        String[] qaq = query.split(" ");
        if(qaq.length<3)
            throw new BasicBusinessException("delete error: Invalid query!");
        if (!qaq[1].equals("from"))
            throw new BasicBusinessException("delete error: Please enter 'from' after 'delete' !");
        String tableName = qaq[2];
        if(qaq.length > 3){
            if (!qaq[3].equals("where"))
                throw new BasicBusinessException("delete error: Please enter 'where' behind the conditions!");
            String[] conditions = Arrays.asList(qaq).subList(4, qaq.length).toArray(new String[]{});
            int index=-1;
            int count = 0; //判断condition符不符合格式
            for (int i = 0; i < conditions.length; i++) {
                System.out.println(conditions[i]);
                count += 1;
                if (conditions[i].equals("and")){
                    index = i;
                    if (count !=4)
                        throw new BasicBusinessException("delete error: Conditions misformat1!");
                    count = 0;
                }
                if (index == i) {
                    // TODO: 生成条件condition
                }
            }
            if (count != 3)
                throw new BasicBusinessException("delete error: Conditions misformat2!");

            // TODO: delete from tableName where conditions 的接口

        }
        else if (qaq.length == 3){
            // TODO: delete from tableName 的接口
        }
    }

    public static void select(String query) throws BasicBusinessException {
        System.out.println("Selecting ...");
        query = query.replaceAll(" *\\( *", " (").replaceAll(" *\\) *", ") ");
        query = query.replaceAll(" *, *", ",");
        query = query.trim();
        String[] qaq = query.split(" ");

        List<TableAttribute> selectedAttributes = new ArrayList<>();
        List<Table> tables = new ArrayList<>();
        List<AttrVSAttrCondition> joinCondition = new ArrayList<>();
        List<ICondition> whereCondition = new ArrayList<>();
        boolean isAnd = true;
        boolean isJoin = false;

        if (qaq.length<4)
            throw new BasicBusinessException("select error: Invalid query!");
        if (!qaq[2].equals("from"))
            throw new BasicBusinessException("select error: Please enter 'from' before the tableName!");

        //判断是不是join
        if(qaq.length>=11){
            if (qaq[1].contains(",")&&(!qaq[4].equals("inner")||!qaq[5].equals("join")||!qaq[7].equals("on")))
                throw new BasicBusinessException("select error: Invalid inner join select!");
            if (qaq[1].contains(",")&&qaq[4].equals("inner")&&qaq[5].equals("join")&&qaq[7].equals("on"))
                isJoin = true;
        }

        String label = qaq[1];
        String tableName = qaq[3];
        if (isJoin){
            //TODO: join on
        }
        else//不是join
        {

            Table tempT = null;
                tempT = DataLoader.getTable(tableName);

            List<Attribute> tempA = tempT.getMeta().getAttributes();
            if (tempT == null || tempA == null)
                throw new BasicBusinessException("select error: No such table!");
            //不是join就只有一个table
            tables.add(tempT);

            if (qaq.length>4){
                //有where
                if (!qaq[4].equals("where"))
                    throw new BasicBusinessException("select error: Please enter 'where' before the conditions!");
                String[] conditions = Arrays.asList(qaq).subList(5, qaq.length).toArray(new String[]{});
                int index=-1;
                int count = 0; //判断condition符不符合格式

                for (int i = 0; i < conditions.length; i++) {
                    count += 1;
                    if (conditions[i].equals("and")){//and分割
                        index = i;
                        if (count !=4)
                            throw new BasicBusinessException("select error: Conditions misformat1!");
                        count = 0;
                    }
                    if (index == i) {//相等的时候说明现在是and，那么之前三个就是一个condition
                        int attrIndex = index-3, symbolIndex = index-2, valueIndex = index-1;
                        int flag = 0;
                        AttrVSValueCondition avvc = new AttrVSValueCondition();
                        for (int j = 0; j < tempA.size(); j++) {
                            if(conditions[attrIndex].equals(tempA.get(j).getName())){
                                //在tableMeta的Attributes里面找到了对应的属性
                                flag =1;
                                Set<Table> t = new HashSet<>();
                                t.add(tempT);
                                TableAttribute ta = new TableAttribute(t, tempA.get(j));
                                avvc.setFormerAttribute(ta);
                                break;
                            }
                        }
                        if (flag == 0)//找不到
                            throw new BasicBusinessException("select error: Not existed attribute!");

                        avvc.setFormerTable(tempT);
                        Element e = new Element();
                        e.setData(qaq[valueIndex]);
                        if (conditions[valueIndex].contains("'")||conditions[valueIndex].contains("‘"))
                            e.setType(DataTypeEnum.STRING.getType());
                        else {
                            if (conditions[valueIndex].contains("."))
                                e.setType(DataTypeEnum.FLOAT.getType());
                            else
                                e.setType(DataTypeEnum.INTEGER.getType());
                        }
                        avvc.setLatterDataElement(e);
                        if (!e.getType().equals(avvc.getFormerAttribute().getAttribute().getType()))
                            throw new BasicBusinessException("select error: The type of set-con value is not match!");

                        avvc.setCondition(Utils.judgeSymbol(conditions[symbolIndex]));
                        whereCondition.add(avvc);
                    }
                }
                if (count != 3)
                    throw new BasicBusinessException("select error: Conditions misformat2!");
                else {//and之后的那个condition
                    int attrIndex = conditions.length-3, symbolIndex = conditions.length-2, valueIndex = conditions.length-1;
                    int flag = 0;
                    AttrVSValueCondition avvc = new AttrVSValueCondition();
                    for (int j = 0; j < tempA.size(); j++) {
                        if(conditions[attrIndex].equals(tempA.get(j).getName())){
                            //在tableMeta的Attributes里面找到了对应的属性
                            flag =1;
                            Set<Table> t = new HashSet<>();
                            t.add(tempT);
                            TableAttribute ta = new TableAttribute(t, tempA.get(j));
                            avvc.setFormerAttribute(ta);
                            break;
                        }
                    }
                    if (flag == 0)//找不到
                        throw new BasicBusinessException("select error: Not existed attribute!");

                    avvc.setFormerTable(tempT);
                    Element e = new Element();
                    e.setData(conditions[valueIndex]);
                    if (conditions[valueIndex].contains("'")||conditions[valueIndex].contains("‘"))
                        e.setType(DataTypeEnum.STRING.getType());
                    else {
                        if (conditions[valueIndex].contains("."))
                            e.setType(DataTypeEnum.FLOAT.getType());
                        else
                            e.setType(DataTypeEnum.INTEGER.getType());
                    }
                    avvc.setLatterDataElement(e);
                    if (!e.getType().equals(avvc.getFormerAttribute().getAttribute().getType()))
                        throw new BasicBusinessException("select error: The type of condition value is not match!");

                    avvc.setCondition(Utils.judgeSymbol(conditions[symbolIndex]));
                    whereCondition.add(avvc);
                }
            }

            //后面就不用管有没有where了
            if (label.equals("*")){// select * from ...
                for (int i = 0; i < tempA.size(); i++) {
                    Set<Table> t = new HashSet<>();
                    t.add(tempT);
                    TableAttribute ta = new TableAttribute(t, tempA.get(i));
                    selectedAttributes.add(ta);
                }
            }
            else if (label.equals("count(*)"))
                throw new BasicBusinessException("select error: Not support 'count' !");
            else{// select ... from ...
                String[] selectAttrs = label.split(",");
                for (int i = 0; i < selectAttrs.length; i++) {
                    if (selectAttrs[i].contains("."))
                        throw new BasicBusinessException("select error: Invalid format!");
                    int flag = 0;
                    for (int j = 0; j < tempA.size(); j++) {
                        if (selectAttrs[i].equals(tempA.get(j).getName())){
                            flag =1;
                            Set<Table> t = new HashSet<>();
                            t.add(tempT);
                            TableAttribute ta = new TableAttribute(t, tempA.get(j));
                            selectedAttributes.add(ta);
                            break;
                        }
                    }
                    if (flag == 0)
                        throw new BasicBusinessException("select error: Not existed attribute2!");
                }
            }
        }
        RecordManager rm = new RecordManager();
        for (int i = 0; i < selectedAttributes.size(); i++) {
            System.out.println(selectedAttributes.get(i));
        }
        for (int i = 0; i < tables.size(); i++) {
            System.out.println(tables.get(i));
        }
        for (int i = 0; i < joinCondition.size(); i++) {
            System.out.println(joinCondition.get(i));
        }
        for (int i = 0; i < whereCondition.size(); i++) {
            System.out.println(whereCondition.get(i));
        }
        rm.select(selectedAttributes,tables,joinCondition,whereCondition,isAnd);
    }

    public static void update(String query) throws BasicBusinessException{
        // 只支持set条件和where条件只有一个
        System.out.println("Updating ...");
        query = query.replaceAll(" *\\( *", " (").replaceAll(" *\\) *", ") ");
        query = query.replaceAll(" *, *", ",");
        query = query.trim();
        String[] qaq = query.split(" ");

        if (qaq.length<10)
            throw new BasicBusinessException("update error: Invalid query!");
        if (!qaq[0].equals("update"))
            throw new BasicBusinessException("update error: Please enter 'update' first!");
        if (!qaq[2].equals("set"))
            throw new BasicBusinessException("update error: Please enter 'set' after the tableName!");
        if (!qaq[6].equals("where"))
            throw new BasicBusinessException("update error: Incorrect format! (Prompt: 'where' or the set-condition)");

        Table table = new Table();
        List<ICondition> setCondition = new ArrayList<>();
        List<ICondition> whereCondition = new ArrayList<>();
        boolean isAnd = true;

        String tableName = qaq[1];
            table = DataLoader.getTable(tableName);

        List<Attribute> tempA = table.getMeta().getAttributes();


        //setCondition
        int attrIndex = 3, symbolIndex = 4, valueIndex = 5;
        int flagSet = 0;
        AttrVSValueCondition setCon = new AttrVSValueCondition();
        for (int i = 0; i < tempA.size(); i++) {
            if(qaq[attrIndex].equals(tempA.get(i).getName())){
                flagSet =1;
                Set<Table> t = new HashSet<>();
                t.add(table);
                TableAttribute ta = new TableAttribute(t, tempA.get(i));
                setCon.setFormerAttribute(ta);
                break;
            }
        }
        if (flagSet == 0)
            throw new BasicBusinessException("select error: Not existed attribute1!");
        setCon.setFormerTable(table);
        Element e = new Element();
        e.setData(qaq[valueIndex]);
        if (qaq[valueIndex].contains("'")||qaq[valueIndex].contains("‘"))
            e.setType(DataTypeEnum.STRING.getType());
        else {
            if(qaq[valueIndex].contains("."))
                e.setType(DataTypeEnum.FLOAT.getType());
            else
                e.setType(DataTypeEnum.INTEGER.getType());
        }
        setCon.setLatterDataElement(e);
        if (!e.getType().equals(setCon.getFormerAttribute().getAttribute().getType()))
            throw new BasicBusinessException("select error: The type of set-con value is not match!");

        setCon.setCondition(Utils.judgeSymbol(qaq[symbolIndex]));
        setCondition.add(setCon);

        //whereCondition
        attrIndex = 7; symbolIndex = 8; valueIndex = 9;
        int flagWhere = 0;
        AttrVSValueCondition whereCon = new AttrVSValueCondition();
        for (int i = 0; i < tempA.size(); i++) {
            if(qaq[attrIndex].equals(tempA.get(i).getName())){
                flagWhere =1;
                Set<Table> t = new HashSet<>();
                t.add(table);
                TableAttribute ta = new TableAttribute(t, tempA.get(i));
                whereCon.setFormerAttribute(ta);
                break;
            }
        }
        if (flagWhere == 0)
            throw new BasicBusinessException("select error: Not existed attribute1!");

        whereCon.setFormerTable(table);
        Element el = new Element();
        el.setData(qaq[valueIndex]);

        if (qaq[valueIndex].contains("'")||qaq[valueIndex].contains("‘"))
            el.setType(DataTypeEnum.STRING.getType());
        else {
            if(qaq[valueIndex].contains("."))
                el.setType(DataTypeEnum.FLOAT.getType());
            else
                el.setType(DataTypeEnum.INTEGER.getType());
        }
        if (!el.getType().equals(whereCon.getFormerAttribute().getAttribute().getType()))
            throw new BasicBusinessException("select error: The type of where-con value is not match!");

        whereCon.setLatterDataElement(el);

        whereCon.setCondition(Utils.judgeSymbol(qaq[symbolIndex]));
        whereCondition.add(whereCon);

        RecordManager rm = new RecordManager();
        rm.update(table,setCondition,whereCondition,isAnd);
    }
}


class Utils{
    public static PredicateEnum judgeSymbol(String s){
        if (s.equals(">"))
            return LARGER;
        else if (s.equals("<"))
            return SMALLER;
        else if (s.equals(">="))
            return LARGERorEQUAL;
        else if (s.equals("<="))
            return SMALLERorEQUAL;
        else if (s.equals("="))
            return EQUAL;
        else
            return notEQUAL;
    }
}
