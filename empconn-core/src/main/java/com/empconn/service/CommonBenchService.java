package com.empconn.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.constants.ApplicationConstants;
import com.empconn.constants.ExceptionConstants;
import com.empconn.exception.EmpConnException;
import com.empconn.mapper.ProjectUnitValueMapper;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Location;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.LocationRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.vo.UnitValue;

@Service
public class CommonBenchService {

	private static final Logger logger = LoggerFactory.getLogger(CommonBenchService.class);

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private ProjectLocationRespository projectLocationRespository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectUnitValueMapper projectUnitValueMapper;

	public boolean onlyActiveAllocationIsNdOrCentralBench(Employee employee) {
		if (null == employee || null == employee.getEmployeeId()) {
			logger.error("Can't check for bench allocations without employee or employee id");
			throw new EmpConnException(ExceptionConstants.DEFAULT_ERROR);
		}

		return allocationRepository.onlyActiveAllocationIsNdOrCentralBench(employee.getEmployeeId());

	}

	public ProjectLocation getGlobalBenchProjectLocation(final Project bench) {
		final Location location = locationRepository.findByName(ApplicationConstants.LOCATION_GLOBAL);
		final ProjectLocation globalBenchProjectLocation = projectLocationRespository
				.findByProjectProjectIdAndLocationLocationId(bench.getProjectId(), location.getLocationId()).get(0);
		return globalBenchProjectLocation;
	}

	public List<String> getBenchProjectNames() {
		return Arrays.asList("Central Bench", "NDBench");
	}

	public List<UnitValue> findBenchProjects(String partialProjectName) {
		logger.debug("Find bench project with name like {}", partialProjectName);
		return projectUnitValueMapper.projectsToUnitValueList(projectRepository.findBenchProjects(partialProjectName));
	}

	public boolean onlyActiveAllocationIsNdOrDeliveryBench(Employee employee) {
		if (null == employee || null == employee.getEmployeeId()) {
			logger.error("Can't check for bench allocations without employee or employee id");
			throw new EmpConnException(ExceptionConstants.DEFAULT_ERROR);
		}

		return allocationRepository.onlyActiveAllocationIsNdOrDeliveryBench(employee.getEmployeeId());

	}

}
