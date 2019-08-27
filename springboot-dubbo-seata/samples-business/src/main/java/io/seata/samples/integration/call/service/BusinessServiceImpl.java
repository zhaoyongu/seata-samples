package io.seata.samples.integration.call.service;

import com.alibaba.dubbo.config.annotation.Reference;

import com.alibaba.fastjson.JSONObject;
import io.seata.core.context.RootContext;
import io.seata.samples.integration.common.dto.BusinessDTO;
import io.seata.samples.integration.common.dto.CommodityDTO;
import io.seata.samples.integration.common.dto.OrderDTO;
import io.seata.samples.integration.common.dubbo.OrderDubboService;
import io.seata.samples.integration.common.dubbo.StorageDubboService;
import io.seata.samples.integration.common.enums.RspStatusEnum;
import io.seata.samples.integration.common.exception.DefaultException;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: heshouyou
 * @Description  Dubbo业务发起方逻辑
 * @Date Created in 2019/1/14 18:36
 */
@Service
public class BusinessServiceImpl implements BusinessService{

	private static final Logger logger = LoggerFactory.getLogger(BusinessServiceImpl.class);

    @Reference(version = "1.0.0", check = false)
    private StorageDubboService storageDubboService;

    @Reference(version = "1.0.0", check = false)
    private OrderDubboService orderDubboService;

    private boolean flag;

    /**
     * 处理业务逻辑
     * @Param:
     * @Return:
     */
    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    public ObjectResponse handleBusiness(BusinessDTO businessDTO) {
        System.out.println("开始全局事务XID = " + RootContext.getXID());
        ObjectResponse<Object> objectResponse = new ObjectResponse<>();
        //1、扣减库存
        CommodityDTO commodityDTO = new CommodityDTO();
        commodityDTO.setCommodityCode(businessDTO.getCommodityCode());
        commodityDTO.setCount(businessDTO.getCount());
	    logger.info("扣减库存参数：{}", JSONObject.toJSONString(commodityDTO));
        ObjectResponse storageResponse = storageDubboService.decreaseStorage(commodityDTO);
	    logger.info("扣减库存结果：{}", JSONObject.toJSONString(storageResponse));
        //2、创建订单
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(businessDTO.getUserId());
        orderDTO.setCommodityCode(businessDTO.getCommodityCode());
        orderDTO.setOrderCount(businessDTO.getCount());
        orderDTO.setOrderAmount(businessDTO.getAmount());
	    logger.info("创建订单参数：{}", JSONObject.toJSONString(commodityDTO));
        ObjectResponse<OrderDTO> response = orderDubboService.createOrder(orderDTO);
	    logger.info("创建订单结果：{}", JSONObject.toJSONString(response));

        //打开注释测试事务发生异常后，全局回滚功能
	    flag = true;
        if (flag) {
            throw new RuntimeException("测试抛异常后，分布式事务回滚！");
        }

        if (storageResponse.getStatus() != 200 || response.getStatus() != 200) {
	        logger.info("返回结果失败，准备抛出异常...");
            throw new DefaultException(RspStatusEnum.FAIL);
        }

        objectResponse.setStatus(RspStatusEnum.SUCCESS.getCode());
        objectResponse.setMessage(RspStatusEnum.SUCCESS.getMessage());
        objectResponse.setData(response.getData());
        return objectResponse;
    }
}
