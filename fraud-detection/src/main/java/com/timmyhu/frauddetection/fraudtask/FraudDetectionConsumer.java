package com.timmyhu.frauddetection.fraudtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.timmyhu.frauddetection.model.FraudRecord;
import com.timmyhu.frauddetection.model.Transaction;
import com.timmyhu.frauddetection.ruleengine.RuleEngineFacade;
import com.timmyhu.frauddetection.service.FraudRecordService;
import com.timmyhu.frauddetection.util.JsonUtil;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;

@Component
public class FraudDetectionConsumer {
    private static final Logger logger = LoggerFactory.getLogger(FraudDetectionConsumer.class);

    @Autowired
    private RuleEngineFacade ruleEngineFacade;

    @Autowired
    private FraudRecordService fraudRecordService;

    @KafkaListener(
        topics = "${spring.kafka.transaction-topic-name}",
        groupId = "${spring.kafka.detect-fraud-group-name}",
        containerFactory = "kafkaManualAckListenerContainerFactory"
    )
    public void consumeTransaction(String message, Acknowledgment ack) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("[Real-time] Processing new message at {}: {}", startTime, message);
            Transaction transaction = JsonUtil.deserializeTransaction(message);
            if (ruleEngineFacade.detectFraud(transaction)) {
                FraudRecord record = new FraudRecord();
                record.setDetectTime(new Date());
                record.setFraudDetail(message);
                fraudRecordService.reportFraudRecord(record);
                logger.warn("[Real-time] Fraud detected in {}ms: {}", 
                    (System.currentTimeMillis() - startTime), message);
            }
            logger.debug("[Real-time] Message processed in {}ms", 
                (System.currentTimeMillis() - startTime));
            ack.acknowledge();
        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
        }
    }
}
