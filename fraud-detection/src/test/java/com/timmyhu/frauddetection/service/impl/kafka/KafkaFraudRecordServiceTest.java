package com.timmyhu.frauddetection.service.impl.kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import com.timmyhu.frauddetection.middleware.KafkaMessageFetcher;
import com.timmyhu.frauddetection.middleware.KafkaProducer;
import com.timmyhu.frauddetection.model.FraudRecord;
import com.timmyhu.frauddetection.service.FraudRecordService;

@SpringBootTest
class KafkaFraudRecordServiceTest {

    @Autowired
    private FraudRecordService fraudRecordService;

    @MockBean
    private KafkaProducer kafkaProducer;

    @MockBean
    private KafkaMessageFetcher kafkaMessageFetcher;

    @Test
    void testReportFraudRecord() {
        // 设置topic
        ReflectionTestUtils.setField(fraudRecordService, "topic", "fraud-topic");

        FraudRecord record = new FraudRecord();
        record.setTransactionId(1L);
        record.setFraudDetail("Suspicious transaction");

        fraudRecordService.reportFraudRecord(record);

        verify(kafkaProducer).sendMessage(eq("fraud-topic"), any(String.class));
    }

    @Test
    void testGetFraudRecords() {
        String json1 = "{\"transactionId\":1,\"fraudDetail\":\"Suspicious amount\"}";
        String json2 = "{\"transactionId\":2,\"fraudDetail\":\"Suspicious account\"}";

        when(kafkaMessageFetcher.fetch(10)).thenReturn(List.of(json1, json2));

        List<FraudRecord> records = fraudRecordService.getFraudRecords(10);

        assert records.size() == 2;
        assert records.get(0).getTransactionId() == 1L;
        assert records.get(1).getTransactionId() == 2L;
    }
}
