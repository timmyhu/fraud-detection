package com.timmyhu.frauddetection.ruleengine.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.timmyhu.frauddetection.model.Transaction;

class AmountFraudRuleTest {

    private AmountFraudRule rule = new AmountFraudRule();

    @Test
    void testNormalAmount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(5000);
        assertFalse(rule.isTransactionFraudulent(transaction));
    }

    @Test
    void testFraudAmount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(15000);
        assertTrue(rule.isTransactionFraudulent(transaction));
    }

    @Test
    void testThresholdAmount() {
        Transaction transaction = new Transaction();
        transaction.setAmount(10000);
        assertFalse(rule.isTransactionFraudulent(transaction));
    }
}
