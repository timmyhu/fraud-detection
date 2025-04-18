package com.timmyhu.frauddetection.middleware.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.support.SendResult;

@SpringBootTest
class AlibabaKafkaProducerTest {

    @Autowired
    private AlibabaKafkaProducer kafkaProducer;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testSendMessageSuccess() throws Exception {
        // 准备成功响应
        RecordMetadata metadata = new RecordMetadata(
            new TopicPartition("test-topic", 0), 0, 0, 0, 0L, 0, 0);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test-topic", "test-message");
        SendResult<String, String> result = new SendResult<>(producerRecord, metadata);
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(result);

        when(kafkaTemplate.send(any(), any())).thenReturn(future);

        // 测试发送消息
        kafkaProducer.sendMessage("test-topic", "test-message");
    }

    @Test
    void testSendMessageFailure() throws Exception {
        // 准备失败响应
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka error"));

        when(kafkaTemplate.send(any(), any())).thenReturn(future);

        // 测试发送消息
        kafkaProducer.sendMessage("test-topic", "test-message");
    }
}
