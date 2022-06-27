package com.empconn.email;

import javax.mail.internet.MimeMessage;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

public interface EmailSenderService {

	@Async
	@Retryable(value = Exception.class, maxAttemptsExpression = "${mail.retry.maxAttempts}",
	backoff = @Backoff(delayExpression = "${mail.retry.maxDelay}"))
	public void send(MimeMessage message);

	@Recover
	public void onError(Exception e, MimeMessage message);

}
