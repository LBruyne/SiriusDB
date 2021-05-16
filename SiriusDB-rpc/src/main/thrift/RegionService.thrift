include "Table.thrift"
include "TableMeta.thrift"
include "Operation.thrift"

namespace java com.siriusdb.thrift.service

service RegionServerService {

    /**
     * 根据表格名查询表格实际数据
     */
    Table.QueryTableDataResponse queryTableData(1: Table.QueryTableDataResponse req),

    /**
     * 根据表格名查询表格元数据
     */
    TableMeta.QueryTableMetaInfoResponse queryTableMetaInfo(1: TableMeta.QueryTableMetaInfoRequest req),

    /**
     * 请求从A服务器到B服务器复制特定表格的数据
     */
    Operation.QueryDataCopyResponse queryDataCopy(1: Operation.QueryDataCopyRequest req),

    /**
     * 执行从A服务器到B服务器复制特定表格的数据
     */
    Operation.ExecDataCopyRequest execDataCopy(1: Operation.ExecDataCopyResponse req)

}