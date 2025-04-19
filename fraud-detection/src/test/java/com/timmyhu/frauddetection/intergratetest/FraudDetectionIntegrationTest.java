package com.timmyhu.frauddetection.intergratetest;

import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FraudDetectionIntegrationTest {

    private static final String BASE_URL = "http://121.41.68.171";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Test
    public void testLargeAmountTransactionFlagged() throws Exception {
        // 1. Post transaction data
        int transactionId = 1000;
        String fromAccount = UUID.randomUUID().toString();
        String transactionJson = String.format("""
            {
                "transactionId": %d,
                "fromAccount": "%s",
                "toAccount": "account4666", 
                "amount": 800000
            }""", transactionId,fromAccount);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/transaction"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(transactionJson))
                .build();

        HttpResponse<String> postResponse = httpClient.send(
            postRequest, HttpResponse.BodyHandlers.ofString());
        
        // Verify POST was successful
        assertTrue(postResponse.statusCode() >= 200 && postResponse.statusCode() < 300,
            "POST transaction failed with status: " + postResponse.statusCode());

        

        // Wait 2 seconds to allow fraud detection processing
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 2. Get detection records and verify fromAccount exists
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/detection-record/list"))
                .GET()
                .build();

        HttpResponse<String> getResponse = httpClient.send(
            getRequest, HttpResponse.BodyHandlers.ofString());
        
        // Verify GET was successful and contains the fromAccount
        assertTrue(getResponse.statusCode() >= 200 && getResponse.statusCode() < 300,
            "GET detection records failed with status: " + getResponse.statusCode());
        assertTrue(getResponse.body().contains(String.valueOf(fromAccount)),
            "Response does not contain the expected fromAccount");
    }

    @Test
    public void testSmallAmountTransactionNotFlagged() throws Exception {
        // 1. Post small transaction data
        int transactionId = 1000;
        String fromAccount = UUID.randomUUID().toString();
        String transactionJson = String.format("""
            {
                "transactionId": %d,
                "fromAccount": "%s",
                "toAccount": "account4666", 
                "amount": 100
            }""", transactionId, fromAccount);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/transaction"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(transactionJson))
                .build();

        HttpResponse<String> postResponse = httpClient.send(
            postRequest, HttpResponse.BodyHandlers.ofString());
        
        // Verify POST was successful
        assertTrue(postResponse.statusCode() >= 200 && postResponse.statusCode() < 300,
            "POST transaction failed with status: " + postResponse.statusCode());


        // 2. Get detection records and verify transactionId does NOT exist
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/detection-record/list"))
                .GET()
                .build();

        HttpResponse<String> getResponse = httpClient.send(
            getRequest, HttpResponse.BodyHandlers.ofString());
        
        // Verify GET was successful and does NOT contain the transactionId
        assertTrue(getResponse.statusCode() >= 200 && getResponse.statusCode() < 300,
            "GET detection records failed with status: " + getResponse.statusCode());
        assertFalse(getResponse.body().contains(String.valueOf(fromAccount)),
            "Response should not contain the fromAccount for small amount");
    }
}
