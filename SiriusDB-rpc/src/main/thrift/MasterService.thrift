include "Operation.thrift"

namespace java com.siriusdb.thrift.service

service MasterService {

    /**
     * 根据表格名查询表格元数据
     */
    Operation.QueryTableMetaInfoResponse queryTableMeta(1: Operation.QueryTableMetaInfoRequest req),

}