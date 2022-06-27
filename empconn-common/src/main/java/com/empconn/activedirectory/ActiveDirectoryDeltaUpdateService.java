package com.empconn.activedirectory;

public interface ActiveDirectoryDeltaUpdateService {

	void update(String employeeLoginMailId, String managerLoginMailId, String projectName);
	void updateProject(String employeeLoginMailId, String projectName);

}