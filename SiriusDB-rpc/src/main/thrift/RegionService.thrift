include "Operation.thrift"

namespace java com.siriusdb.thrift.service

service RegionService {

    /**
     * 根据表格名查询表格实际数据
     */
    Operation.QueryTableDataResponse queryTableData(1: Operation.QueryTableDataRequest req),

    /**
     * 根据表格名查询表格元数据
     */
    Operation.QueryTableMetaInfoResponse queryTableMetaInfo(1: Operation.QueryTableMetaInfoRequest req),

    /**
     * 请求从A服务器到B服务器复制特定表格的数据
     */
    Operation.QueryDataCopyResponse queryDataCopy(1: Operation.QueryDataCopyRequest req),

    /**
     * 执行从A服务器到B服务器复制特定表格的数据
     */
    Operation.ExecDataCopyResponse execDataCopy(1: Operation.ExecDataCopyRequest req)

}