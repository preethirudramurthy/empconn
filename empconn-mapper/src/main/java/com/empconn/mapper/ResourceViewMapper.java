package com.empconn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.dto.allocation.ResourceViewResponseDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.repositories.EarmarkRepository;

@Mapper(componentModel = "spring", uses = {CommonQualifiedMapper.class})
public abstract class ResourceViewMapper {


	@Autowired
	EarmarkRepository earMarkRepository;

	@Mapping(source = "allocationId", target = "allocationId")
	@Mapping(source = "employee.empCode", target = "empCode")
	@Mapping(source = "employee.fullName", target = "empName")
	@Mapping(source = "employee.title.name", target = "title")
	@Mapping(source = "employee.location.name", target = "projectLocation")
	@Mapping(source = "employee", target = "primarySkillList", qualifiedByName = "employeeToPrimarySkillList")
	@Mapping(source = "employee", target = "secondarySkillList", qualifiedByName = "employeeToSecondarySkillList")
	@Mapping(source = "project.account.name", target = "accountName")
	@Mapping(source = "project.account.vertical.name", target = "vertical")
	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "allocation", target = "percentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "allocation", target = "startDate", qualifiedByName = "allocationStartDate")
	@Mapping(source = "releaseDate", target = "releaseDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "reportingManagerId.fullName", target = "reportingManager")
	@Mapping(source = "employee", target = "isPrimary", qualifiedByName = "primaryManagerName")
	@Mapping(source = "isBillable", target = "billable")
	@Mapping(source = "employee", target = "primryManagerName", qualifiedByName = "primaryManagerName")
	@Mapping(source = "project.employee1.fullName", target = "devGdm")
	@Mapping(source = "project.employee2.fullName", target = "qaGdm")
	public abstract ResourceViewResponseDto allocationToSwitchOverDto(Allocation allocation);

	public abstract List<ResourceViewResponseDto> allocationToAvailableResourceDtoList(List<Allocation> allocations);






}

