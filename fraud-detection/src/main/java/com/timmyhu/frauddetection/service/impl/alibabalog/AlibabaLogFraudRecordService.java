package com.timmyhu.frauddetection.service.impl.alibabalog;

import java.util.List;

import org.springframework.stereotype.Service;

import com.timmyhu.frauddetection.middleware.DistributedLog;
import com.timmyhu.frauddetection.model.FraudRecord;
import com.timmyhu.frauddetection.service.FraudRecordService;
import com.timmyhu.frauddetection.util.JsonUtil;

@Service
public class AlibabaLogFraudRecordService implements FraudRecordService{

    private DistributedLog distributedLog;

    public AlibabaLogFraudRecordService(DistributedLog distributedLog) {
        this.distributedLog = distributedLog;
    }

    @Override
    public void reportFraudRecord(FraudRecord record) {
        String message = JsonUtil.serializeFraudRecord(record);
        distributedLog.reportLog(message);
    }

    @Override
    public List<String> getFraudRecords(int num) {
        return distributedLog.fetch(num);
    }

}
