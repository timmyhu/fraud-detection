package com.timmyhu.frauddetection.model;

import java.util.Date;

public class FraudRecord {

    private String fraudDetail;

    private Date detectTime;

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
