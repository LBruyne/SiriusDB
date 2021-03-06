include "Base.thrift"
include "Table.thrift"
namespace java com.siriusdb.thrift.model

/**
 * 请求体：向Master请求创建一个表格
 */
struct QueryCreateTableRequest {
    1: optional string name,        // table的名字
    255: required Base.Base base
}

/**
 * 响应体：向Master请求创建一个表格
 */
struct QueryCreateTableResponse {
    1: optional string locatedServerName,
    2: optional string locatedServerUrl,
    255: required Base.BaseResp baseResp
}

/**
 * 根据表格名服务器请求相应表格数据
 */
struct QueryTableDataRequest {
    1: optional list<string> tableNames,
    255: required Base.Base base,
}

/**
 * 查询表格数据响应
 */
struct QueryTableDataResponse {
    1: optional list<Table.VTable> tables,
    255: required Base.BaseResp baseResp
}

/**
 * 根据表格名查询相应表格元数据
 */
struct QueryTableMetaInfoRequest {
    1: optional list<string> name,
    255: required Base.Base base
}

/**
 * 查询表格元数据响应
 */
struct QueryTableMetaInfoResponse {
    1: optional list<Table.VTableMeta> meta,
    255: required Base.BaseResp baseResp
}

/**
 * Master告知Region当前状态请求
 */
struct NotifyStateRequest {
    1: optional i32 stateCode,
    2: optional string dualServerName,
    3: optional string dualServerUrl,
    255: required Base.Base base
}
/**
 * Master告知Region当前状态响应
 */
struct NotifyStateResponse {
    255: required Base.BaseResp baseResp
}

/**
 * Region告知Server TableMeta变化请求
 */
struct NotifyTableMetaChangeRequest {
    1: optional string tableName,
    2: optional i32 operationCode,
    3: optional Table.VTableMeta tableMeta
    255: required Base.Base base
}

/**
 * Region告知Master TableMeta变化响应
 */
struct NotifyTableMetaChangeResponse {
    255: required Base.BaseResp baseResp
}

/**
 * A告知B Table变化请求
 */
struct NotifyTableChangeRequest {
    1: optional list<string> tableNames,
    2: optional i32 operationCode,
    3: optional list<Table.VTable> tables,
    255: required Base.Base base
}

/**
 * A告知B Table变化响应
 */
struct NotifyTableChangeResponse {
    255: required Base.BaseResp baseResp
}

/**
 * master告知region表格复制请求
 */
struct ExecTableCopyRequest {
    1: optional string targetName,
    2: optional string targetUrl,
    3: optional list<string> tableNames,
    255: required Base.Base base
}

/**
 * master告知region表格复制响应
 */
struct ExecTableCopyResponse {
    255: required Base.BaseResp baseResp
}