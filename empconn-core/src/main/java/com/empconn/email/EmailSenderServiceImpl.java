package com.empconn.email;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderServiceImpl implements EmailSenderService {

	private static final Logger logger = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

	@Autowired
	private JavaMailSender emailSender;

	@Override
	public void send(MimeMessage mimeMessage) {
		logger.debug("Initiate send mail operation");
		emailSender.send(mimeMessage);
	}

	@Override
	public void onError(Exception e, MimeMessage mimeMessage) {
		logger.error("Exception[{}] in sending mail", e.getMessage());
		// TODO: Log/report this failure for reference or further action? Consider this in future releases if needed
	}

}