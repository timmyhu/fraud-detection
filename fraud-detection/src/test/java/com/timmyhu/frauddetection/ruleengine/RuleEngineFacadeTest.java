package com.timmyhu.frauddetection.ruleengine;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.timmyhu.frauddetection.model.Transaction;

@SpringBootTest
class RuleEngineFacadeTest {

    @Autowired
    private RuleEngineFacade ruleEngineFacade;

    @MockBean(name = "amountFraudRule")
    private FraudRule amountRule;

    @MockBean(name = "accountAbnormalityRule") 
    private FraudRule accountRule;

    @BeforeEach
    void setUp() {
        // Default mock behavior
        when(amountRule.isTransactionFraudulent(any())).thenReturn(false);
        when(accountRule.isTransactionFraudulent(any())).thenReturn(false);
    }

    @Test
    void testNoFraudDetected() {
        Transaction transaction = new Transaction();
        transaction.setFromAccount("account1");
        transaction.setToAccount("account2");
        transaction.setAmount(100);
        when(amountRule.isTransactionFraudulent(transaction)).thenReturn(false);
        when(accountRule.isTransactionFraudulent(transaction)).thenReturn(false);

        assertFalse(ruleEngineFacade.detectFraud(transaction));
    }

    @Test
    void testAmountFraudDetected() {
        Transaction transaction = new Transaction();
        when(amountRule.isTransactionFraudulent(transaction)).thenReturn(true);
        when(accountRule.isTransactionFraudulent(transaction)).thenReturn(false);

        assertTrue(ruleEngineFacade.detectFraud(transaction));
    }

    @Test
    void testAccountFraudDetected() {
        Transaction transaction = new Transaction();
        when(amountRule.isTransactionFraudulent(transaction)).thenReturn(false);
        when(accountRule.isTransactionFraudulent(transaction)).thenReturn(true);

        assertTrue(ruleEngineFacade.detectFraud(transaction));
    }

    @Test
    void testBothFraudsDetected() {
        Transaction transaction = new Transaction();
        when(amountRule.isTransactionFraudulent(transaction)).thenReturn(true);
        when(accountRule.isTransactionFraudulent(transaction)).thenReturn(true);

        assertTrue(ruleEngineFacade.detectFraud(transaction));
    }
}
