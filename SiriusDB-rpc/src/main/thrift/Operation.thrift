include "Base.thrift"
include "TableMeta.thrift"
include "Table.thrift"
namespace java com.siriusdb.thrift.model

/**
 * 请求从source服务器向target服务器发起数据复制
 */
struct QueryDataCopyRequest {
    1: required string source,
    2: required list<string> target,
    3: required list<string> tableName,
    255: required Base.Base Base
}

/**
 * 从source服务器向target服务器发起数据复制操作的请求结果
 */
struct QueryDataCopyResponse {
    255: required Base.BaseResp BaseResp
}

/**
 * RegionServer执行复制请求，将一个服务器的数据复制到另一个服务器上
 */
struct ExecDataCopyRequest {
    1: required list<Table.TableDataDetail> tables,
    2: required TableMeta.TableMetaInfoDetail meta,
    255: required Base.Base Base,
}

/**
 * 复制数据的请求结果
 */
struct ExecDataCopyResponse {
    255: required Base.BaseResp BaseResp,
}
