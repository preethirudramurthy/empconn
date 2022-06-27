package com.empconn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.allocation.DeallocationResourceListResponseDto;
import com.empconn.persistence.entities.Allocation;

@Mapper(componentModel = "spring", uses = {CommonQualifiedMapper.class})
public abstract class DeallocationMapper {


	@Mapping(source = "allocationId", target = "allocationId")
	@Mapping(source = "employee.empCode", target = "empCode")
	@Mapping(source = "employee.fullName", target = "empName")
	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "employee.location.name", target = "projectLocation")
	@Mapping(source = "employee.title.name", target = "title")
	@Mapping(source = "reportingManagerId.fullName", target = "reportingMangerName")
	@Mapping(source = "employee.primaryAllocation.reportingManagerId.fullName", target = "primaryManagerName")
	@Mapping(source = "allocation", target = "percentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "isBillable", target = "billable")
	@Mapping(source = "allocation", target = "allocationStartDate", qualifiedByName = "allocationStartDate")
	@Mapping(source = "releaseDate", target = "allocationReleaseDate", qualifiedByName = "DateToLocalDate")
	public abstract DeallocationResourceListResponseDto allocationToDeallocationResoucrListDto(Allocation allocation);

	public abstract List<DeallocationResourceListResponseDto> allocationToAvailableResourceDtoList(
			List<Allocation> allocations);

}
