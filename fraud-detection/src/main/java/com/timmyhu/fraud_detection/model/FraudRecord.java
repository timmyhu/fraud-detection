package com.timmyhu.fraud_detection.model;

import java.util.Date;

public class FraudRecord {

    private String ruleName;

    private String account;

    private Date detectTime;

    public String getRuleName() {
        return ruleName;
    }

    public String getAccount() {
        return account;
    }

    public Date getDetectTime() {
        return detectTime;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setDetectTime(Date detectTime) {
        this.detectTime = detectTime;
    }


}
