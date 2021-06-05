package com.siriusdb.model.region;

import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.model.db.Table;
import com.siriusdb.thrift.model.VTable;
import com.siriusdb.utils.copy.CopyUtils;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class FileServer {

    private List<String> fileList;

    public List<VTable> readFile(){
        List<VTable> tableList = new ArrayList<>();
        List<String> tableName1 = new ArrayList<String>();
        if(fileList.get(0) == "ALL_TABLE"){
            File file = new File(this.getClass().getResource("").getPath());
            File[] tempList = file.listFiles();
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile()) {
                    tableName1.add(tempList[i].toString());
                }
                if (tempList[i].isDirectory()) {
                }
            }
        }
        else{
            tableName1.addAll(fileList);
        }
        for(int i=0;i<tableName1.size();i++) {
            Table tableTmp = null;
            VTable vtableTmp = null;
            File file = new File(tableName1.get(i) + ".dat");
            FileInputStream in;
            try {
                in = new FileInputStream(file);
                ObjectInputStream objIn = new ObjectInputStream(in);
                tableTmp = (Table) objIn.readObject();
                objIn.close();
            } catch (Exception e) {
            }
            vtableTmp = CopyUtils.tableToVTable(tableTmp);
            tableList.add(vtableTmp);
        }
        return tableList;
    }

}
