package com.empconn.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.earmark.EarmarkItemDto;
import com.empconn.persistence.entities.Earmark;
import com.empconn.repositories.AllocationRepository;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public abstract class EarmarkEarmarkItemDtoMapper {

	@Autowired
	AllocationRepository allocationRepository;

	@Mapping(target = "startDate", source = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(target = "endDate", source = "endDate", qualifiedByName = "DateToLocalDate")
	@Mapping(target = "salesforceIdList", source = "source", qualifiedByName = "earmarkToSalesforceIdList")
	@Mapping(target = "earmarkPercentage", source = "percentage")
	@Mapping(target = "earmarkProjectName", source = "source", qualifiedByName = "earmarkToEarmarkProjectName")
	@Mapping(target = "earmarkAccountName", source = "source", qualifiedByName = "earmarkToEarmarkAccountName")
	@Mapping(target = "availableFrom", source = "allocation", qualifiedByName = "allocationToAvailableFrom")
	@Mapping(target = "availablePercentage", source = "allocation", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(target = "allocatedProjectName", source = "allocation.project.name")
	@Mapping(target = "allocatedAccountName", source = "allocation.project.account.name")
	@Mapping(target = "secondarySkillList", source = "allocation.employee", qualifiedByName = "employeeToSecondarySkillList")
	@Mapping(target = "primarySkillList", source = "allocation.employee", qualifiedByName = "employeeToPrimarySkillList")
	@Mapping(target = "title", source = "allocation.employee.title.name")
	@Mapping(target = "empName", source = "allocation.employee.fullName")
	@Mapping(target = "empCode", source = "allocation.employee.empCode")
	abstract EarmarkItemDto earmarkToEarmarkItemDto(Earmark source);

	public List<EarmarkItemDto> earmarksToEarmarkItemDtos(List<Earmark> earmarks) {
		List<EarmarkItemDto> list = new ArrayList<>();
		if (!CollectionUtils.isEmpty(earmarks)) {
			for (final Earmark e : earmarks)
				list.add(earmarkToEarmarkItemDto(e));
		}
		list = list.stream().distinct().collect(Collectors.toList());
		return list;
	}

}
