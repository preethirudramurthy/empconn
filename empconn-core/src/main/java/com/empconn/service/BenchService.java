package com.empconn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.constants.ExceptionConstants;
import com.empconn.exception.EmpConnException;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.Employee;
import com.empconn.repositories.AllocationRepository;
import com.empconn.security.SecurityUtil;

public abstract class BenchService {

	private static final Logger logger = LoggerFactory.getLogger(BenchService.class);
	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private AllocationRepository allocationRepository;

	public abstract String getBenchProjectName();

	public Allocation getBenchAllocation(Long employeeId) {
		logger.debug("Get {} allocation of employee with id {}", getBenchProjectName(), employeeId);
		return allocationRepository.getAllocation(employeeId, getBenchProjectName());
	}

	public Allocation getBenchAllocation(Employee employee) {
		logger.debug("Get {} allocation of employee with id {}", getBenchProjectName(), employee.getEmployeeId());
		return getBenchAllocation(employee.getEmployeeId());
	}

	public int getAvailableBenchAllocation(Employee employee) {
		logger.debug("Get {} allocated percentage of employee with id {}", getBenchProjectName(),
				employee.getEmployeeId());
		final Allocation benchAllocation = getBenchAllocation(employee.getEmployeeId());
		if (null == benchAllocation)
			return 0;
		return benchAllocation.getAllocationDetails().stream().filter(AllocationDetail::getIsActive)
				.map(AllocationDetail::getAllocatedPercentage).reduce(0, Integer::sum);
	}

	public boolean activeBenchAllocationIsAvailable(Employee employee) {
		return (null != getBenchAllocation(employee.getEmployeeId()));
	}

	public boolean activeBenchAllocationIsNotAvailable(Employee employee) {
		return !activeBenchAllocationIsAvailable(employee);
	}

	public void deactivateBenchAllocation(Employee employee) {
		if (null == employee || null == employee.getEmployeeId())
			return;
		deactivateBenchAllocation(employee.getEmployeeId());
	}

	public void deactivateBenchAllocation(Long employeeId) {
		final Allocation allocation = getBenchAllocation(employeeId);
		if (null == allocation)
			return;
		allocation.deactivate(securityUtil.getLoggedInEmployee());
	}

	public boolean onlyActiveAllocationIsNdOrCentralBench(Employee employee) {
		if (null == employee || null == employee.getEmployeeId()) {
			logger.error("Can't check for bench allocations without employee or employee id");
			throw new EmpConnException(ExceptionConstants.DEFAULT_ERROR);
		}

		return allocationRepository.onlyActiveAllocationIsNdOrCentralBench(employee.getEmployeeId());

	}

}
