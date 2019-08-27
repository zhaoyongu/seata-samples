package io.seata.samples.integration.call.controller;

import io.seata.samples.integration.call.service.BusinessService;
import io.seata.samples.integration.common.dto.BusinessDTO;
import io.seata.samples.integration.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: heshouyou
 * @Description  Dubbo业务执行入口
 * @Date Created in 2019/1/14 17:15
 */
@RestController
@RequestMapping("/business/dubbo")
@Slf4j
public class BusinessController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessController.class);


    @Autowired
    private BusinessService businessService;

    /**
     * 模拟用户购买商品下单业务逻辑流程
     * @Param:
     * @Return:
     */
    @PostMapping("/buy")
    ObjectResponse handleBusiness(@RequestBody BusinessDTO businessDTO){
        LOGGER.info("模拟用户下单请求参数：{}",businessDTO.toString());
        Assert.notNull(businessDTO, "参数businessDTO为空");
        Assert.notNull(businessDTO.getCommodityCode(), "参数businessDTO为空");
        Assert.notNull(businessDTO.getCount(), "参数businessDTO.count为空");
        Assert.notNull(businessDTO.getAmount(), "参数businessDTO.amount为空");
        Assert.notNull(businessDTO.getUserId(), "参数businessDTO.userId为空");
        Assert.notNull(businessDTO.getName(), "参数businessDTO.name为空");
        return businessService.handleBusiness(businessDTO);
    }
}
