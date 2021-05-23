package com.siriusdb.client.db.manager;

import com.siriusdb.enums.DataTypeEnum;
import com.siriusdb.enums.PredicateEnum;
import com.siriusdb.model.db.*;

import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * @Auther: ylx
 * @Date: 2021/05/20/15:01
 * @Description: The module for Index Management.
 */
public class IndexManager {

    private static LinkedHashMap<String, BPTree<DataTypeEnum, Row>> BPTreeMap = new LinkedHashMap<>();

    public IndexManager() {
        //Nothing
    }

    public static Vector<Row> select(TableMeta tb, Index idx, Condition cond, DataTypeEnum key) throws IllegalArgumentException {
        String tableName = tb.getName();
        Integer attribute = idx.getAttribute();
        int index = CatalogManager.get_attribute_index(tableName, attribute);
        DataTypeEnum type = DataTypeEnum.valueOf(CatalogManager.get_type(tableName, index));

        BPTree<DataTypeEnum, Row> tmpTree;

        tmpTree = BPTreeMap.get(idx.getName());
        return IndexManager.<Integer>satisfies_cond(tmpTree, cond.getCondition(), key);
    }

    public static void delete(TableMeta tb, Index idx, DataTypeEnum key) throws IllegalArgumentException {
        String tableName = tb.getName();
        Integer attribute = idx.getAttribute();
        int index = CatalogManager.get_attribute_index(tableName, attribute);
        DataTypeEnum type = DataTypeEnum.valueOf(CatalogManager.get_type(tableName, index));

        BPTree<DataTypeEnum, Row> tmpTree;

        tmpTree = BPTreeMap.get(idx.getName());
        tmpTree.delete(key);

    }

    public static void insert(TableMeta tb, Index idx, DataTypeEnum key, Row value) throws IllegalArgumentException {
        String tableName = tb.getName();
        Integer attribute = idx.getAttribute();
        int index = CatalogManager.get_attribute_index(tableName, attribute);
        DataTypeEnum type = DataTypeEnum.valueOf(CatalogManager.get_type(tableName, index));

        BPTree<DataTypeEnum, Row> tmpTree;

        tmpTree = BPTreeMap.get(idx.getName());
        tmpTree.insert(key, value);

    }

    public static void update(TableMeta tb, Index idx, DataTypeEnum key, Row value) throws IllegalArgumentException {
        String tableName = tb.getName();
        Integer attribute = idx.getAttribute();
        int index = CatalogManager.get_attribute_index(tableName, attribute);
        DataTypeEnum type = DataTypeEnum.valueOf(CatalogManager.get_type(tableName, index));

        BPTree<DataTypeEnum, Row> tmpTree;

        tmpTree = BPTreeMap.get(idx.getName());
        tmpTree.update(key, value);

    }

    //returns a vector of Rowes which satisfy the condition
    private static <K extends Comparable<? super K>> Vector<Row> satisfies_cond(BPTree<DataTypeEnum, Row> tree, PredicateEnum operator, DataTypeEnum key) throws IllegalArgumentException {
        if (operator.equals("=")) {
            return tree.findEq(key);
        } else if (operator.equals("<>")) {
            return tree.findNeq(key);
        } else if (operator.equals(">")) {
            return tree.findGreater(key);
        } else if (operator.equals("<")) {
            return tree.findLess(key);
        } else if (operator.equals(">=")) {
            return tree.findGeq(key);
        } else if (operator.equals("<=")) {
            return tree.findLeq(key);
        } else { //undefined operator
            throw new IllegalArgumentException();
        }
    }

    public static void hello() {
        System.out.println("Hello Client 1 !");
    }
}
