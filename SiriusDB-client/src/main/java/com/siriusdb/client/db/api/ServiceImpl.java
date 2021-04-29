package com.siriusdb.client.db.api;

import com.siriusdb.client.db.manager.CatalogManager;
import com.siriusdb.client.db.manager.IndexManager;
import com.siriusdb.client.db.manager.RecordManager;

import javax.annotation.Resource;

/**
 * @Description: 对公共接口的实现示例
 * @author: liuxuanming
 * @date: 2021/04/29 4:47 下午
 */
public class ServiceImpl implements IService {

    private static CatalogManager catalogManager;

    @Resource
    private IndexManager indexManager;

    @Resource
    private RecordManager recordManager;

    public static void DemoMethod1() {
        catalogManager.hello();
    }

    public void DemoMethod2() {
        indexManager.hello();
    }

}
