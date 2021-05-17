include "Base.thrift"
namespace java com.siriusdb.thrift.model

/**
 * 表格中每一列（属性）的数据结构
 */
struct ColumnMetaInfo {
    1: required i32 id,
    2: required string name,
    3: required string type
}

/**
 * 表格元数据的数据结构
 */
struct TableMetaInfoDetail {
    1: required i32 id,
    2: required string name,
    3: required string primaryKey,
    4: required list<ColumnMetaInfo> columnList,
    5: required string locatedServerName
}

/**
 * 根据表格名查询相应表格元数据
 */
struct QueryTableMetaInfoRequest {
    2: required list<string> tableName,
    255: required Base.Base base
}

/**
 * 查询表格元数据响应
 */
struct QueryTableMetaInfoResponse {
    1: optional list<TableMetaInfoDetail> tableMetaInfo,
    255: required Base.BaseResp baseResp
}