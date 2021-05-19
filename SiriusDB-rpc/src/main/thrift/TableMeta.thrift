include "Base.thrift"
namespace java com.siriusdb.thrift.model

/**
 * 表格中每一列（属性）的数据结构
 */
struct VAttribute {
    1: required i32 id,
    2: required string name,
    3: required string type
}

/**
 * 表格元数据的数据结构
 */
struct VTableMeta {
    1: required string name,
    2: required string primaryKey,
    3: required list<VAttribute> attributes,
    4: required string locatedServerName
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
    1: optional list<VTableMeta> meta,
    255: required Base.BaseResp baseResp
}