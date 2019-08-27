package io.seata.samples.integration.order.dubbo;

import com.alibaba.dubbo.config.annotation.Service;

import com.alibaba.fastjson.JSONObject;
import io.seata.core.context.RootContext;
import io.seata.samples.integration.common.dto.OrderDTO;
import io.seata.samples.integration.common.dubbo.OrderDubboService;
import io.seata.samples.integration.common.response.ObjectResponse;
import io.seata.samples.integration.order.service.ITOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: heshouyou
 * @Description
 * @Date Created in 2019/1/23 15:59
 */
@Service(version = "1.0.0",protocol = "${dubbo.protocol.id}",
		application = "${dubbo.application.id}",registry = "${dubbo.registry.id}",
		timeout = 3000)
public class OrderDubboServiceImpl implements OrderDubboService {

	private static final Logger logger = LoggerFactory.getLogger(OrderDubboServiceImpl.class);

    @Autowired
    private ITOrderService orderService;

    @Override
    public ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO) {
	    logger.info("创建订单参数[全局事务XID = {}]：{}", RootContext.getXID(), JSONObject.toJSONString(orderDTO));
        ObjectResponse res = orderService.createOrder(orderDTO);
	    logger.info("创建订单结果：{}", JSONObject.toJSONString(res));
        return res;
    }
}
