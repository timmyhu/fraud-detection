package com.timmyhu.frauddetection.middleware.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.context.TestPropertySource;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AdminClient.class, AlibabaKafkaMessageFetcher.class})
@TestPropertySource(properties = {
    "spring.kafka.fraud-record-topic-name=fraud-topic",
    "spring.kafka.batch-fetch-fraud-group-name=test-group"
})
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
class AlibabaKafkaMessageFetcherTest {

    @Mock
    private ConsumerFactory<String, String> consumerFactory;

    @Mock
    private KafkaConsumer<String, String> kafkaConsumer;

    @Mock
    private AdminClient adminClient;

    @InjectMocks
    private AlibabaKafkaMessageFetcher messageFetcher;

    @Before
    public void setUp() {
        Map<String, Object> configs = new HashMap<>();
        configs.put("bootstrap.servers", "localhost:9092");
        configs.put("group.id", "test-group");
        when(consumerFactory.getConfigurationProperties()).thenReturn(configs);
        when(consumerFactory.createConsumer(any(), any())).thenReturn(kafkaConsumer);
    }

    @Test
    void testFetchMessages() throws Exception {
        // Mock configuration
        Map<String, Object> configs = new HashMap<>();
        configs.put("bootstrap.servers", "localhost:9092");
        configs.put("group.id", "test-group");
        when(consumerFactory.getConfigurationProperties()).thenReturn(configs);
        when(consumerFactory.createConsumer(any(), any())).thenReturn(kafkaConsumer);

        // Mock consumer records
        TopicPartition partition = new TopicPartition("fraud-topic", 0);
        ConsumerRecord<String, String> record = new ConsumerRecord<>("fraud-topic", 0, 0, "key", "message");
        ConsumerRecords<String, String> records = new ConsumerRecords<>(Collections.singletonMap(partition, 
            Collections.singletonList(record)));
        when(kafkaConsumer.poll(any(Duration.class))).thenReturn(records);

        List<String> messages = messageFetcher.fetch(1);
        assertEquals(1, messages.size());
        assertEquals("message", messages.get(0));
    }
}
