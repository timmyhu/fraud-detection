package com.timmyhu.frauddetection.service.impl.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.timmyhu.frauddetection.middleware.KafkaProducer;
import com.timmyhu.frauddetection.model.Transaction;
import com.timmyhu.frauddetection.service.TransactionService;
import com.timmyhu.frauddetection.util.JsonUtil;

@Service
public class KafkaTransactionService implements TransactionService{

    private KafkaProducer kafkaProducerService;

    @Value("${spring.kafka.transaction-topic-name}")
    private String topic;

    public KafkaTransactionService(KafkaProducer kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        String message = JsonUtil.serializeTransaction(transaction);
        kafkaProducerService.sendMessage(topic, message);
    }

}
