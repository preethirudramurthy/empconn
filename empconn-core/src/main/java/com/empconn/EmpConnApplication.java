package com.empconn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableRetry
@IntegrationComponentScan
@EnableIntegration
@EnableScheduling
@EnableCaching
public class EmpConnApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpConnApplication.class, args);
	}

}
