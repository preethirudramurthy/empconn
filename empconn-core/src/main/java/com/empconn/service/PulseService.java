package com.empconn.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empconn.dto.EmployeeDataResponseDto;
import com.empconn.dto.EmployeeInfoResponseDto;
import com.empconn.mapper.AllocationToEmployeeDataResponseDto;
import com.empconn.mapper.AllocationToEmployeeInfoResponseDto;
import com.empconn.mapper.ObjectToEmployeeDataResponseDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.ProjectRepository;

@Service
@Transactional
public class PulseService {

	private static final Logger logger = LoggerFactory.getLogger(PulseService.class);

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private AllocationToEmployeeInfoResponseDto allocationToEmployeeInfoResponseDto;

	@Autowired
	private AllocationToEmployeeDataResponseDto allocationToEmployeeDataResponseDto;

	@Autowired
	private ObjectToEmployeeDataResponseDto objectToEmployeeDataResponseDto;

	public EmployeeInfoResponseDto getEmployeeDetail(String loginId) {
		logger.debug("Inside Service for getEmployeeDetail");
		Optional<Employee> emOpt = employeeRepository.findByLoginIdIgnoreCaseAndIsActiveTrue(loginId);
		final Employee employee = emOpt.orElse(null);
		return (employee != null)?allocationToEmployeeInfoResponseDto.allocationToEmployeeInfoResponseDto(employee.getPrimaryAllocation()):null;
	}

	public Set<EmployeeDataResponseDto> getAllEmployeesBasedOnProject(String empCode) {
		logger.debug("Inside Service for getAllEmployeesBasedOnProject");
		final Employee employee = employeeRepository.findByEmpCodeIgnoreCaseAndIsActiveTrue(empCode);
		final Set<Allocation> employeeAllocationSetNonBench = allocationRepository.findByEmployeeIdAndIsActive(employee.getEmployeeId(),true);
		final Set<Allocation> employeeAllocationSetBench = allocationRepository.findByEmployeeIdAndBenchProject(employee.getEmployeeId(),true);
		Set<EmployeeDataResponseDto> dataResponseDtos = new HashSet<>();
		if(!employeeAllocationSetNonBench.isEmpty()) {
			getEmployeesList(employeeAllocationSetNonBench, dataResponseDtos);
		}
		else {
			getEmployeesList(employeeAllocationSetBench, dataResponseDtos);
		}
		logger.debug("Size of EmployeeDataResponseDto Set : {}",dataResponseDtos.size());
		return dataResponseDtos;
	}

	private void getEmployeesList(final Set<Allocation> employeeAllocationSet,
			final Set<EmployeeDataResponseDto> dataResponseDtos) {
		final Set<Long> projectSet = new HashSet<>();
		employeeAllocationSet.stream().forEach(a-> projectSet.add(a.getProject().getProjectId()));
		final Set<Allocation> allocationSet = allocationRepository.findByProjectProjectIdInAndIsActiveTrue(projectSet);
		allocationSet.removeAll(employeeAllocationSet);
		dataResponseDtos.addAll(allocationToEmployeeDataResponseDto.allocationsToEmployeeDataResponseDto(allocationSet));
		logger.debug("Size of EmployeeDataResponseDto Set : {}", dataResponseDtos.size());
		final List<Project> projectList = projectRepository.findAllById(projectSet);
		for(final Project p : projectList) {
			final Set<Employee> employeeSet = new HashSet<>(p.getGdms().values());
			p.getProjectLocations().stream().forEach(pl -> employeeSet.addAll(pl.getAllManagers().values()));
			final Set<EmployeeDataResponseDto> dto = allocationToEmployeeDataResponseDto.employeesToEmployeeDataResponseDto(employeeSet);
			dto.stream().forEach(data -> {
				data.setProject(p.getName());
				data.setProjectName(p.getName());
			});
			dataResponseDtos.addAll(dto);
		}
	}

	public List<EmployeeDataResponseDto> getAllEmployeesPartialSearch(String empName) {
		final Set<Object[]> object = employeeRepository.findEmployeesMatchingName(empName,empName,empName,empName,empName,empName,empName,empName);
		return objectToEmployeeDataResponseDto.objectsToEmployeeDataResponseDto(object);
	}
}
