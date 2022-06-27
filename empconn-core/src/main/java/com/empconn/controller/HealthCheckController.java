package com.empconn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.activedirectory.ActiveDirectoryDepartmentUpdateService;
import com.empconn.activedirectory.ActiveDirectoryUtilityService;
import com.empconn.service.HealthCheckService;
import com.empconn.utilities.CommonUtil;

@RestController
@RequestMapping("healthcheck")
@CrossOrigin(origins = {"${app.domain}"})
public class HealthCheckController {

	private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

	@Autowired
	private HealthCheckService healthCheckService;

	@Autowired
	private ActiveDirectoryDepartmentUpdateService activeDirectoryDepartmentUpdateService;

	@Autowired
	private ActiveDirectoryUtilityService activeDirectoryUtilityService;


	@GetMapping("mail")
	public void mail() {
		logger.debug("Test Mail");
		healthCheckService.checkMailServiceHealth();
	}

	@GetMapping("mail-attachment")
	public void approvePin() {
		logger.debug("Test mail with attachment");
		healthCheckService.checkMailServiceWithAttachmentHealth();
	}

	@GetMapping("active-directory/{loginId}")
	public String getUserFromActiveDirectory(@PathVariable String loginId, @RequestParam String attribute) {
		logger.debug("Test connection status with Active Directory");
		return activeDirectoryUtilityService.getAttribute(loginId, attribute);
	}

	@PostMapping("active-directory/{loginId}")
	public void changeProject(@PathVariable String loginId, @RequestParam String projectName) {
		logger.debug("Test project update operation in Active Directory");
		activeDirectoryDepartmentUpdateService.update(CommonUtil.loginIdToMailId(loginId), projectName);
	}

}
