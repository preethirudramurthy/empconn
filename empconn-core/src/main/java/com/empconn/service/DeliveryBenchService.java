package com.empconn.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.empconn.mapper.CommonQualifiedMapper;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Employee;
import com.empconn.repositories.AllocationRepository;
import com.empconn.security.SecurityUtil;

@Service
public class DeliveryBenchService {

	private static final Logger logger = LoggerFactory.getLogger(DeliveryBenchService.class);

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private SyncToTimesheetService syncToTimesheetService;

	@Autowired
	private CommonQualifiedMapper commonQualifiedMapper;

	public int getAvailableBenchAllocation(Employee employee) {
		logger.debug("Get Practice and Central Bench allocated percentage of employee with id {}",
				employee.getEmployeeId());
		final Set<Allocation> deliveryBenchAllocations = getDeliveryBenchAllocations(employee);
		if (CollectionUtils.isEmpty(deliveryBenchAllocations))
			return 0;
		return deliveryBenchAllocations.stream().map(a -> commonQualifiedMapper.mergedAllocatedPercentage(a)).reduce(0, Integer::sum);
	}

	private Set<Allocation> getDeliveryBenchAllocations(Employee employee) {
		return allocationRepository.findByEmployeeIdAndDeliveryBenchProject(employee.getEmployeeId(), true);
	}

	public void deactivateDeliveryBenchAllocation(Employee employee) {
		logger.debug("Deactivate delivery bench projects of employee with id {}", employee.getEmployeeId());
		final Set<Allocation> deliveryBenchAllocations = getDeliveryBenchAllocations(employee);
		deliveryBenchAllocations.forEach(a -> a.deactivate(securityUtil.getLoggedInEmployee()));
		deliveryBenchAllocations.forEach(a -> syncToTimesheetService.syncProjectAllocation(a));
	}

}
