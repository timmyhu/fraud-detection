package com.timmyhu.frauddetection.ruleengine;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.timmyhu.frauddetection.model.Transaction;
import java.util.List;

@Component
public class RuleEngineFacade {

    private static final Logger logger = LoggerFactory.getLogger(RuleEngineFacade.class);
    
    private final List<FraudRule> fraudRules;

    public RuleEngineFacade(List<FraudRule> fraudRules) {
        this.fraudRules = fraudRules;
        logger.info("Initialized with {} fraud detection rules", fraudRules.size());
    }

    public boolean detectFraud(Transaction transaction) {
        logger.debug("Detecting fraud for transaction: {}", transaction);
        boolean isFraud = fraudRules.stream()
            .anyMatch(rule -> rule.isTransactionFraudulent(transaction));
        
        if (isFraud) {
            logger.warn("Fraud detected in transaction: {}", transaction);
        }
        
        return isFraud;
    }
}
