package com.empconn.service;

import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Employee;
import com.empconn.repositories.AllocationRepository;
import com.empconn.security.SecurityUtil;

@Service
public class DeactivationService {

	private static final Logger logger = LoggerFactory.getLogger(DeactivationService.class);

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private SecurityUtil securityUtil;

	private void deactivateAllocations(Employee employee) {
		if (null == employee)
			return;

		logger.debug("Deactivate allocations of employee {}", employee.getFirstName());
		final Set<Allocation> allocations = allocationRepository.findByEmployeeEmployeeId(employee.getEmployeeId());
		allocations.forEach(a -> a.deactivate(securityUtil.getLoggedInEmployee()));
	}

	@Transactional
	public void deactivate(Employee employee) {
		if (null == employee)
			return;

		logger.debug("Deactivate employee {} and allocations", employee.getFirstName());
		deactivateAllocations(employee);
		employee.setIsActive(false);

	}
}
