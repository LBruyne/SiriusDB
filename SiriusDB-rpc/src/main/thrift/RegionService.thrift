include "Operation.thrift"

namespace java com.siriusdb.thrift.service

service RegionService {

    /**
     * 根据表格名查询表格实际数据
     */
    Operation.QueryTableDataResponse queryTableData(1: Operation.QueryTableDataRequest req),

    /**
     * 请求从A服务器到B服务器创建、删除、更新特定表格数据
     */
    Operation.NotifyTableChangeResponse notifyTableChange(1: Operation.NotifyTableChangeRequest req),

    /**
     * Master告知某服务器状态变化
     */
    Operation.NotifyStateResponse notifyStateChange(1: Operation.NotifyStateRequest req)

}