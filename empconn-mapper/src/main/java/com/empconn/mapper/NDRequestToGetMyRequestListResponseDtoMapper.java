package com.empconn.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.AllocationDto;
import com.empconn.dto.GetMyRequestListResponseDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.NdRequest;
import com.empconn.persistence.entities.SalesforceIdentifier;
import com.empconn.repositories.AllocationRepository;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public abstract class NDRequestToGetMyRequestListResponseDtoMapper {

	@Autowired
	private AllocationRepository allocationRepository;

	@Mapping(source = "ndRequestId", target = "requestId")
	@Mapping(source = "employee1.empCode", target = "empCode")
	@Mapping(source = "employee1", target = "empName", qualifiedByName = "employeeToFullName")
	@Mapping(source = "employee1.title.name", target = "title")
	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "project.salesforceIdentifiers", target = "salesforceIdList", qualifiedByName = "salesforceIdentifiersToStringList")
	@Mapping(source = "percentage", target = "percentage")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "releaseDate", target = "releaseDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "employee2", target = "reportingManager", qualifiedByName = "employeeToFullName")
	@Mapping(source = "isBillable", target = "billable")
	@Mapping(source = "source", target = "allocationList", qualifiedByName = "allocationdata")
	public abstract GetMyRequestListResponseDto ndRequestToGetMyRequestListResponseDto(NdRequest source);

	@Named("ndRequestsToResponseDtos")
	public abstract List<GetMyRequestListResponseDto> ndRequestsToGetMyRequestListResponses(List<NdRequest> ndRequests);

	@Named("salesforceIdentifiersToStringList")
	public List<String> salesforceIdentifiersToStringList(Set<SalesforceIdentifier> salesforceIdentifiers) {
		if (salesforceIdentifiers != null) {
			return salesforceIdentifiers.stream().map(SalesforceIdentifier::getValue).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Named("allocationdata")
	List<AllocationDto> allocationdata(NdRequest source) {
		List<AllocationDto> allocationDtos;
		final List<Allocation> alloList = allocationRepository
				.findByEmployeeEmployeeIdAndIsActive(source.getEmployee1().getEmployeeId(), true).stream()
				.filter(n -> !n.getProject().getName()
						.equalsIgnoreCase(ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME))
				.collect(Collectors.toList());
		allocationDtos = employeeToAllocationDto(alloList);
		return allocationDtos;
	}

	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "releaseDate", target = "releaseDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "source", target = "allocationPercentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "source", target = "startDate", qualifiedByName = "getAllocationStartDateForND")
	public abstract AllocationDto employeeToAllocationDto(Allocation source);

	public abstract List<AllocationDto> employeeToAllocationDto(List<Allocation> source);
}
