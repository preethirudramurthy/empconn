package com.empconn.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.allocation.AllocationForSwitchOverDto;
import com.empconn.dto.allocation.AllocationSummaryDto;
import com.empconn.dto.allocation.EarmarkForAllocationDto;
import com.empconn.dto.allocation.ExistingAllocationDto;
import com.empconn.dto.allocation.SwitchOverDtlsDto;
import com.empconn.dto.earmark.EarmarkAvailabilityDto;
import com.empconn.dto.earmark.EarmarkSummaryDto;
import com.empconn.dto.earmark.ResourceAvailabilitySummaryDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.EarmarkSalesforceIdentifier;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Opportunity;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.SalesforceIdentifier;
import com.empconn.repositories.EarmarkRepository;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public abstract class AllocationMapper {

	@Autowired
	EarmarkRepository earMarkRepository;

	@Mapping(source = "employee", target = "fullName", qualifiedByName = "employeeToFullName")
	@Mapping(source = "employee.empCode", target = "empCode")
	@Mapping(source = "employee.title.name", target = "title")
	@Mapping(source = "allocation", target = "availabilityList", qualifiedByName = "allocationToResourceAvailSummaryDtoList")
	public abstract EarmarkAvailabilityDto allocationToToEarmarkAvailabilityDto(Allocation allocation);

	@Mapping(source = "project.account.name", target = "accountName")
	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "allocation", target = "percentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "allocation", target = "availableFrom", qualifiedByName = "allocationToAvailableFrom")
	public abstract ResourceAvailabilitySummaryDto allocationToResourceAvailabilitySummaryDto(Allocation allocation);

	@Mapping(source = "allocationId", target = "allocationId")
	@Mapping(source = "employee.employeeId", target = "resourceId")
	@Mapping(source = "employee.empCode", target = "empCode")
	@Mapping(source = "employee.fullName", target = "empName")
	@Mapping(source = "employee.title.name", target = "title")
	@Mapping(source = "project.name", target = "projectName")
	@Mapping(source = "project.account.name", target = "accountName")
	@Mapping(source = "allocation", target = "projectLocation", qualifiedByName = "locationName")
	@Mapping(source = "allocation", target = "allocationPercentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "allocation", target = "startDate", qualifiedByName = "allocationStartDate")
	@Mapping(source = "releaseDate", target = "releaseDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "employee", target = "primarySkillList", qualifiedByName = "employeeToPrimarySkillList")
	@Mapping(source = "employee", target = "secondarySkillList", qualifiedByName = "employeeToSecondarySkillList")
	@Mapping(target = "allocationStatus", expression = "java(getAllocationStatus(allocation))")
	public abstract AllocationForSwitchOverDto allocationToSwitchOverDto(Allocation allocation);

	@Named("allocationToResourceAvailSummaryDtoList")
	List<ResourceAvailabilitySummaryDto> allocationToResourceAvailSummaryDtoList(Allocation allocation) {
		final List<ResourceAvailabilitySummaryDto> resourceAvailabilitySummaryDtoList = new ArrayList<>();
		resourceAvailabilitySummaryDtoList.add(allocationToResourceAvailabilitySummaryDto(allocation));
		return resourceAvailabilitySummaryDtoList;
	}

	public abstract List<AllocationForSwitchOverDto> allocationToAvailableResourceDtoList(List<Allocation> allocations);

	@Mapping(target = "projectCode", expression = "java(getCustomizedId(source))")
	@Mapping(source = "source", target = "salesforceIdList", qualifiedByName = "earmarkToSalesforceIdList")
	@Mapping(source = "source", target = "accountName", qualifiedByName = "earmarkToEarmarkAccountName")
	@Mapping(source = "source", target = "projectName", qualifiedByName = "earmarkToEarmarkProjectName")
	@Mapping(source = "employee2.fullName", target = "managerName")
	public abstract EarmarkSummaryDto earMarkToEarmarkSummaryDto(Earmark source);

	public abstract List<EarmarkSummaryDto> earMarkToEarmarkSummaryDtoList(List<Earmark> earMarks);

	@Named("getCustomizedId")
	String getCustomizedId(Earmark earMark) {
		if (earMark.getProject() != null && earMark.getProject().getName() != null
				&& !"".equalsIgnoreCase(earMark.getProject().getName().trim())) {
			return customizeProjectId(earMark.getProject());
		} else {
			return "";
		}
	}

	@Named("customizeProjectId")
	String customizeProjectId(Project project) {
		String projectId = null;
		if (project != null) {
			final Calendar calendar = new GregorianCalendar();
			calendar.setTime(project.getStartDate());
			final String accountName = project.getAccount().getName() == null ? ""
					: StringUtils.substring(project.getAccount().getName(), 0, 3);
			final String projectName = project.getName() == null ? "" : StringUtils.substring(project.getName(), 0, 3);
			projectId = calendar.get(Calendar.YEAR) + accountName + projectName + "_" + project.getProjectId();
		}
		return projectId;
	}

	@Named("customizeOpportunityId")
	String customizeOpportunityId(Opportunity opportunity) {
		String opportunityId = null;
		if (opportunity != null) {
			final Calendar calendar = new GregorianCalendar();
			calendar.setTime(opportunity.getCreatedOn());
			final String accountName = opportunity.getAccount() == null
					? StringUtils.substring(opportunity.getAccountName(), 0, 3)
							: StringUtils.substring(opportunity.getAccount().getName(), 0, 3);
					final String projectName = opportunity.getName() == null ? ""
							: StringUtils.substring(opportunity.getName(), 0, 3);
					opportunityId = calendar.get(Calendar.YEAR) + accountName + projectName + "_"
							+ opportunity.getOpportunityId();
		}
		return opportunityId;
	}

	@Named("customizeProjectId")
	String customizeProjectName(Allocation allocation) {
		String projectId = null;
		if (allocation.getProject() != null) {
			final Calendar calendar = new GregorianCalendar();
			calendar.setTime(allocation.getProject().getStartDate());
			final String accountName = allocation.getProject().getAccount().getName() == null ? ""
					: StringUtils.substring(allocation.getProject().getAccount().getName(), 0, 3);
			final String projectName = allocation.getProject().getName() == null ? ""
					: StringUtils.substring(allocation.getProject().getName(), 0, 3);
			projectId = calendar.get(Calendar.YEAR) + accountName + projectName + "_"
					+ allocation.getProject().getProjectId();
		}
		return projectId;
	}

	@Mapping(source = "allocation.allocationId", target = "allocationId")
	@Mapping(source = "allocation.employee.employeeId", target = "resourceId")
	@Mapping(source = "allocation.employee.empCode", target = "empCode")
	@Mapping(source = "allocation.employee", target = "empName", qualifiedByName = "employeeToFullName")
	@Mapping(source = "allocation.employee.title.name", target = "title")
	@Mapping(source = "allocation", target = "availablePercentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "toProject.endDate", target = "projectEndDate", qualifiedByName = "DateToLocalDateTime")
	@Mapping(source = "toProject", target = "projectSalesforceIdList", qualifiedByName = "projectToSalesForceIdList")
	@Mapping(source = "toProject", target = "managerList", qualifiedByName = "projectToManagerList")
	@Mapping(source = "allocationSummaryDto", target = "allocationSummary")
	@Mapping(source = "exitAllocationDto", target = "existingAllocationInThisProject")
	@Mapping(target = "movementDate", expression = "java(CommonQualifiedMapper.getDateOfMovement())")
	public abstract SwitchOverDtlsDto allocationToAllocationDetailsDto(Allocation allocation, Project toProject,
			List<AllocationSummaryDto> allocationSummaryDto, ExistingAllocationDto exitAllocationDto);

	@Named("projectToSalesForceIdList")
	List<String> projectToSalesForceIdList(Project project) {
		List<String> salesForceIdentifierIdList = new ArrayList<>();
		if (project.getSalesforceIdentifiers() != null) {
			salesForceIdentifierIdList = project.getSalesforceIdentifiers().stream()
					.filter(SalesforceIdentifier::getIsActive).map(SalesforceIdentifier::getValue)
					.collect(Collectors.toList());
		}
		return salesForceIdentifierIdList;
	}

	@Named("projectToManagerList")
	List<Map<String, String>> projectToManagerList(Project project) {
		return project.getProjectLocations().stream().map(x -> getProjectManagers.apply(x)).flatMap(List::stream)
				.collect(Collectors.toList());
	}

	Function<ProjectLocation, List<Map<String, String>>> getProjectManagers = projectLocation -> {
		final List<Map<String, String>> locationManagerList = new ArrayList<>();
		final Map<String, Employee> allManagers = projectLocation.getAllManagers();
		allManagers.entrySet().stream().forEach(y -> {
			if (y.getValue() != null) {
				final Map<String, String> locationManagerMap = new HashMap<>();
				locationManagerMap.put("projectLocationName", projectLocation.getLocation().getName());
				locationManagerMap.put("workgroup", y.getKey());
				locationManagerMap.put("mangerName", y.getValue().getFullName());
				locationManagerList.add(locationManagerMap);
			}

		});

		return locationManagerList;
	};

	@Mapping(source = "earmark.earmarkId", target = "earmarkId")
	@Mapping(source = "earmark", target = "projectOppName", qualifiedByName = "projectOppName")
	@Mapping(source = "earmark.allocation.allocationId", target = "allocationId")
	@Mapping(source = "earmark.allocation.employee.employeeId", target = "resourceId")
	@Mapping(source = "earmark.allocation.employee.empCode", target = "empCode")
	@Mapping(source = "earmark.allocation.employee", target = "empName", qualifiedByName = "employeeToFullName")
	@Mapping(source = "earmark.allocation.employee.title.name", target = "title")
	@Mapping(source = "earmark.percentage", target = "earmarkPercentage")
	@Mapping(source = "earmark.allocation", target = "availablePercentage", qualifiedByName = "mergedAllocatedPercentage")
	@Mapping(source = "earmark.billable", target = "billable")
	@Mapping(source = "project.endDate", target = "projectEndDate", qualifiedByName = "DateToLocalDate")
	@Mapping(source = "earmark", target = "isOpp", qualifiedByName = "opportunity")
	@Mapping(target = "earmarkSalesforceIdList", expression = "java(earmarkToEarmarkSalesforceIdList(earmark, project))")
	@Mapping(target = "earmarkReleaseDate", expression = "java(earmarkToEarmarkReleaseDate(earmark, project))")
	@Mapping(target = "movementDate", expression = "java(CommonQualifiedMapper.getDateOfMovement())")
	@Mapping(source = "project", target = "managerList", qualifiedByName = "projectToManagerList")
	@Mapping(source = "allocationSummaryDto", target = "allocationSummary")
	@Mapping(source = "exitAllocationDto", target = "existingAllocationInThisProject")
	@Mapping(source = "project.salesforceIdentifiers", target = "projectSalesforceIdList", qualifiedByName = "projectSfToSfValueList")
	public abstract EarmarkForAllocationDto earmarkToEarmarkForAllocationDto(Earmark earmark,
			List<AllocationSummaryDto> allocationSummaryDto, ExistingAllocationDto exitAllocationDto, Project project);

	@Named("projectOppName")
	String projectOppName(Earmark earmark) {
		String st = "";
		if (earmark.getProject() != null) {
			st = earmark.getProject().getName();
		}
		if (earmark.getOpportunity() != null) {
			st = earmark.getOpportunity().getName();
		}
		return st;
	}

	@Named("opportunity")
	Boolean opportunity(Earmark earmark) {
		Boolean flag = false;
		if (earmark.getOpportunity() != null) {
			flag = true;
		}
		return flag;
	}

	@Named("projectSfToSfValueList")
	List<String> projectSfToSfValueList(Set<SalesforceIdentifier> sfs) {
		if (CollectionUtils.isEmpty(sfs))
			return new ArrayList<>();
		return sfs.stream().map(SalesforceIdentifier::getValue).collect(Collectors.toList());
	}

	@Named("earmarkToEarmarkSalesforceIdList")
	List<String> earmarkToEarmarkSalesforceIdList(Earmark earmark, Project project) {
		if (CollectionUtils.isEmpty(earmark.getEarmarkSalesforceIdentifiers()) || (earmark.getProject() != null
				&& !earmark.getProject().getProjectId().equals(project.getProjectId())))
			return new ArrayList<>();

		final List<String> earmarkSfList = earmark.getEarmarkSalesforceIdentifiers().stream()
				.map(EarmarkSalesforceIdentifier::getValue).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(project.getSalesforceIdentifiers())) {
			final List<String> projectSfList = project.getSalesforceIdentifiers().stream()
					.map(SalesforceIdentifier::getValue).collect(Collectors.toList());
			earmarkSfList.removeAll(projectSfList);
		}
		return earmarkSfList;
	}

	public LocalDateTime earmarkToEarmarkReleaseDate(Earmark earmark, Project project) {
		if ((earmark.getProject() != null && earmark.getProject().getProjectId().equals(project.getProjectId()))
				|| earmark.getOpportunity() != null)
			return CommonQualifiedMapper.dateToLocalDateTime(earmark.getEndDate());
		return CommonQualifiedMapper.dateToLocalDateTime(project.getEndDate());
	}

	public String getAllocationStatus(Allocation allocation) {
		if (allocation.getAllocationStatus() == null)
			return ("---");
		else if (allocation.getAllocationStatus().getStatus().equals("Allocated"))
			return ("--");
		else
			return allocation.getAllocationStatus().getStatus();
	}
}