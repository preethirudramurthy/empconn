package com.empconn.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.RequestDto;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.NdRequest;
import com.empconn.persistence.entities.NdRequestSalesforceIdentifier;
import com.empconn.persistence.entities.Project;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.ProjectRepository;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring")
public abstract class ResourceRequestDtoToNdRequestMapper {

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Mapping(source = "resourceId", target = "employee1", qualifiedByName = "getEmployee")
	@Mapping(source = "projectId", target = "project", qualifiedByName = "getProject")
	@Mapping(source = "percentage", target = "percentage")
	@Mapping(source = "startDate", target = "startDate")
	@Mapping(source = "releaseDate", target = "releaseDate")
	@Mapping(source = "billable", target = "isBillable")
	@Mapping(source = "isActive", target = "isActive")
	@Mapping(source = "reportingManagerId", target = "employee2", qualifiedByName = "getEmployee")
	@Mapping(target = "ndRequestSalesforceIdentifiers", expression = "java(salesforceIdListToNdRequestSfs(requestDto.getExtraSalesforceIdList(), ndRequest))")
	public abstract NdRequest resourceRequestDtoToNdRequest(RequestDto requestDto);


	@Named("salesforceIdListToNdRequestSfs")
	public List<NdRequestSalesforceIdentifier> salesforceIdListToNdRequestSfs(List<String> sfList, NdRequest ndRequest) {
		if (!CollectionUtils.isEmpty(sfList)) {
			final List<NdRequestSalesforceIdentifier> list = new ArrayList<>();
			for (final String s : sfList) {
				list.add(salesforceValueToNdRequestSalesforceId(s, ndRequest));
			}
			return list;
		}
		return Collections.emptyList();
	}

	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "ndRequest", target = "ndRequest")
	@Mapping(target = "isActive", constant = "true")
	@Mapping(target = "modifiedOn", ignore = true)
	@Mapping(target = "modifiedBy", ignore = true)
	public abstract NdRequestSalesforceIdentifier salesforceValueToNdRequestSalesforceId(String value, NdRequest ndRequest);

	@Named("getProject")
	public Project getProject(Long projectId) {
		Optional<Project> projectOpt = projectRepository.findById(projectId);
		return (projectOpt.isPresent())? projectOpt.get():null;
	}

	@Named("getEmployee")
	public Employee getEmployee(Long resourceId) {
		Optional<Employee> empOpt = employeeRepository.findById(resourceId);
		return (empOpt.isPresent())? empOpt.get():null;
	}
}
