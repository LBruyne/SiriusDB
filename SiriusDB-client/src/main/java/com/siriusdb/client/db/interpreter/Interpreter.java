
package com.siriusdb.client.db.interpreter;

/**
 * @Description: module for interpreter.
 * @author: Lluvia
 * @date: 2021/06/01 2:30 下午
 */

import com.siriusdb.client.db.manager.DataLoader;
import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.model.db.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            String line = "";
            StringBuilder input = new StringBuilder();
            // 处理输入
            while (true) {
                line = reader.readLine();
                if (line.toString().equals("quit")) {
                    System.out.println("See You Again.");
                    return;
                } else if (line.toString().equals("reset")) {
                    System.out.println("Reset Already.");
                    input.setLength(0);
                    reset = 1;
                    break;
                }
                if (line.toString().contains(";")) {
                    index = line.indexOf(";");
                    input.append(line, 0, index);
                    break;
                } else
                    input.append(line);
            }
            if (reset == 1)
                continue;

            String query = input.toString().toString().trim().replaceAll("\\s+", " ");
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
                if (!attrType.equals("int") && !attrType.equals("float") && !attrType.equals("string"))
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
        table.setMeta(tableMeta);

//        try {
//            DataLoader.createTable(table);
//        } catch (TException e) {
//            e.printStackTrace();
//        }

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
            int index=0;
            for (int i = 0; i < conditions.length; i++) {
                System.out.println(conditions[i]);
                if (conditions[i].equals("and"))
                    index = i;
                if(index == i){
                    // TODO: 生成条件condition
                }
            }
            // TODO: delete from tableName where conditions 的接口

        }
        else if (qaq.length == 3){
            // TODO: delete from tableName 的接口
        }
    }
    public static void select(String query) throws BasicBusinessException{
        System.out.println("Selecting ...");
    }
    public static void update(String query) throws BasicBusinessException{
        System.out.println("Updating ...");
    }
}
