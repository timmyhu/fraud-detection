package com.timmyhu.frauddetection.service.impl.alibabalog;

import com.timmyhu.frauddetection.middleware.DistributedLog;
import com.timmyhu.frauddetection.model.FraudRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AlibabaLogFraudRecordServiceTest {

    @Mock
    private DistributedLog distributedLog;

    @InjectMocks
    private AlibabaLogFraudRecordService alibabaLogFraudRecordService;

    @Test
    public void testReportFraudRecord() throws Exception {
        FraudRecord record = new FraudRecord();
        record.setFraudDetail("fraud detected");

        alibabaLogFraudRecordService.reportFraudRecord(record);

        verify(distributedLog).reportLog(anyString());
    }

    @Test
    public void testReportFraudRecordWithException() throws Exception {
        FraudRecord record = new FraudRecord();
        record.setFraudDetail("fraud detected");

        doThrow(new RuntimeException("Test Exception"))
            .when(distributedLog).reportLog(anyString());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            alibabaLogFraudRecordService.reportFraudRecord(record);
        });
        assertEquals("Test Exception", exception.getMessage());
    }

    @Test
    public void testGetFraudRecords() throws Exception {
        when(distributedLog.fetch(5)).thenReturn(List.of("record1", "record2"));

        List<String> result = alibabaLogFraudRecordService.getFraudRecords(5);
        
        assertEquals(2, result.size());
        assertEquals("record1", result.get(0));
        assertEquals("record2", result.get(1));
        verify(distributedLog).fetch(5);
    }

    @Test
    public void testGetFraudRecordsWithException() throws Exception {
        when(distributedLog.fetch(5))
            .thenThrow(new RuntimeException("Fetch failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            alibabaLogFraudRecordService.getFraudRecords(5);
        });
        assertEquals("Fetch failed", exception.getMessage());
    }
}
