package com.timmyhu.frauddetection.middleware;

public interface KafkaProducer {
     void sendMessage(String topic, String message);
}
