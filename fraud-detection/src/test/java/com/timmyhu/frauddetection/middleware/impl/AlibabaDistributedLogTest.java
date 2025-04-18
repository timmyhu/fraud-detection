package com.timmyhu.frauddetection.middleware.impl;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.request.PutLogsRequest;
import com.aliyun.openservices.log.request.GetLogsRequest;
import com.aliyun.openservices.log.response.GetLogsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AlibabaDistributedLogTest {

    @MockBean
    private Client client;

    @InjectMocks
    private AlibabaDistributedLog alibabaDistributedLog;

    @Test
    public void testReportLogSuccess() throws Exception {
        String message = "test message";
        when(client.PutLogs(any(PutLogsRequest.class))).thenReturn(null);
        
        alibabaDistributedLog.reportLog(message);
        
        verify(client).PutLogs(any(PutLogsRequest.class));
    }

    @Test
    public void testReportLogFailure() throws Exception {
        String message = "test message";
        when(client.PutLogs(any(PutLogsRequest.class)))
            .thenThrow(new RuntimeException("Test Exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            alibabaDistributedLog.reportLog(message);
        });
        assertEquals("Failed to report log to Alibaba Cloud Log Service", exception.getMessage());
    }

    @Test
    public void testFetchLogs() throws Exception {
        GetLogsResponse mockResponse = mock(GetLogsResponse.class);
        when(mockResponse.GetLogs()).thenReturn(new ArrayList<>());
        when(client.GetLogs(any(GetLogsRequest.class))).thenReturn(mockResponse);

        List<String> result = alibabaDistributedLog.fetch(5);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(client).GetLogs(any(GetLogsRequest.class));
    }

    @Test
    public void testFetchLogsWithContent() throws Exception {
        GetLogsResponse mockResponse = mock(GetLogsResponse.class);
        
        List<com.aliyun.openservices.log.common.QueriedLog> logs = new ArrayList<>();
        com.aliyun.openservices.log.common.QueriedLog log = mock(com.aliyun.openservices.log.common.QueriedLog.class);
        when(log.GetLogItem()).thenReturn(new LogItem());
        logs.add(log);
        
        when(mockResponse.GetLogs()).thenReturn((ArrayList)logs);
        when(client.GetLogs(any(GetLogsRequest.class))).thenReturn(mockResponse);

        List<String> result = alibabaDistributedLog.fetch(5);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFetchLogsFailure() throws Exception {
        when(client.GetLogs(any(GetLogsRequest.class)))
            .thenThrow(new RuntimeException("Test Exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            alibabaDistributedLog.fetch(5);
        });
        assertEquals("Failed to fetch logs from Alibaba Cloud Log Service", exception.getMessage());
    }
}
