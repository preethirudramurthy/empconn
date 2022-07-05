package com.empconn.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

public interface LdapService {

	@Async
	@Retryable(value = Exception.class, maxAttemptsExpression = "${active.directory.retry.maxAttempts}",
	backoff = @Backoff(delayExpression = "${active.directory.retry.maxDelay}"))
	void updateManager(String employeeEmailId, String managerEmailId);

	@Async
	@Retryable(value = Exception.class, maxAttemptsExpression = "${active.directory.retry.maxAttempts}",
	backoff = @Backoff(delayExpression = "${active.directory.retry.maxDelay}"))
	void updateProject(String employeeEmailId, String projectName);

	@Recover
	void onError(Exception e, String employeeEmailId, String managerEmailId);

}