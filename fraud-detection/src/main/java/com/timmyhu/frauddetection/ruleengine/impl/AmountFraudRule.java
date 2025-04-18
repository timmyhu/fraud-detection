package com.timmyhu.frauddetection.ruleengine.impl;

import com.timmyhu.frauddetection.model.Transaction;
import com.timmyhu.frauddetection.ruleengine.FraudRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AmountFraudRule implements FraudRule {

    private static final Logger logger = LoggerFactory.getLogger(AmountFraudRule.class);

    private static final int FRAUD_AMOUNT = 10000;

    @Override
    public boolean isTransactionFraudulent(Transaction transaction) {
        boolean isFraud = transaction.getAmount() > FRAUD_AMOUNT;
        if (isFraud) {
            logger.warn("检测到欺诈交易，金额: {}, 阈值: {}", transaction.getAmount(), FRAUD_AMOUNT);
        }
        return isFraud;
    }
}
