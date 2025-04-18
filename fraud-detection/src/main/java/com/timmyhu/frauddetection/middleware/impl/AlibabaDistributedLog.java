package com.timmyhu.frauddetection.middleware.impl;

import com.timmyhu.frauddetection.middleware.DistributedLog;
import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.request.PutLogsRequest;
import com.aliyun.openservices.log.request.GetLogsRequest;
import com.aliyun.openservices.log.response.GetLogsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlibabaDistributedLog implements DistributedLog, InitializingBean {

    @Value("${aliyun.log.endpoint}")
    private String endpoint;

    @Value("${aliyun.log.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.log.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.log.project}")
    private String project;

    @Value("${aliyun.log.logstore}")
    private String logstore;

    @Value("${aliyun.log.topic}")
    private String topic;

    @Value("${aliyun.log.source}")
    private String source;

    private Client client;

    @Override
    public void afterPropertiesSet() {
        this.client = new Client(endpoint, accessKeyId, accessKeySecret);
    }

    @Override
    public void reportLog(String message) {
        try {
            LogItem logItem = new LogItem();
            logItem.PushBack("content", message);
            
            List<LogItem> logItems = new ArrayList<>();
            logItems.add(logItem);
            
            PutLogsRequest request = new PutLogsRequest(project, logstore, topic, source, logItems);
            client.PutLogs(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to report log to Alibaba Cloud Log Service", e);
        }
    }

    @Override
    public List<String> fetch(int messageNum) {
        try {
            int fromTime = (int)(System.currentTimeMillis() / 1000 - 3600);
            int toTime = (int)(System.currentTimeMillis() / 1000);
            String query = "* | select content order by __time__ desc limit " + messageNum;
            
            GetLogsRequest request = new GetLogsRequest(
                project, 
                logstore,
                fromTime,
                toTime,
                topic,
                query
            );
            
            GetLogsResponse response = client.GetLogs(request);
            List<String> logs = new ArrayList<>();
            
            for (com.aliyun.openservices.log.common.QueriedLog log : response.GetLogs()) {
                logs.add(log.GetLogItem().ToJsonString());
            }
            
            return logs;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch logs from Alibaba Cloud Log Service", e);
        }
    }
}
