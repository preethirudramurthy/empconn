package com.empconn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.activedirectory.ActiveDirectoryDepartmentUpdateService;
import com.empconn.activedirectory.ActiveDirectoryManagerUpdateService;

@Service
public class LdapServiceImpl implements LdapService {

	private static final Logger logger = LoggerFactory.getLogger(LdapServiceImpl.class);

	@Autowired
	private ActiveDirectoryManagerUpdateService ldapManagerUpdateService;

	@Autowired
	private ActiveDirectoryDepartmentUpdateService ldapDepartmentUpdateService;

	@Override
	public void updateManager(String employeeEmailId, String managerEmailId) {
		ldapManagerUpdateService.update(employeeEmailId, managerEmailId);
	}

	@Override
	public void updateProject(String employeeEmailId, String projectName) {
		ldapDepartmentUpdateService.update(employeeEmailId, projectName);
	}

	@Override
	public void onError(Exception e, String employeeEmailId, String argument) {
		logger.error("Retry operation to update active directory failed for the arguments {}, {}", employeeEmailId, argument);
	}

}
