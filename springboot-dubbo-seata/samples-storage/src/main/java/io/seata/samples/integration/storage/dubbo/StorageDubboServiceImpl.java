package io.seata.samples.integration.storage.dubbo;

import com.alibaba.dubbo.config.annotation.Service;

import com.alibaba.fastjson.JSONObject;
import io.seata.core.context.RootContext;
import io.seata.samples.integration.common.dto.CommodityDTO;
import io.seata.samples.integration.common.dubbo.StorageDubboService;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.storage.service.ITStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/23 16:13
 */
@Service(version = "1.0.0",protocol = "${dubbo.protocol.id}",
        application = "${dubbo.application.id}",registry = "${dubbo.registry.id}",
        timeout = 5000)
public class StorageDubboServiceImpl implements StorageDubboService {

    private static final Logger logger = LoggerFactory.getLogger(StorageDubboServiceImpl.class);

    @Autowired
    private ITStorageService storageService;

    @Override
    public ObjectResponse decreaseStorage(CommodityDTO commodityDTO) {
        logger.info("扣减库存参数[全局事务XID = {}]：{}", RootContext.getXID(), JSONObject.toJSONString(commodityDTO));
        ObjectResponse res = storageService.decreaseStorage(commodityDTO);
        logger.info("扣减库存结果：{}", JSONObject.toJSONString(res));
        return res;
    }
}
