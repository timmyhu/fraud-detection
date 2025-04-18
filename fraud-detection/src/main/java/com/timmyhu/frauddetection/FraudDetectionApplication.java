package com.timmyhu.frauddetection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.timmyhu.frauddetection"})
public class FraudDetectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FraudDetectionApplication.class, args);
	}
}
