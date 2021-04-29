include "shop.thrift"
include "audit_order_rule.thrift"
include "warehouse.thrift"
include "goods_sku.thrift"
include "express_rule.thrift"
namespace java com.siriusdb.rpc

service BasicService{
    /**
    * 根据shopNo查询店铺信息
    **/
    shop.QueryShopResponse queryShop(1: shop.QueryShopRequest req),
    /**
    * 根据shopNo查询虚拟仓信息
    **/
    shop.QueryVWarehouseResponse queryVWarehouse(1: shop.QueryVWarehouseRequest req),
    /**
     * 查询符合条件的审单规则
     **/
    audit_order_rule.AuditOrderRuleQueryResponse queryAuditOrderRules(1: audit_order_rule.AuditOrderRuleQueryRequest req),
    /**
    * 根据warehouseNo查询仓库信息
    **/
    warehouse.QueryWarehouseResponse queryWarehouse(1: warehouse.QueryWarehouseRequest req),
    /**
    * 根据skuNo查询sku信息
    **/
    goods_sku.QueryGoodsSkuResponse queryGoodsSku(1: goods_sku.QueryGoodsSkuRequest req),
    /**
    * 根据仓库编码查询快递规则
    **/
    express_rule.ExpressRuleQueryResponse queryExpressRule(1: express_rule.ExpressRuleQueryRequest req),
}