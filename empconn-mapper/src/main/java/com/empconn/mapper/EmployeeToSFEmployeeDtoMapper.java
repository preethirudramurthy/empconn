package com.empconn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.util.StringUtils;

import com.empconn.dto.SFEmployeeDto;
import com.empconn.persistence.entities.BusinessUnit;
import com.empconn.persistence.entities.Department;
import com.empconn.persistence.entities.Division;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Location;
import com.empconn.persistence.entities.Title;
import com.empconn.repositories.BusinessUnitRepository;
import com.empconn.repositories.DepartmentRepository;
import com.empconn.repositories.DivisionRepository;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.LocationRepository;
import com.empconn.repositories.TitleRepository;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring")
public abstract class EmployeeToSFEmployeeDtoMapper extends BaseMapper {

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private TitleRepository titleRepository;

	@Autowired
	private BusinessUnitRepository businessUnitRepository;

	@Autowired
	private DivisionRepository divisionRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	SecurityUtil securityUtil;

	@Mapping(source = "lwd", target = "lastWorkingDay", qualifiedByName = "String_ddmmyyyy_ToDate")
	@Mapping(source = "gdmId", target = "employee2", qualifiedByName = "getEmployee")
	@Mapping(target = "createdBy", expression = "java(securityUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(source = "locationGroup", target = "location", qualifiedByName = "getLocation")
	@Mapping(source = "title", target = "title", qualifiedByName = "getTitle")
	@Mapping(source = "businessUnit", target = "businessUnit", qualifiedByName = "getBusinessUnit")
	@Mapping(source = "division", target = "division", qualifiedByName = "getDivision")
	@Mapping(source = "department", target = "department", qualifiedByName = "getDepartment")
	@Mapping(source = "dateOfJoining", target = "dateOfJoining", qualifiedByName = "String_ddmmyyyy_ToDate")
	@Mapping(source = "active", target = "isActive")
	@Mapping(source = "manager", target = "isManager")
	@Mapping(source = "reportingManagerId", target = "ndReportingManagerId", qualifiedByName = "getEmployee")
	public abstract Employee sfEmployeeDtoToEmployee(SFEmployeeDto source);

	@Mapping(source = "lwd", target = "lastWorkingDay", qualifiedByName = "String_ddmmyyyy_ToDate")
	@Mapping(source = "gdmId", target = "employee2", qualifiedByName = "getEmployee")
	@Mapping(target = "modifiedBy", expression = "java(securityUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "location", ignore = true)
	@Mapping(target = "title", ignore = true)
	@Mapping(target = "businessUnit", ignore = true)
	@Mapping(target = "division", ignore = true)
	@Mapping(target = "department", ignore = true)
	@Mapping(source = "dateOfJoining", target = "dateOfJoining", qualifiedByName = "String_ddmmyyyy_ToDate")
	@Mapping(source = "active", target = "isActive")
	@Mapping(source = "manager", target = "isManager")
	public abstract Employee sfEmployeeDtoToEmployee(SFEmployeeDto source, @MappingTarget Employee existingEmployee);

	@Named("getLocation")
	public Location getLocation(String locationGroup) {
		return locationRepository.findByNameIgnoringCase(StringUtils.trim(locationGroup));
	}

	@Named("getTempEmployee")
	public Long getTempEmployee(String temp) {
		return 100L;
	}

	@Named("getTitle")
	public Title getTitle(String title) {
		return titleRepository.findByNameIgnoringCase(StringUtils.trim(title));
	}

	@Named("getBusinessUnit")
	public BusinessUnit getBusinessUnit(String businessUnit) {
		return businessUnitRepository.findByNameIgnoringCase(StringUtils.trim(businessUnit));
	}

	@Named("getDivision")
	public Division getDivision(String division) {
		return divisionRepository.findByNameIgnoringCase(StringUtils.trim(division));
	}

	@Named("getDepartment")
	public Department getDepartment(String department) {
		return departmentRepository.findByNameIgnoringCase(StringUtils.trim(department));
	}

	@Named("getEmployee")
	public Employee getEmployee(String empCode) {
		return employeeRepository.findByEmpCodeAndIsActiveTrue(empCode);
	}

}