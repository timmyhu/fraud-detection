package com.timmyhu.frauddetection.model;

import java.util.Date;

public class FraudRecord {

    private Long transactionId;

    private String fraudDetail;

    private Date detectTime;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Date getDetectTime() {
        return detectTime;
    }

    public void setDetectTime(Date detectTime) {
        this.detectTime = detectTime;
    }

    public String getFraudDetail() {
        return fraudDetail;
    }

    public void setFraudDetail(String fraudDetail) {
        this.fraudDetail = fraudDetail;
    }
}
