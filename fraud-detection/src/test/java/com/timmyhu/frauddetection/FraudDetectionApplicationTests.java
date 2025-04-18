package com.timmyhu.frauddetection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.timmyhu.frauddetection.controller.TransactionController;
import com.timmyhu.frauddetection.ruleengine.RuleEngineFacade;
import com.timmyhu.frauddetection.service.TransactionService;

@SpringBootTest
class FraudDetectionApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        // 验证应用上下文加载成功
        assertNotNull(context);
    }

    @Test
    void verifyMainComponents() {
        // 验证关键组件是否加载
        assertNotNull(context.getBean(TransactionController.class));
        assertNotNull(context.getBean(TransactionService.class));
        assertNotNull(context.getBean(RuleEngineFacade.class));
    }

    @Test
    void testApplicationStartup() {
        // 验证应用启动成功
        FraudDetectionApplication.main(new String[]{});
    }
}
