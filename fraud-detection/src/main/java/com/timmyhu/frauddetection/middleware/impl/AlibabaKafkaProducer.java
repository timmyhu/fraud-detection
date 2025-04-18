package com.timmyhu.frauddetection.middleware.impl;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.timmyhu.frauddetection.middleware.KafkaProducer;

@Service
public class AlibabaKafkaProducer implements KafkaProducer{
    private static final Logger logger = LoggerFactory.getLogger(AlibabaKafkaProducer.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    public AlibabaKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 发送消息到指定主题
     * @param topic 主题名称
     * @param message 消息内容
     */
    public void sendMessage(String topic, String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("消息发送失败: {}", ex.getMessage());
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                logger.info("消息发送成功: {}-{}-{}", 
                    metadata.topic(), 
                    metadata.partition(),
                    metadata.offset());
            }
        });
    }
}
