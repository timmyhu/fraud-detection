package com.timmyhu.frauddetection.ruleengine.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.timmyhu.frauddetection.model.Transaction;

class AccountAbnormalityRuleTest {

    private AccountAbnormalityRule rule = new AccountAbnormalityRule();

    @Test
    void testNormalAccounts() {
        Transaction transaction = new Transaction();
        transaction.setFromAccount("normal1");
        transaction.setToAccount("normal2");
        assertFalse(rule.isTransactionFraudulent(transaction));
    }

    @Test
    void testSuspiciousFromAccount() {
        Transaction transaction = new Transaction();
        transaction.setFromAccount("zhangsan");
        transaction.setToAccount("normal1");
        assertTrue(rule.isTransactionFraudulent(transaction));
    }

    @Test
    void testSuspiciousToAccount() {
        Transaction transaction = new Transaction();
        transaction.setFromAccount("normal1");
        transaction.setToAccount("lisi");
        assertTrue(rule.isTransactionFraudulent(transaction));
    }

    @Test
    void testBothSuspiciousAccounts() {
        Transaction transaction = new Transaction();
        transaction.setFromAccount("wangwu");
        transaction.setToAccount("zhangsan");
        assertTrue(rule.isTransactionFraudulent(transaction));
    }

    @Test
    void testNullFromAccount() {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(null);
        transaction.setToAccount("normal1");
        assertFalse(rule.isTransactionFraudulent(transaction));
    }

    @Test
    void testNullToAccount() {
        Transaction transaction = new Transaction();
        transaction.setFromAccount("normal1");
        transaction.setToAccount(null);
        assertFalse(rule.isTransactionFraudulent(transaction));
    }
}
