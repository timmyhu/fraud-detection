package com.timmyhu.frauddetection.middleware.impl;

import com.timmyhu.frauddetection.middleware.KafkaMessageFetcher;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class AlibabaKafkaMessageFetcher implements KafkaMessageFetcher {

    @Value("${spring.kafka.fraud-record-topic-name}")
    private String topicName;

    @Value("${spring.kafka.batch-fetch-fraud-group-name}")
    private String groupId;



    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @Override
    public List<String> fetch(int messageNum) {
        List<String> messages = new ArrayList<>();
        Properties props = new Properties();
        props.putAll(consumerFactory.getConfigurationProperties());
        
        try (AdminClient admin = AdminClient.create(props);
             KafkaConsumer<String, String> consumer = 
                (KafkaConsumer<String, String>) consumerFactory.createConsumer(groupId, null)) {
            
            // Get all partitions for the topic
            List<TopicPartition> partitions = admin.describeTopics(Collections.singletonList(topicName))
                .all()
                .get()
                .get(topicName)
                .partitions()
                .stream()
                .map(p -> new TopicPartition(topicName, p.partition()))
                .collect(Collectors.toList());
            
            // Get end offsets for all partitions
            Map<TopicPartition, OffsetSpec> offsetSpecs = partitions.stream()
                .collect(Collectors.toMap(p -> p, p -> OffsetSpec.latest()));
            Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> offsets = 
                admin.listOffsets(offsetSpecs).all().get();
            
            // Manually assign all partitions
            consumer.assign(partitions);
            
            // For each partition, get latest messages
            for (TopicPartition partition : partitions) {
                long endOffset = offsets.get(partition).offset();
                long startOffset = Math.max(0, endOffset - Math.min(10, messageNum));
                
                consumer.seek(partition, startOffset);
                
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5000));
                for (ConsumerRecord<String, String> record : records) {
                    if (record.partition() == partition.partition()) {
                        messages.add(record.value());
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to fetch messages from Kafka", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch messages from Kafka", e);
        }
        return messages;
    }
}
