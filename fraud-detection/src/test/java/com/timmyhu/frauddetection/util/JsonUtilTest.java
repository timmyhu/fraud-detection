package com.timmyhu.frauddetection.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import org.junit.jupiter.api.Test;

import com.timmyhu.frauddetection.model.FraudRecord;
import com.timmyhu.frauddetection.model.Transaction;

class JsonUtilTest {

    @Test
    void testTransactionSerialization() {
        Transaction transaction = new Transaction();
        transaction.setFromAccount("123");
        transaction.setToAccount("456");
        transaction.setAmount(100);

        String json = JsonUtil.serializeTransaction(transaction);
        Transaction deserialized = JsonUtil.deserializeTransaction(json);

        assertNotNull(deserialized);
        assertEquals(transaction.getFromAccount(), deserialized.getFromAccount());
        assertEquals(transaction.getToAccount(), deserialized.getToAccount());
        assertEquals(transaction.getAmount(), deserialized.getAmount());
    }

    @Test
    void testFraudRecordSerialization() {
        FraudRecord record = new FraudRecord();
        record.setFraudDetail("Suspicious amount");
        record.setDetectTime(new Date());

        String json = JsonUtil.serializeFraudRecord(record);
        FraudRecord deserialized = JsonUtil.deserializeFraudRecord(json);

        assertNotNull(deserialized);
        assertEquals(record.getFraudDetail(), deserialized.getFraudDetail());
        assertEquals(record.getDetectTime(), deserialized.getDetectTime());
    }
}
