package com.timmyhu.frauddetection.service.impl.kafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.timmyhu.frauddetection.middleware.KafkaMessageFetcher;
import com.timmyhu.frauddetection.middleware.KafkaProducer;
import com.timmyhu.frauddetection.model.FraudRecord;
import com.timmyhu.frauddetection.service.FraudRecordService;
import com.timmyhu.frauddetection.util.JsonUtil;

@Service
public class KafkaFraudRecordService implements FraudRecordService{

    private KafkaProducer kafkaProducerService;

    private KafkaMessageFetcher kafkaMessageFetcher;

    @Value("${spring.kafka.fraud-record-topic-name}")
    private String topic;

    public KafkaFraudRecordService(KafkaProducer kafkaProducerService, KafkaMessageFetcher kafkaMessageFetcher) {
        this.kafkaProducerService = kafkaProducerService;
        this.kafkaMessageFetcher = kafkaMessageFetcher;
    }

    @Override
    public void reportFraudRecord(FraudRecord record) {
        String message = JsonUtil.serializeFraudRecord(record);
        kafkaProducerService.sendMessage(topic, message);
    }

    @Override
    public List<FraudRecord> getFraudRecords(int num) {
        List<String> messages = kafkaMessageFetcher.fetch(num);
        if (messages == null || messages.isEmpty()) {
            return List.of();
        }

        return messages.stream()
            .filter(msg -> msg != null && !msg.trim().isEmpty())
            .map(msg -> {
                try {
                    return JsonUtil.deserializeFraudRecord(msg);
                } catch (Exception e) {
                    return null;
                }
            })
            .filter(record -> record != null)
            .toList();
    }

    

}
