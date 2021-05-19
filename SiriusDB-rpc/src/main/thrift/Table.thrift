include "Base.thrift"
namespace java com.siriusdb.thrift.model

/**
 * 数据表中每一行的数据结构
 */
struct VRow {
    1: required list<VElement> elements
}

/**
 * 数据表中每个数据项的数据结构
 */
struct VElement {
    1: required i32 columnId,
    2: required string type,
    3: required string data
}

/**
 * 表格元数据的数据结构
 */
struct VTable {
    1: required string name,
    2: required list<VRow> rows
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
    1: optional list<VTable> tableData,
    255: required Base.BaseResp baseResp
}