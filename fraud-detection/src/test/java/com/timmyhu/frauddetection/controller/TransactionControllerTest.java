package com.timmyhu.frauddetection.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timmyhu.frauddetection.model.Transaction;
import com.timmyhu.frauddetection.service.TransactionService;

@WebMvcTest(controllers = TransactionController.class)
@AutoConfigureWebMvc
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSendMessageToKafka() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setFromAccount("123");
        transaction.setToAccount("456");
        transaction.setAmount(100);

        mockMvc.perform(post("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
            .andExpect(status().isOk())
            .andExpect(content().string("ok"));

        verify(transactionService).addTransaction(any(Transaction.class));
    }
}
