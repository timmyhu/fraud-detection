package com.timmyhu.frauddetection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timmyhu.frauddetection.service.FraudRecordService;

@RestController
public class DetectionRecordController {

    private static final int BATCH_NUM = 10; 

    @Autowired
    private FraudRecordService fraudRecordService;

    @GetMapping("/detection-record/list")
    public List<String> queryDetectionRecords() {
        return fraudRecordService.getFraudRecords(BATCH_NUM);
    }

}
