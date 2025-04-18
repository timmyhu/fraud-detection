package com.timmyhu.frauddetection.fraudtask;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.support.Acknowledgment;

import com.timmyhu.frauddetection.model.FraudRecord;
import com.timmyhu.frauddetection.model.Transaction;
import com.timmyhu.frauddetection.ruleengine.RuleEngineFacade;
import com.timmyhu.frauddetection.service.FraudRecordService;

@SpringBootTest
public class FraudDetectionConsumerTest {

    @Autowired
    private FraudDetectionConsumer fraudDetectionConsumer;

    @MockBean
    private RuleEngineFacade ruleEngineFacade;

    @MockBean
    private FraudRecordService fraudRecordService;

    @MockBean
    private Acknowledgment acknowledgment;

    @Test
    public void testConsumeTransaction_FraudDetected() throws Exception {
        String message = "{\"fromAccount\":\"test1\",\"toAccount\":\"test2\"}";
        when(ruleEngineFacade.detectFraud(any(Transaction.class))).thenReturn(true);

        fraudDetectionConsumer.consumeTransaction(message, acknowledgment);

        verify(ruleEngineFacade).detectFraud(any(Transaction.class));
        verify(fraudRecordService).reportFraudRecord(any(FraudRecord.class));
        verify(acknowledgment).acknowledge();
    }

    @Test
    public void testConsumeTransaction_NoFraud() throws Exception {
        String message = "{\"fromAccount\":\"test1\",\"toAccount\":\"test2\"}";
        when(ruleEngineFacade.detectFraud(any(Transaction.class))).thenReturn(false);

        fraudDetectionConsumer.consumeTransaction(message, acknowledgment);

        verify(ruleEngineFacade).detectFraud(any(Transaction.class));
        verify(fraudRecordService, never()).reportFraudRecord(any(FraudRecord.class));
        verify(acknowledgment).acknowledge();
    }
}
