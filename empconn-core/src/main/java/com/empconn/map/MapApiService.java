package com.empconn.map;

import java.util.concurrent.Future;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

public interface MapApiService {

	@Async
	@Retryable(value = Exception.class, maxAttemptsExpression = "${map.retry.maxAttempts}", backoff = @Backoff(delayExpression = "${map.retry.maxDelay}"))
	public <T> Future<ResponseEntity<String>> doMapRequest(String url, HttpMethod requestMethod, T requestObject);

	@Recover
	public <T> Future<ResponseEntity<String>> onError(Exception e, String url, HttpMethod requestMethod,
			T requestObject);

}
