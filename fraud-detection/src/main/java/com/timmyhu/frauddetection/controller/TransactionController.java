package com.timmyhu.frauddetection.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.timmyhu.frauddetection.model.Transaction;
import com.timmyhu.frauddetection.service.TransactionService;

@RestController
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public String sendMessageToKafka(
            @RequestBody Transaction transaction) {
        transactionService.addTransaction(transaction);
        return "ok";
    }
}
