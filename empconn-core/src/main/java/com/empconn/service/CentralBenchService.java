package com.empconn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.constants.ApplicationConstants;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.repositories.ProjectLocationRespository;

@Service
public class CentralBenchService extends BenchService {

	private static final Logger logger = LoggerFactory.getLogger(CentralBenchService.class);

	@Autowired
	private ProjectLocationRespository projectLocationRepository;

	public String getBenchProjectName() {
		return ApplicationConstants.DELIVERY_BENCH_PROJECT_NAME;
	}

	public Employee getDevManager() {
		logger.debug("Get Dev Manager of Central Bench");
		final ProjectLocation centralBenchProjectLocation = projectLocationRepository.findGlobalCentralBenchProjectLocation();
		return centralBenchProjectLocation.getEmployee1();
	}

	public Employee getDevGdm() {
		logger.debug("Get Dev GDM of Central Bench");
		final ProjectLocation centralBenchProjectLocation = projectLocationRepository.findGlobalCentralBenchProjectLocation();
		return centralBenchProjectLocation.getProject().getEmployee1();
	}

}
