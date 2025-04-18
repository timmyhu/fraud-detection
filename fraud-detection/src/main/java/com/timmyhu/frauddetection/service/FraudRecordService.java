package com.timmyhu.frauddetection.service;

import java.util.List;

import com.timmyhu.frauddetection.model.FraudRecord;

public interface FraudRecordService {

    void reportFraudRecord(FraudRecord record);

    List<String> getFraudRecords(int num);

}
