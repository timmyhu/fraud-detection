package com.timmyhu.frauddetection.middleware;

import java.util.List;

public interface KafkaMessageFetcher {

    List<String> fetch(int messageNum);

}
