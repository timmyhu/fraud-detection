package com.timmyhu.frauddetection.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timmyhu.frauddetection.model.FraudRecord;
import com.timmyhu.frauddetection.service.FraudRecordService;

@RestController
public class DetectionRecordController {

    private static final int BATCH_NUM = 10; 

    @Autowired
    @Qualifier("kafkaFraudRecordService") 
    private FraudRecordService fraudRecordService;

    @GetMapping("/detection-record/list")
    public List<FraudRecord> queryDetectionRecords() {
        return fraudRecordService.getFraudRecords(BATCH_NUM);
    }

}
