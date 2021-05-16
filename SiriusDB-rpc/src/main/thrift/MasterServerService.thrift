include "Table.thrift"
include "TableMeta.thrift"

namespace java com.siriusdb.thrift.service

service MasterServerService {

    /**
     * 根据表格名查询表格元数据
     */
    TableMeta.QueryTableMetaInfoResponse queryTableMeta(1: TableMeta.QueryTableMetaInfoRequest req),

}