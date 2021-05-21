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
 * 表格中每一列（属性）的数据结构
 */
struct VAttribute {
    1: required i32 id,
    2: required string name,
    3: required string type
}

/**
 * 表格中的索引(Index)
 */
struct VIndex{
    1: required string name,
    2: required i32 attribute
}

/**
 * 表格元数据的数据结构
 */
struct VTableMeta {
    1: required string name,
    2: required string primaryKey,
    3: required list<VAttribute> attributes,
    4: required string locatedServerName,
    5: required string locatedServerUrl
}

/**
 * 表格数据的数据结构
 */
struct VTable {
    1: required VTableMeta meta,
    2: required list<VIndex> indexes,
    3: required list<VRow> data
}

