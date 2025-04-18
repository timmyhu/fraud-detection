package com.timmyhu.frauddetection.service.impl.kafka;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import com.timmyhu.frauddetection.middleware.KafkaProducer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmyhu.frauddetection.model.Transaction;

@SpringBootTest
class KafkaTransactionServiceTest {

    @Autowired
    private KafkaTransactionService transactionService;

    @MockBean
    private KafkaProducer kafkaProducer;

    @Test
    void testAddTransaction() {
        // 设置topic
        ReflectionTestUtils.setField(transactionService, "topic", "transaction-topic");

        Transaction transaction = new Transaction();
        transaction.setFromAccount("123");
        transaction.setToAccount("456");
        transaction.setAmount(100);

        transactionService.addTransaction(transaction);

        verify(kafkaProducer).sendMessage(eq("transaction-topic"), 
            org.mockito.ArgumentMatchers.argThat(json -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode expected = mapper.readTree("{\"fromAccount\":\"123\",\"toAccount\":\"456\",\"amount\":100}");
                    JsonNode actual = mapper.readTree(json);
                    return expected.equals(actual);
                } catch (Exception e) {
                    return false;
                }
            }));
    }
}
