package com.timmyhu.fraud_detection.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timmyhu.fraud_detection.model.FraudRecord;

@RestController
public class DetectionRecordController {

    @GetMapping("/detection-record/list")
    public List<FraudRecord> queryDetectionRecords() {
        List<FraudRecord> result = new ArrayList<>();
        FraudRecord demo = new FraudRecord();
        demo.setAccount("testAccount");
        demo.setDetectTime(new Date());
        demo.setRuleName("ruleName");
        result.add(demo);
        return result;
    }

}
