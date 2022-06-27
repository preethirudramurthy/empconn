package com.empconn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empconn.service.LdapService;

@RestController
@RequestMapping("ldap")
@CrossOrigin(origins = {"${app.domain}"})
public class LDAPController {

	private static final Logger logger = LoggerFactory.getLogger(LDAPController.class);

	@Autowired
	private LdapService ldapService;

	@PutMapping("/employee/{employeeEmailId}/manager/{managerEmployeeEmailId}")
	public void updateEmployeeManagerInLDAP(@PathVariable String employeeEmailId, @PathVariable String managerEmployeeEmailId) {
		logger.debug("Update manager");
		ldapService.updateManager(employeeEmailId, managerEmployeeEmailId);
	}

	@PutMapping("/employee/{employeeEmailId}/project/{projectName}")
	public void updateEmployeeProjectInLDAP(@PathVariable String employeeEmailId, @PathVariable String projectName) {
		logger.debug("Update projectName");
		ldapService.updateProject(employeeEmailId, projectName);
	}

}
