package com.siriusdb.client.db.manager;

import com.siriusdb.enums.DataTypeEnum;
import com.siriusdb.model.db.Attribute;
import com.siriusdb.model.db.Index;
import com.siriusdb.model.db.Table;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @Auther: ylx
 * @Date: 2021/05/20/15:02
 * @Description: The module for Catalog Management.
 */
public class CatalogManager {

    /*
    private static LinkedHashMap<String, Table> tables = new LinkedHashMap<>();
    private static LinkedHashMap<String, Index> indexes = new LinkedHashMap<>();
    private static String tableFilename = "table_catalog";
    private static String indexFilename = "index_catalog";

    public static void initial_catalog() throws IOException {
        initial_table();
        initial_index();
    }

    private static void initial_table() throws IOException {
        File file = new File(tableFilename);
        if (!file.exists()) return;
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        String tmpTableName, tmpPrimaryKey;
        int tmpIndexNum, tmpAttributeNum;

        while (dis.available() > 0) {
            Vector<Attribute> tmpAttributeVector = new Vector<Attribute>();
            Vector<Index> tmpIndexVector = new Vector<Index>();
            tmpTableName = dis.readUTF();
            tmpPrimaryKey = dis.readUTF();

            tmpIndexNum = dis.readInt();
            for (int i = 0; i < tmpIndexNum; i++) {
                String tmpIndexName, tmpAttributeName;
                tmpIndexName = dis.readUTF();
                tmpAttributeName = dis.readUTF();
                tmpIndexVector.addElement(new Index(tmpIndexName, tmpTableName, tmpAttributeName));
            }

            tmpAttributeNum = dis.readInt();
            for (int i = 0; i < tmpAttributeNum; i++) {
                String tmpAttributeName, tmpIndexName, tmpTypeName;
                DataTypeEnum tmpType;
                //Table tmpTable;
                int tmpId;
                boolean tmpIsUnique, tmpHasIndex;
                tmpId = dis.readInt();
                tmpAttributeName = dis.readUTF();
                tmpTypeName = dis.readUTF();
                tmpHasIndex = dis.readBoolean();
                tmpIsUnique = dis.readBoolean();
                tmpIndexName = dis.readUTF();
                tmpType = DataTypeEnum.valueOf(tmpTypeName);
                tmpAttributeVector.addElement(new Attribute(tmpId, tmpAttributeName, tmpType.getType(), tmpHasIndex, tmpIsUnique));
            }
            tables.put(tmpTableName, new Table(tmpTableName, tmpPrimaryKey, tmpAttributeVector, tmpIndexVector));
        }
        dis.close();
    }

    private static void initial_index() throws IOException {
        File file = new File(indexFilename);
        if (!file.exists()) return;
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        String tmpIndexName, tmpTableName, tmpAttributeName;
        while (dis.available() > 0) {
            tmpIndexName = dis.readUTF();
            tmpTableName = dis.readUTF();
            tmpAttributeName = dis.readUTF();
            indexes.put(tmpIndexName, new Index(tmpIndexName, tmpTableName, tmpAttributeName));
        }
        dis.close();
    }

    public static void store_catalog() throws IOException {
        store_table();
        store_index();
    }

    private static void store_table() throws IOException {
        File file = new File(tableFilename);
        FileOutputStream fos = new FileOutputStream(file);
        DataOutputStream dos = new DataOutputStream(fos);
        Table tmpTable;
        Iterator<Map.Entry<String, Table>> iter = tables.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            tmpTable = (Table) entry.getValue();
            dos.writeUTF(tmpTable.getName());
            dos.writeUTF(tmpTable.getPrimaryKey());
            dos.writeInt(tmpTable.getAttributes().size());
            dos.writeInt(tmpTable.getIndexes().size());
            for (int i = 0; i < tmpTable.getIndexes().size(); i++) {
                Index tmpIndex = tmpTable.getIndexes().get(i);
                dos.writeUTF(tmpIndex.getIndexName());
                dos.writeUTF(tmpIndex.getAttributeName());
            }
            dos.writeInt(tmpTable.getAttributes().size());
            for (int i = 0; i < tmpTable.getAttributes().size(); i++) {
                Attribute tmpAttribute = tmpTable.getAttributes().get(i);
                dos.writeUTF(tmpAttribute.getName());
                dos.writeUTF(tmpAttribute.getType());
                //dos.writeInt(tmpAttribute.type.get_length());
                dos.writeBoolean(tmpAttribute.getIsUnique());
            }
        }
        dos.close();
    }

    private static void store_index() throws IOException {
        File file = new File(indexFilename);
        if (file.exists()) file.delete();
        FileOutputStream fos = new FileOutputStream(file);
        DataOutputStream dos = new DataOutputStream(fos);
        Index tmpIndex;
        //Enumeration<Index> en = indexes.elements();
        Iterator<Map.Entry<String, Index>> iter = indexes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            tmpIndex = (Index) entry.getValue();
            //tmpIndex = en.nextElement();
            dos.writeUTF(tmpIndex.getIndexName());
            dos.writeUTF(tmpIndex.getTableName());
            dos.writeUTF(tmpIndex.getAttributeName());
        }
        dos.close();
    }

    public static void show_catalog() {
        show_table();
        System.out.println();
        show_index();
    }

    public static void show_index() {
        Index tmpIndex;
        Iterator<Map.Entry<String, Index>> iter = indexes.entrySet().iterator();
        int idx = 5, tab = 5, attr = 9;
        //System.out.println("There are " + indexes.size() + " indexes in the database: ");
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            tmpIndex = (Index) entry.getValue();
            idx = tmpIndex.getIndexName().length() > idx ? tmpIndex.getIndexName().length() : idx;
            tab = tmpIndex.getTableName().length() > tab ? tmpIndex.getTableName().length() : tab;
            attr = tmpIndex.getAttributeName().length() > attr ? tmpIndex.getAttributeName().length() : attr;
        }
        String format = "|%-" + idx + "s|%-" + tab + "s|%-" + attr + "s|\n";
        iter = indexes.entrySet().iterator();
        System.out.printf(format, "INDEX", "TABLE", "ATTRIBUTE");
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            tmpIndex = (Index) entry.getValue();
            System.out.printf(format, tmpIndex.getIndexName(), tmpIndex.getTableName(), tmpIndex.getAttributeName());
        }

    }

    public static int get_max_attr_length(Table tab) {
        int len = 9;//the size of "ATTRIBUTE"
        for (int i = 0; i < tab.getAttributes().size(); i++) {
            int v = tab.getAttributes().get(i).getName().length();
            len = v > len ? v : len;
        }
        return len;
    }

    public static void show_table() {
        Table tmpTable;
        Attribute tmpAttribute;
        Iterator<Map.Entry<String, Table>> iter = tables.entrySet().iterator();
        //System.out.println("There are " + tables.size() + " tables in the database: ");
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            tmpTable = (Table) entry.getValue();
            //System.out.println("[TABLE] " + tmpTable.tableName);
            String format = "|%-" + get_max_attr_length(tmpTable) + "s";
            format += "|%-5s|%-6s|%-6s|\n";
            System.out.printf(format, "ATTRIBUTE", "TYPE",  "UNIQUE");
            for (int i = 0; i < tmpTable.getAttributes().size(); i++) {
                tmpAttribute = tmpTable.getAttributes().get(i);
                System.out.printf(format, tmpAttribute.getName(), tmpAttribute.getType(), tmpAttribute.getIsUnique());
            }
            if (iter.hasNext()) System.out.println("--------------------------------");
        }
    }

    public static Table get_table(String tableName) {
        return tables.get(tableName);
    }

    public static Index get_index(String indexName) {
        return indexes.get(indexName);
    }

    public static String get_primary_key(String tableName) {
        return get_table(tableName).getPrimaryKey();
    }

    public static int get_attribute_num(String tableName) {
        return get_table(tableName).getAttributes().size();
    }

    //check
    public static boolean is_primary_key(String tableName, String attributeName) {
        if (tables.containsKey(tableName)) {
            Table tmpTable = get_table(tableName);
            return tmpTable.getPrimaryKey().equals(attributeName);
        } else {
            System.out.println("The table " + tableName + " doesn't exist");
            return false;
        }
    }

    public static boolean is_unique(String tableName, String attributeName) {
        if (tables.containsKey(tableName)) {
            Table tmpTable = get_table(tableName);
            for (int i = 0; i < tmpTable.getAttributes().size(); i++) {
                Attribute tmpAttribute = tmpTable.getAttributes().get(i);
                if (tmpAttribute.getName().equals(attributeName)) {
                    return tmpAttribute.getIsUnique();
                }
            }
            //if (i >= tmpTable.attributeVector.size()) {
            System.out.println("The attribute " + attributeName + " doesn't exist");
            return false;
            //}
        }
        System.out.println("The table " + tableName + " doesn't exist");
        return false;

    }

    public static boolean is_index_key(String tableName, String attributeName) {
        if (tables.containsKey(tableName)) {
            Table tmpTable = get_table(tableName);
            if (is_attribute_exist(tableName, attributeName)) {
                for (int i = 0; i < tmpTable.getIndexes().size(); i++) {
                    if (tmpTable.getIndexes().get(i).getAttributeName().equals(attributeName))
                        return true;
                }
            } else {
                System.out.println("The attribute " + attributeName + " doesn't exist");
            }
        } else
            System.out.println("The table " + tableName + " doesn't exist");
        return false;
    }

    private static boolean is_index_exist(String indexName) {
        return indexes.containsKey(indexName);
    }

    private static boolean is_attribute_exist(String tableName, String attributeName) {
        Table tmpTable = get_table(tableName);
        for (int i = 0; i < tmpTable.getAttributes().size(); i++) {
            if (tmpTable.getAttributes().get(i).getName().equals(attributeName))
                return true;
        }
        return false;
    }

    public static String get_index_name(String tableName, String attributeName) {
        if (tables.containsKey(tableName)) {
            Table tmpTable = get_table(tableName);
            if (is_attribute_exist(tableName, attributeName)) {
                for (int i = 0; i < tmpTable.getIndexes().size(); i++) {
                    if (tmpTable.getIndexes().get(i).getAttributeName().equals(attributeName))
                        return tmpTable.getIndexes().get(i).getIndexName();
                }
            } else {
                System.out.println("The attribute " + attributeName + " doesn't exist");
            }
        } else
            System.out.println("The table " + tableName + " doesn't exist");
        return null;
    }

    public static String get_attribute_name(String tableName, int i) {
        return tables.get(tableName).getAttributes().get(i).getName();
    }

    public static int get_attribute_index(String tableName, String attributeName) {
        Table tmpTable = tables.get(tableName);
        Attribute tmpAttribute;
        for (int i = 0; i < tmpTable.getAttributes().size(); i++) {
            tmpAttribute = tmpTable.getAttributes().get(i);
            if (tmpAttribute.getName().equals(attributeName))
                return i;
        }
        System.out.println("The attribute " + attributeName + " doesn't exist");
        return -1;
    }

    public static String get_attribute_type(String tableName, String attributeName) {
        Table tmpTable = tables.get(tableName);
        Attribute tmpAttribute;
        for (int i = 0; i < tmpTable.getAttributes().size(); i++) {
            tmpAttribute = tmpTable.getAttributes().get(i);
            if (tmpAttribute.getName().equals(attributeName))
                return tmpAttribute.getType();
        }
        System.out.println("The attribute " + attributeName + " doesn't exist");
        return null;
    }

    public static String get_type(String tableName, int i) {
        //Table tmpTable=tables.get(tableName);
        return tables.get(tableName).getAttributes().get(i).getType();
    }

    public static boolean update_index_table(String indexName, Index tmpIndex) {
        indexes.replace(indexName, tmpIndex);
        return true;
    }

    public static boolean is_attribute_exist(Vector<Attribute> attributeVector, String attributeName) {
        for (int i = 0; i < attributeVector.size(); i++) {
            if (attributeVector.get(i).getName().equals(attributeName))
                return true;
        }
        return false;
    }

    //Interface
    public static boolean create_table(Table newTable) throws NullPointerException{
        tables.put(newTable.getName(), newTable);
        //indexes.put(newTable.indexes.firstElement().indexName, newTable.indexes.firstElement());
        return true;
    }

    public static boolean drop_table(String tableName) throws NullPointerException{
        Table tmpTable = tables.get(tableName);
        for (int i = 0; i < tmpTable.getIndexes().size(); i++) {
            indexes.remove(tmpTable.getIndexes().get(i).getIndexName());
        }
        tables.remove(tableName);
        return true;
    }

    public static boolean create_index(Index newIndex) throws NullPointerException{
        Table tmpTable = get_table(newIndex.getTableName());
        tmpTable.getIndexes().add(newIndex);
        indexes.put(newIndex.getIndexName(), newIndex);
        return true;
    }

    public static boolean drop_index(String indexName) throws NullPointerException{
        Index tmpIndex = get_index(indexName);
        Table tmpTable = get_table(tmpIndex.getTableName());
        tmpTable.getIndexes().remove(tmpIndex);
        indexes.remove(indexName);
        return true;
    }
    public static void hello() {
        System.out.println("Hello Client 1 !");
    }
     */
}
