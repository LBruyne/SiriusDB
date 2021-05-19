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