package com.empconn.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.empconn.persistence.entities.Project;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.ProjectRepository;

@Service
public class ProjectUtil {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	public List<String> getEmployeeLoginMailIdsOfPrimaryAllocationProjects(Long projectId, String projectName) {
		Optional<Project> prOpt = projectRepository.findById(projectId);
		if(prOpt.isPresent() && prOpt.get().getIsActive()) {
			return employeeRepository.getEmployeeLoginIdsOfPrimaryAllocationProjects(projectId, StringUtils.trim(projectName));
		}

		return new ArrayList<>();
	}

}
