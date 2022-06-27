package com.empconn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.earmark.EarmarkDetailsDto;
import com.empconn.persistence.entities.Earmark;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public abstract class EarmarkToEarmarkDetailsDtoMapper {

	@Mapping(source = "allocation.employee.fullName", target = "empName")
	@Mapping(source = "allocation.employee.title.name", target = "title")
	@Mapping(source = "allocation.project.account.name", target = "accountName")
	@Mapping(source = "allocation.project.name", target = "projectName")
	@Mapping(source = "allocation", target = "availablePercentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "allocation", target = "availableFrom", qualifiedByName = "allocationToAvailableFrom")
	@Mapping(source = "source", target = "earmarkAccountName", qualifiedByName = "earmarkToEarmarkAccountName")
	@Mapping(source = "source", target = "earmarkProjectName", qualifiedByName = "earmarkToEarmarkProjectName")
	@Mapping(target = "isOpp", expression = "java(source.getOpportunity()!=null)")
	@Mapping(source = "source", target = "salesforceIdList", qualifiedByName = "earmarkToSalesforceIdList")
	@Mapping(source = "employee2", target = "managerName", qualifiedByName = "employeeToFullName")
	@Mapping(source = "endDate", target = "endDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "startDate", target = "startDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "percentage", target = "earmarkPercentage")
	@Mapping(source = "billable", target = "billable")
	@Mapping(source = "isClientInterviewNeeded", target = "clientInterviewNeeded")
	public abstract EarmarkDetailsDto earmarksToEarmarkDetailsDtos(Earmark source);

}
