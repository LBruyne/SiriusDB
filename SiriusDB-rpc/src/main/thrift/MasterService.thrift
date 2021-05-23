include "Operation.thrift"

namespace java com.siriusdb.thrift.service

service MasterService {

    /**
     * 根据表格名查询表格元数据
     */
    Operation.QueryTableMetaInfoResponse queryTableMeta(1: Operation.QueryTableMetaInfoRequest req),

    /**
     * Region向Master通知TableMeta的新建、删除、修改
     */
    Operation.NotifyTableMetaChangeResponse notifyTableMetaChange(1: Operation.NotifyTableMetaChangeRequest req),

    /**
     * 请求创建一个表格，根据负载均衡返回位置
     */
    Operation.QueryCreateTableResponse queryCreateTable(1: Operation.QueryCreateTableRequest req)

}