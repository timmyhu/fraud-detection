package com.timmyhu.frauddetection.ruleengine.impl;

import com.timmyhu.frauddetection.model.Transaction;
import com.timmyhu.frauddetection.ruleengine.FraudRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountAbnormalityRule implements FraudRule {

    private static final Logger logger = LoggerFactory.getLogger(AccountAbnormalityRule.class);
    private static final Set<String> SUSPICIOUS_ACCOUNTS = Collections.newSetFromMap(new ConcurrentHashMap<>());

    //just for task. In production live enviorment,these data should be stored somewhere sush as data storage or cache.
    static {
        SUSPICIOUS_ACCOUNTS.add("zhangsan");
        SUSPICIOUS_ACCOUNTS.add("lisi");
        SUSPICIOUS_ACCOUNTS.add("wangwu");
    }

    @Override
    public boolean isTransactionFraudulent(Transaction transaction) {
        if (transaction.getFromAccount() == null || transaction.getToAccount() == null) {
            return false;
        }
        boolean isFraud = SUSPICIOUS_ACCOUNTS.contains(transaction.getFromAccount()) || 
                         SUSPICIOUS_ACCOUNTS.contains(transaction.getToAccount());
        
        if (isFraud) {
            logger.warn("检测到可疑账户交易，from: {}, to: {}", 
                       transaction.getFromAccount(), transaction.getToAccount());
        }
        
        return isFraud;
    }
}
