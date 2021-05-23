package com.siriusdb.client.db.manager;

import com.siriusdb.enums.DataTypeEnum;
import com.siriusdb.enums.PredicateEnum;
import com.siriusdb.model.db.*;
import com.siriusdb.client.db.manager.BPTree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

/**
 * @Auther: ylx
 * @Date: 2021/05/20/15:01
 * @Description: The module for Index Management.
 */
public class IndexManager<T> {

//    private static LinkedHashMap<String, BPTree<DataTypeEnum, Row>> BPTreeMap = new LinkedHashMap<>();
//
//    public IndexManager() {
//        //Nothing
//    }
//
//    public static Vector<Row> select(TableMeta tb, Index idx, Condition cond, DataTypeEnum key) throws IllegalArgumentException {
//        String tableName = tb.getName();
//        Integer attribute = idx.getAttribute();
//        int index = CatalogManager.get_attribute_index(tableName, attribute);
//        DataTypeEnum type = DataTypeEnum.valueOf(CatalogManager.get_type(tableName, index));
//
//        BPTree<DataTypeEnum, Row> tmpTree;
//
//        tmpTree = BPTreeMap.get(idx.getName());
//        return IndexManager.<Integer>satisfies_cond(tmpTree, cond.getCondition(), key);
//    }
//
//    public static void delete(TableMeta tb, Index idx, DataTypeEnum key) throws IllegalArgumentException {
//        String tableName = tb.getName();
//        Integer attribute = idx.getAttribute();
//        int index = CatalogManager.get_attribute_index(tableName, attribute);
//        DataTypeEnum type = DataTypeEnum.valueOf(CatalogManager.get_type(tableName, index));
//
//        BPTree<DataTypeEnum, Row> tmpTree;
//
//        tmpTree = BPTreeMap.get(idx.getName());
//        tmpTree.delete(key);
//
//    }
//
//    public static void insert(TableMeta tb, Index idx, DataTypeEnum key, Row value) throws IllegalArgumentException {
//        String tableName = tb.getName();
//        Integer attribute = idx.getAttribute();
//        int index = CatalogManager.get_attribute_index(tableName, attribute);
//        DataTypeEnum type = DataTypeEnum.valueOf(CatalogManager.get_type(tableName, index));
//
//        BPTree<DataTypeEnum, Row> tmpTree;
//
//        tmpTree = BPTreeMap.get(idx.getName());
//        tmpTree.insert(key, value);
//
//    }
//
//    public static void update(TableMeta tb, Index idx, DataTypeEnum key, Row value) throws IllegalArgumentException {
//        String tableName = tb.getName();
//        Integer attribute = idx.getAttribute();
//        int index = CatalogManager.get_attribute_index(tableName, attribute);
//        DataTypeEnum type = DataTypeEnum.valueOf(CatalogManager.get_type(tableName, index));
//
//        BPTree<DataTypeEnum, Row> tmpTree;
//
//        tmpTree = BPTreeMap.get(idx.getName());
//        tmpTree.update(key, value);
//
//    }
//
//    //returns a vector of Rowes which satisfy the condition
//    private static <K extends Comparable<? super K>> Vector<Row> satisfies_cond(BPTree<DataTypeEnum, Row> tree, PredicateEnum operator, DataTypeEnum key) throws IllegalArgumentException {
//        if (operator.equals("=")) {
//            return tree.findEq(key);
//        } else if (operator.equals("<>")) {
//            return tree.findNeq(key);
//        } else if (operator.equals(">")) {
//            return tree.findGreater(key);
//        } else if (operator.equals("<")) {
//            return tree.findLess(key);
//        } else if (operator.equals(">=")) {
//            return tree.findGeq(key);
//        } else if (operator.equals("<=")) {
//            return tree.findLeq(key);
//        } else { //undefined operator
//            throw new IllegalArgumentException();
//        }
//    }

    public static <T> List<BPTree<Element<T>, Row>> build_BPTree(Table tmpTable){
        ArrayList<BPTree<Element<T>, Row>> tmpTreeList = new ArrayList();
        for(int i = 0; i < tmpTable.getIndexes().size(); i++){
            BPTree<Element<T>, Row> tmpTree = new BPTree<>(i);
            for(int j = 0; j < tmpTable.getData().size(); j++){
                tmpTree.insert(tmpTable.getData().get(j).getElements().get(j), tmpTable.getData().get(j));
            }
            tmpTreeList.add(tmpTree);
        }
        return tmpTreeList;
    }

    public static boolean create_index(Index newIndex, Table tmpTable) throws NullPointerException{
        List<Index> list = tmpTable.getIndexes();
        list.add(newIndex);
        return true;
    }

    public static boolean drop_index(Index oldIndex, Table tmpTable) throws NullPointerException{
        List<Index> list = tmpTable.getIndexes();
        list.remove(oldIndex);
        return true;
    }

    public static void hello() {
        System.out.println("Hello Client 1 !");
    }
}
