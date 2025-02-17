package io.seata.samples.integration.account.dubbo;

import com.alibaba.dubbo.config.annotation.Service;

import com.alibaba.fastjson.JSONObject;
import io.seata.core.context.RootContext;
import io.seata.samples.integration.account.service.ITAccountService;
import io.seata.samples.integration.common.dto.AccountDTO;
import io.seata.samples.integration.common.dubbo.AccountDubboService;
import io.seata.samples.integration.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: heshouyou
 * @Description  Dubbo Api Impl
 * @Date Created in 2019/1/23 14:40
 */
@Service(version = "1.0.0",protocol = "${dubbo.protocol.id}",
         application = "${dubbo.application.id}",registry = "${dubbo.registry.id}",
         timeout = 5000)
public class AccountDubboServiceImpl implements AccountDubboService {

    private static final Logger logger = LoggerFactory.getLogger(AccountDubboServiceImpl.class);

    @Autowired
    private ITAccountService accountService;

    @Override
    public ObjectResponse decreaseAccount(AccountDTO accountDTO) {
        logger.info("扣减资产参数[全局事务XID = {}]：{}", RootContext.getXID(), JSONObject.toJSONString(accountDTO));
        ObjectResponse res = accountService.decreaseAccount(accountDTO);
        logger.info("扣减资产结果：{}", JSONObject.toJSONString(res));
        return res;
    }
}
