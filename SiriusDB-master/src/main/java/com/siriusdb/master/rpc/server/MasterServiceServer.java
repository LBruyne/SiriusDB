package com.siriusdb.master.rpc.server;

import com.siriusdb.common.UtilConstant;
import com.siriusdb.master.biz.zk.ServiceStrategyExecutor;
import com.siriusdb.model.db.Attribute;
import com.siriusdb.model.db.TableMeta;
import com.siriusdb.thrift.model.QueryTableMetaInfoResponse;
import com.siriusdb.thrift.model.VAttribute;
import com.siriusdb.thrift.model.VTableMeta;
import com.siriusdb.utils.rpc.DynamicThriftServer;
import com.siriusdb.utils.rpc.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.BeanUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: MasterServer的服务端
 * @author: liuxuanming
 * @date: 2021/05/16 11:24 上午
 */
@Slf4j
public class MasterServiceServer extends DynamicThriftServer {
    public MasterServiceServer(TProcessor processor, Integer port) throws TTransportException {
        super(processor, port);
    }

    public MasterServiceServer(TProcessor processor) throws TTransportException {
        super(processor);
    }

    public static QueryTableMetaInfoResponse queryTableMeta(List<String> names) {
        QueryTableMetaInfoResponse response = null;

        // 这里是真正的业务逻辑
        if (names == null || names.size() == 0) {
            // 对参数进行验证
            log.warn("请求参数有误");
            response = new QueryTableMetaInfoResponse()
                    .setMeta(null)
                    .setBaseResp(RpcResult.failResp());
        } else if (names.size() == 1 && names.get(0).equals(UtilConstant.ALL_TABLE)) {
            // 请求所有表格的元数据
            log.warn("请求所有表格的元数据");
            List<VTableMeta> result = ServiceStrategyExecutor.DataHolder.tableMetaList
                    .stream()
                    .map(table -> tableMToVTableM(table)).collect(Collectors.toList());
            response = new QueryTableMetaInfoResponse()
                    .setMeta(result)
                    .setBaseResp(RpcResult.successResp());
        } else {
            // 请求特定表格的数据
            log.warn("请求表格{}的元数据", names);
            List<VTableMeta> result = new LinkedList<>();
            for (String name : names) {
                // 对每个表格名称进行查询
                TableMeta table = ServiceStrategyExecutor.DataHolder.findTable(name);
                if (table == null) {
                    // 发现不存在的表格名，返回错误信息
                    log.warn("查询表格{}失败，该表格不存在", name);
                    return new QueryTableMetaInfoResponse()
                            .setMeta(null)
                            .setBaseResp(RpcResult.failResp());
                } else {
                    // 该表格存在
                    log.warn("查询表格{}成功", name);
                    result.add(tableMToVTableM(table));
                }
            }
            response = new QueryTableMetaInfoResponse()
                    .setMeta(result)
                    .setBaseResp(RpcResult.successResp());
        }
        return response;
    }

    private static VTableMeta tableMToVTableM(TableMeta table) {
//        // 比较复杂的办法，一个个属性复制
//        VTableMeta vtable = new VTableMeta();
//        // 复制name属性
//        vtable.setName(table.getName());
//        // 复制primaryKey属性
//        vtable.setPrimaryKey(table.getPrimaryKey());
//        // 复制locatedServerName属性
//        vtable.setLocatedServerName(table.getLocatedServerName());
//        // 复制attributes属性
//        List<VAttribute> vAttributes = new LinkedList<>();
//        for (Attribute attribute : table.getAttributes()) {
//            VAttribute vAttribute = new VAttribute();
//            vAttribute.setId(attribute.getId());
//            vAttribute.setName(attribute.getName());
//            vAttribute.setType(attribute.getType());
//            vAttributes.add(vAttribute);
//        }
//        vtable.setAttributes(vAttributes);
//        return vtable;

        // 比较简单的办法，用BeanUtils进行属性拷贝
        VTableMeta vtable = new VTableMeta();
        // 公共属性复制：要求属性类型、属性名称一致
        BeanUtils.copyProperties(table, vtable);
        // 复制attributes属性
        vtable.setAttributes( table.getAttributes().stream().map(attribute -> attrToVAttr(attribute)).collect(Collectors.toList()) );
        // TODO 可能有其他信息
        return vtable;
    }

    private static VAttribute attrToVAttr(Attribute attribute) {
        VAttribute vAttribute = new VAttribute();
        BeanUtils.copyProperties(attribute, vAttribute);
        // TODO 可能有其他信息
        return vAttribute;
    }

}
