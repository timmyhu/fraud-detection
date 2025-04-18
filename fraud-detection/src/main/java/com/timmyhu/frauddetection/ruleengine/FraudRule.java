package com.timmyhu.frauddetection.ruleengine;

import com.timmyhu.frauddetection.model.Transaction;

public interface FraudRule {

    boolean isTransactionFraudulent(Transaction transaction);

}
