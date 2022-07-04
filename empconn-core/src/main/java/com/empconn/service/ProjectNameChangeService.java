package com.empconn.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.activedirectory.ActiveDirectoryDeltaUpdateService;
import com.empconn.util.ProjectUtil;
import com.empconn.utilities.CommonUtil;

@Service
public class ProjectNameChangeService {

	private static final Logger logger = LoggerFactory.getLogger(ProjectNameChangeService.class);

	@Autowired
	private ActiveDirectoryDeltaUpdateService activeDirectoryDeltaUpdateService;

	@Autowired
	private ProjectUtil projectUtil;

	public void process(Long projectId, String projectName, List<String> employeeLoginMailIds) {

		if(null == projectId) {
			logger.debug("This is a new project. Preallocationevents corresponding to project would be triggered from allocation and hence skipping it here");
			return;
		}

		logger.debug("Trigger project name change related events");

		employeeLoginMailIds.forEach(e -> activeDirectoryDeltaUpdateService.updateProject(e, projectName));


		logger.debug("Initiated project name change related events");

	}

	public List<String> getEmployeeLoginMailIdsToUpdateProjectNameChange(Long projectId, String projectName) {
		final List<String> employeeLoginMailIds = projectUtil.getEmployeeLoginMailIdsOfPrimaryAllocationProjects(projectId, projectName);
		logger.debug("Total no.of employees where project name change should be updated to AD is {}", employeeLoginMailIds.size());
		return employeeLoginMailIds.stream().map(CommonUtil::loginIdToMailId).collect(Collectors.toList());
	}

}
