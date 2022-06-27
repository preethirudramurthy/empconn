package com.empconn.activedirectory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.service.LdapService;

@Service
public class ActiveDirectoryDeltaUpdateServiceImpl implements ActiveDirectoryDeltaUpdateService {

	private static final Logger logger = LoggerFactory.getLogger(ActiveDirectoryDeltaUpdateServiceImpl.class);

	@Autowired
	private LdapService ldapService;

	@Override
	public void update(String employeeLoginMailId, String managerLoginMailId, String projectName) {

		logger.debug("Updating AD with the manager email id for the employee[{}]", employeeLoginMailId);
		ldapService.updateManager(employeeLoginMailId, managerLoginMailId);

		updateProject(employeeLoginMailId, projectName);

	}

	@Override
	public void updateProject(String employeeLoginMailId, String projectName) {

		logger.debug("Updating AD with the project name for the employee[{}]", employeeLoginMailId);
		ldapService.updateProject(employeeLoginMailId, projectName);

	}

}
