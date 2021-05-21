package com.siriusdb.master.rpc.client;

import com.siriusdb.common.UtilConstant;
import com.siriusdb.enums.ErrorCodeEnum;
import com.siriusdb.enums.RpcCodeEnum;
import com.siriusdb.exception.BasicBusinessException;
import com.siriusdb.model.db.Attribute;
import com.siriusdb.model.db.TableMeta;
import com.siriusdb.thrift.model.*;
import com.siriusdb.thrift.service.RegionService;
import com.siriusdb.utils.rpc.DynamicThriftClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: MasterServer的RPC客户端
 * @author: liuxuanming
 * @date: 2021/05/16 11:22 上午
 */
@Slf4j
public class RegionServiceClient extends DynamicThriftClient<RegionService.Client> {

    public RegionServiceClient(Class<RegionService.Client> ts, String ip, Integer port) {
        super(ts, ip, port);
    }

    public RegionServiceClient(Class<RegionService.Client> ts) {
        super(ts);
    }

    /**
     * 请求表格数据
     *
     * @param name     目标表格名称
     * @param receiver 目标服务器名称
     * @return
     */
    public List<TableMeta> queryTableMeta(List<String> name, String receiver) throws TException {
        if(name == null || name.size() == 0) throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求的名字列表为空");
        QueryTableMetaInfoRequest request = new QueryTableMetaInfoRequest()
                .setBase(new Base()
                        .setCaller(UtilConstant.getHostname())
                        .setReceiver(receiver))
                .setName(name);

        QueryTableMetaInfoResponse response = client.queryTableMetaInfo(request);

        List<TableMeta> result = null;
        if(response == null || response.getBaseResp() == null) {
            log.warn("向{}请求表格{}元数据结果为空", receiver ,name);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求元数据结果为空");
        } else if(response.getBaseResp().getCode() == RpcCodeEnum.FAIL.getCode()) {
            log.warn("向{}请求表格{}元数据失败", receiver, name);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求元数据失败");
        } else if(response.getBaseResp().getCode() == RpcCodeEnum.NOT_FOUND.getCode()){
            log.warn("向{}请求表格{}元数据未找到", receiver, name);
            throw new BasicBusinessException(ErrorCodeEnum.FAIL.getCode(), "请求的元数据未找到");
        } else {
            log.warn("在{}找到了表格{}的元数据", receiver, name);
            result = response.getMeta().stream().map(vTableMeta -> vTableMToTableM(vTableMeta)).collect(Collectors.toList());
        }
        return result;
    }

    private TableMeta vTableMToTableM(VTableMeta vTableMeta) {
        TableMeta tableMeta = new TableMeta();
        BeanUtils.copyProperties(vTableMeta, tableMeta);
        tableMeta.setAttributes(vTableMeta.getAttributes()
                .stream()
                .map(vAttr -> vAttrToAttr(vAttr)).collect(Collectors.toList()));
        return tableMeta;
    }

    private Attribute vAttrToAttr(VAttribute vAttribute) {
        Attribute attribute = new Attribute();
        BeanUtils.copyProperties(vAttribute, attribute);
        return attribute;
    }
}
