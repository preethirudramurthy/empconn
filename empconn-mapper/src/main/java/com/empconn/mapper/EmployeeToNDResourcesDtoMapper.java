package com.empconn.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.AllocationDto;
import com.empconn.dto.NDResourcesDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Employee;
import com.empconn.repositories.AllocationRepository;

@Mapper(componentModel = "spring", uses = {CommonQualifiedMapper.class})
public abstract class EmployeeToNDResourcesDtoMapper {
	@Autowired
	private AllocationRepository allocationRepository;

	@Mapping(source = "empCode", target = "empCode")
	@Mapping(source = "employeeId", target = "resourceId")
	@Mapping(source = "source", target = "empName", qualifiedByName = "employeeToFullName")
	@Mapping(source = "location.name", target = "empLocation")
	@Mapping(source = "title.name", target = "title")
	@Mapping(source = "isActive", target = "isActive")
	@Mapping(source = "department.name", target = "department")
	@Mapping(source = "createdBy", target = "createdBy")
	@Mapping(source = "source", target = "allocationList", qualifiedByName = "allocationdata")
	public abstract NDResourcesDto employeeToNDResourcesDto(Employee source);

	public abstract List<NDResourcesDto> employeeToNDResourcesDto(List<Employee> source);

	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "releaseDate", target = "releaseDate", qualifiedByName = "DateToFormattedDate")
	@Mapping(source = "source", target = "allocationPercentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "source", target = "startDate", qualifiedByName = "getAllocationStartDateForND")
	public abstract AllocationDto employeeToAllocationDto(Allocation source);

	public abstract List<AllocationDto> employeeToAllocationDto(List<Allocation> source);

	@Named("allocationdata")
	List<AllocationDto> allocationdata(Employee employee) {
		List<AllocationDto> allocationDtos;
		final List<Allocation> alloList = allocationRepository.findByEmployeeEmployeeIdAndIsActive(employee.getEmployeeId(), true).stream()
				.filter(n -> !n.getProject().getName().equalsIgnoreCase(ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME))
				.collect(Collectors.toList());
		allocationDtos = employeeToAllocationDto(alloList);
		return allocationDtos;
	}
}
