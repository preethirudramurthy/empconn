package com.empconn.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.empconn.email.EmailService;
import com.empconn.response.MockResponse;

@Component
public class HealthCheckService {

	private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);

	@Autowired
	private EmailService emailService;

	@Value("${spring.profiles.active:Local}")
	private String activeProfile;

	public void checkMailServiceHealth() {
		logger.debug("Initiating health check of mail service. The recepient will receive the test mail if the mail service is up and running.");
		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put("environmentName", activeProfile);
		emailService.send("mail-health-check", templateModel);
	}

	public void checkMailServiceWithAttachmentHealth() {
		logger.debug("Test approve Pin mail flow");

		final Map<String, Object> templateModel = new HashMap<>();
		templateModel.put("projectName", "EmpConn");
		templateModel.put("projectInitiator", "PMO-1");
		templateModel.put("projectManager", "Dinesh Kumar");
		templateModel.put("meetingDate", "11/11/2020");
		templateModel.put("projectInformation", MockResponse.getMockProjectInformation());

	}


}
