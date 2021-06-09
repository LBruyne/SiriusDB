package com.siriusdb.model.region;

import com.siriusdb.common.MasterConstant;
import com.siriusdb.common.UtilConstant;
import com.siriusdb.enums.DataServerStateEnum;
import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.model.db.Table;
import com.siriusdb.thrift.model.VTable;
import com.siriusdb.thrift.model.VTableMeta;
import com.siriusdb.utils.copy.CopyUtils;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Slf4j
public class FileServer implements Serializable {

    private List<String> fileList;

    public List<VTable> readFile(
            DataServerStateEnum state,String name,String url) {
        List<VTable> tableList = new ArrayList<>();
        List<String> tableName1 = new ArrayList<String>();
        if (fileList.get(0).equals(UtilConstant.ALL_TABLE)) {
            File file1 = new File(this.getClass().getResource("/").getPath());
            File file2 = new File(file1.getParent());
            File file3 = new File(file2.getParent());
            File file = new File(file3.getParent());
            File[] tempList = file.listFiles();
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile() && tempList[i].getName().contains(UtilConstant.getHostname()) && !tempList[i].getName().contains("dualMachine")) {
                    tableName1.add(tempList[i].getName());
                    log.warn(tempList[i].getName());
                }
                if (tempList[i].isDirectory()) {
                }
            }
        } else {
            tableName1.addAll(fileList);
        }
        log.warn("Fileserver:此次添加的表名tablenames:{}",tableName1);
        for (int i = 0; i < tableName1.size(); i++) {
            Table tableTmp = null;
            VTable vtableTmp = null;
            File file = new File(tableName1.get(i));
            if (!file.exists()) {
                continue;
            }
            FileInputStream in;
            try {
                in = new FileInputStream(file);
                ObjectInputStream objIn = new ObjectInputStream(in);
                tableTmp = (Table) objIn.readObject();
                objIn.close();
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
            vtableTmp = CopyUtils.tableToVTable(tableTmp);
            VTableMeta vTableMeta = vtableTmp.getMeta();
            if(state == DataServerStateEnum.PRIMARY) {
                vtableTmp.setMeta(new VTableMeta()
                        .setAttributes(vTableMeta.getAttributes())
                        .setLocatedServerName(UtilConstant.getHostname())
                        .setLocatedServerUrl(UtilConstant.HOST_URL)
                        .setName(vTableMeta.getName())
                        .setPrimaryKey(vTableMeta.getPrimaryKey()));
            }
            else if(state == DataServerStateEnum.COPY){
                vtableTmp.setMeta(new VTableMeta()
                        .setAttributes(vTableMeta.getAttributes())
                        .setLocatedServerName(name)
                        .setLocatedServerUrl(url)
                        .setName(vTableMeta.getName())
                        .setPrimaryKey(vTableMeta.getPrimaryKey()));
            }
            log.warn("Fileserver:此次添加的表为:{}",vtableTmp);
            tableList.add(vtableTmp);
        }
        return tableList;
    }

}
