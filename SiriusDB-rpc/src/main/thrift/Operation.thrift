include "Base.thrift"
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
    1: required list<Table.VTable> tables,
    255: required Base.Base Base,
}

/**
 * 复制数据的请求结果
 */
struct ExecDataCopyResponse {
    255: required Base.BaseResp BaseResp,
}

/**
 * 根据表格名服务器请求相应表格数据
 */
struct QueryTableDataRequest {
    1: required list<string> tableName,
    255: required Base.Base base,
}

/**
 * 查询表格数据响应
 */
struct QueryTableDataResponse {
    1: optional list<Table.VTable> tableData,
    255: required Base.BaseResp baseResp
}

/**
 * 根据表格名查询相应表格元数据
 */
struct QueryTableMetaInfoRequest {
    1: required list<string> name,
    255: required Base.Base base
}

/**
 * 查询表格元数据响应
 */
struct QueryTableMetaInfoResponse {
    1: optional list<Table.VTableMeta> meta,
    255: required Base.BaseResp baseResp
}