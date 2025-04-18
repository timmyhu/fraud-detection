package com.timmyhu.frauddetection.util;

import com.alibaba.fastjson.JSON;
import com.timmyhu.frauddetection.model.FraudRecord;
import com.timmyhu.frauddetection.model.Transaction;

public class JsonUtil {

    /**
     * 将Transaction对象序列化为JSON字符串
     * @param transaction 交易对象
     * @return JSON字符串
     */
    public static String serializeTransaction(Transaction transaction) {
        return JSON.toJSONString(transaction);
    }

    /**
     * 将JSON字符串反序列化为Transaction对象
     * @param json JSON字符串
     * @return Transaction对象
     */
    public static Transaction deserializeTransaction(String json) {
        return JSON.parseObject(json, Transaction.class);
    }

        /**
     * 将Transaction对象序列化为JSON字符串
     * @param fraudRecord 欺诈记录
     * @return JSON字符串
     */
    public static String serializeFraudRecord(FraudRecord fraudRecord) {
        return JSON.toJSONString(fraudRecord);
    }

    /**
     * 将JSON字符串反序列化为Transaction对象
     * @param json JSON字符串
     * @return Transaction对象
     */
    public static FraudRecord deserializeFraudRecord(String json) {
        return JSON.parseObject(json, FraudRecord.class);
    }
}
