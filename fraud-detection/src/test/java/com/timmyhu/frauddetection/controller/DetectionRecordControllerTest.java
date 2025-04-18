package com.timmyhu.frauddetection.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.timmyhu.frauddetection.model.FraudRecord;
import com.timmyhu.frauddetection.service.FraudRecordService;

@WebMvcTest(DetectionRecordController.class)
class DetectionRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("kafkaFraudRecordService")
    private FraudRecordService fraudRecordService;

    @Test
    void testQueryDetectionRecords() throws Exception {
        FraudRecord record1 = new FraudRecord();
        record1.setTransactionId(1L);
        record1.setFraudDetail("Suspicious amount");

        FraudRecord record2 = new FraudRecord();
        record2.setTransactionId(2L);
        record2.setFraudDetail("Suspicious account");

        when(fraudRecordService.getFraudRecords(10))
            .thenReturn(Arrays.asList(record1, record2));

        mockMvc.perform(get("/detection-record/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].transactionId").value(1))
            .andExpect(jsonPath("$[0].fraudDetail").value("Suspicious amount"))
            .andExpect(jsonPath("$[1].transactionId").value(2))
            .andExpect(jsonPath("$[1].fraudDetail").value("Suspicious account"));
    }
}
