package com.empconn.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.earmark.EarmarkInfoDto;
import com.empconn.dto.earmark.EarmarkOppurtunityDto;
import com.empconn.dto.earmark.EarmarkProjectDto;
import com.empconn.persistence.entities.Account;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.EarmarkSalesforceIdentifier;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Opportunity;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.SalesforceIdentifier;
import com.empconn.persistence.entities.Vertical;
import com.empconn.repositories.AccountRepository;
import com.empconn.repositories.AllocationRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.repositories.VerticalRepository;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring", uses = { CommonQualifiedMapper.class })
public abstract class ProjectDtoToEarmarkToEarmarkMapper {

	@Autowired
	AllocationRepository allocationRepository;

	@Autowired
	VerticalRepository verticalRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ProjectRepository projectRepository;

	public List<Earmark> earmarkProjectDtoToEarmarkList(EarmarkProjectDto earmarkProjectDto) {
		final List<Earmark> earmarks = new ArrayList<>();
		final Project project = projectRepository.findByProjectId(Long.valueOf(earmarkProjectDto.getProjectId()));

		if (!CollectionUtils.isEmpty(earmarkProjectDto.getSalesforceIdList())) {
			final List<String> projectSfIds = project.getSalesforceIdentifiers().stream()
					.map(SalesforceIdentifier::getValue).collect(Collectors.toList());
			earmarkProjectDto.getSalesforceIdList().removeAll(projectSfIds);
		}

		Employee manager = null;
		if (earmarkProjectDto.getManagerId() != null && employeeRepository.findById(Long.parseLong(earmarkProjectDto.getManagerId())).isPresent())
			manager = employeeRepository.findById(Long.parseLong(earmarkProjectDto.getManagerId())).get();
		else
			manager = jwtEmployeeUtil.getLoggedInEmployee();

		for (final EarmarkInfoDto e : earmarkProjectDto.getEarmarkList()) {
			final Earmark earmark = earmarkProjectDtoToEarmark(earmarkProjectDto, project, manager);
			earmarkInfoDtoToEarmark(e, earmark);
			earmarks.add(earmark);
		}
		return earmarks;
	}

	@Mapping(source = "clientInterviewNeeded", target = "isClientInterviewNeeded")
	@Mapping(source = "allocationId", target = "allocation", qualifiedByName = "allocationIdToAllocation")
	public abstract Earmark earmarkInfoDtoToEarmark(EarmarkInfoDto source, @MappingTarget Earmark earmark);

	@Mapping(target = "earmarkSalesforceIdentifiers", expression = "java(salesforceIdListToEarmarkSfs(source.getSalesforceIdList(), earmark))")
	@Mapping(source = "manager", target = "employee2")
	@Mapping(source = "project", target = "project")
	@Mapping(source = "source.startDate", target = "startDate")
	@Mapping(source = "source.endDate", target = "endDate")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "modifiedOn", ignore = true)
	@Mapping(target = "modifiedBy", ignore = true)
	public abstract Earmark earmarkProjectDtoToEarmark(EarmarkProjectDto source, Project project, Employee manager);

	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "earmark", target = "earmark")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(target = "modifiedOn", ignore = true)
	@Mapping(target = "modifiedBy", ignore = true)
	public abstract EarmarkSalesforceIdentifier salesforceValueToEarmarkSalesforceId(String value, Earmark earmark);

	@Mapping(source = "opportunityName", target = "name")
	@Mapping(source = "verticalId", target = "vertical", qualifiedByName = "VerticalIdToVertical")
	@Mapping(source = "devGDMId", target = "employee1", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "qaGDMId", target = "employee2", qualifiedByName = "employeeIdToEmployee")
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(target = "account", source = "accountName", qualifiedByName = "getAccountId")
	public abstract Opportunity earmarkOppurtunityDtoToOpportunity(EarmarkOppurtunityDto earmarkOppurtunityDto);

	public List<Earmark> earmarkOppurtunityDtoToEarmarkList(EarmarkOppurtunityDto earmarkOppurtunityDto,
			Opportunity opportunity) {

		final List<Earmark> earmarks = new ArrayList<>();

		Employee manager = null;
		if (earmarkOppurtunityDto.getManagerId() != null && employeeRepository.findById(Long.parseLong(earmarkOppurtunityDto.getManagerId())).isPresent())
			manager = employeeRepository.findById(Long.parseLong(earmarkOppurtunityDto.getManagerId())).get();
		else
			manager = jwtEmployeeUtil.getLoggedInEmployee();

		for (final EarmarkInfoDto e : earmarkOppurtunityDto.getEarmarkList()) {
			final Earmark earmark = earmarkOppurtunityDtoToEarmark(earmarkOppurtunityDto, opportunity, manager);
			earmarkInfoDtoToEarmark(e, earmark);
			earmarks.add(earmark);
		}
		return earmarks;
	}

	@Mapping(target = "earmarkSalesforceIdentifiers", expression = "java(salesforceIdListToEarmarkSfs(source.getSalesforceIdList(), earmark))")
	@Mapping(source = "manager", target = "employee2")
	@Mapping(source = "opportunity", target = "opportunity")
	@Mapping(source = "source.startDate", target = "startDate")
	@Mapping(source = "source.endDate", target = "endDate")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "modifiedOn", ignore = true)
	@Mapping(target = "modifiedBy", ignore = true)
	public abstract Earmark earmarkOppurtunityDtoToEarmark(EarmarkOppurtunityDto source, Opportunity opportunity,
			Employee manager);

	@Named("employeeIdToEmployee")
	Employee employeeIdToEmployee(String employeeId) {
		if (employeeId != null && employeeRepository.findById(Long.valueOf(employeeId)).isPresent()) {
			return employeeRepository.findById(Long.valueOf(employeeId)).get();
		}
		return null;
	}

	@Named("VerticalIdToVertical")
	public Vertical verticalIdToVertical(String verticalId) {
		final Optional<Vertical> vertical = verticalRepository.findById(Integer.parseInt(verticalId));
		if (vertical.isPresent())
			return vertical.get();
		return null;
	}

	@Named("getAccountId")
	public Account getAccountId(String accountName) {
		final Optional<Account> account = accountRepository.findByName(accountName);
		if (account.isPresent())
			return account.get();
		return null;
	}

	@Named("allocationIdToAllocation")
	public Allocation allocationIdToAllocation(String allocationId) {
		final Optional<Allocation> allocation = allocationRepository.findById(Long.valueOf(allocationId));
		if (allocation.isPresent())
			return allocation.get();
		return null;
	}

	@Named("salesforceIdListToEarmarkSfs")
	public List<EarmarkSalesforceIdentifier> salesforceIdListToEarmarkSfs(List<String> sfList, Earmark earmark) {
		if (!CollectionUtils.isEmpty(sfList)) {
			final List<EarmarkSalesforceIdentifier> list = new ArrayList<>();
			for (final String s : sfList) {
				list.add(salesforceValueToEarmarkSalesforceId(s, earmark));
			}
			return list;
		}
		return Collections.emptyList();
	}
}
