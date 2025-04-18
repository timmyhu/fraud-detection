package com.timmyhu.frauddetection.middleware;

import java.util.List;

public interface DistributedLog {

    void reportLog(String message);

    List<String> fetch(int messageNum);

}
