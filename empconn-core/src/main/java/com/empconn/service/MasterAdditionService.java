package com.empconn.service;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.dto.SFEmployeeDto;
import com.empconn.exception.EmpConnException;
import com.empconn.persistence.entities.BusinessUnit;
import com.empconn.persistence.entities.Department;
import com.empconn.persistence.entities.Division;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Location;
import com.empconn.persistence.entities.NamedAuditable;
import com.empconn.persistence.entities.Title;
import com.empconn.repositories.BusinessUnitRepository;
import com.empconn.repositories.DepartmentRepository;
import com.empconn.repositories.DivisionRepository;
import com.empconn.repositories.LocationRepository;
import com.empconn.repositories.TitleRepository;
import com.empconn.security.SecurityUtil;

@Service
public class MasterAdditionService {

	private static final Logger logger = LoggerFactory.getLogger(MasterAdditionService.class);

	@Autowired
	private BusinessUnitRepository businessUnitRepository;

	@Autowired
	private DivisionRepository divisionRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private TitleRepository titleRepository;

	@Autowired
	private SecurityUtil securityUtil;

	public Employee addMissingMasterRecords(SFEmployeeDto sfEmployee, Employee employee) {


		try {

			BusinessUnit businessUnit = null;
			Division division = null;
			Department department = null;
			Location location = null;
			Title title = null;

			if(businessUnitShouldBeCreated(sfEmployee, employee))
				businessUnit = businessUnitRepository.save(initialize(BusinessUnit.class, sfEmployee.getBusinessUnit()));
			else
				businessUnit = getBusinessUnit(sfEmployee.getBusinessUnit());
			employee.setBusinessUnit(businessUnit);

			if(divisionShouldBeCreated(sfEmployee, employee))
				division = divisionRepository.save(initialize(Division.class, sfEmployee.getDivision()));
			else
				division = getDivision(sfEmployee.getDivision());
			employee.setDivision(division);

			if(departmentShouldBeCreated(sfEmployee, employee))
				department = departmentRepository.save(initialize(Department.class, sfEmployee.getDepartment()));
			else
				department = getDepartment(sfEmployee.getDepartment());
			employee.setDepartment(department);

			if(locationShouldBeCreated(sfEmployee, employee)) {
				final Location l = initialize(Location.class, sfEmployee.getLocationGroup());
				l.setHierarchy(locationRepository.findMaxHierarchy());
				location = locationRepository.save(l);
			}
			else
				location = getLocation(sfEmployee.getLocationGroup());
			employee.setLocation(location);

			if(titleShouldBeCreated(sfEmployee, employee))
				title = titleRepository.save(initialize(Title.class, sfEmployee.getTitle()));
			else
				title = getTitle(sfEmployee.getTitle());
			employee.setTitle(title);

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {

			logger.error("Exception in adding to master tables", e);
			throw new EmpConnException("MasterAdditionFailure");

		}



		return employee;
	}

	private boolean divisionShouldBeCreated(SFEmployeeDto sfEmployee, Employee employee) {
		//		return null == employee.getDivision() && StringUtils.isNotEmpty(sfEmployee.getDivision());
		return StringUtils.isNotEmpty(sfEmployee.getDivision()) && null == getDivision(sfEmployee.getDivision());
	}

	private boolean titleShouldBeCreated(SFEmployeeDto sfEmployee, Employee employee) {
		//		return null == employee.getTitle() && StringUtils.isNotEmpty(sfEmployee.getTitle());
		return StringUtils.isNotEmpty(sfEmployee.getTitle()) && null == getTitle(sfEmployee.getTitle());
	}

	private boolean locationShouldBeCreated(SFEmployeeDto sfEmployee, Employee employee) {
		//		return null == employee.getLocation() && StringUtils.isNotEmpty(sfEmployee.getLocationGroup());
		return StringUtils.isNotEmpty(sfEmployee.getLocationGroup()) && null == getLocation(sfEmployee.getLocationGroup());
	}

	private boolean departmentShouldBeCreated(SFEmployeeDto sfEmployee, Employee employee) {
		//		return null == employee.getDepartment() && StringUtils.isNotEmpty(sfEmployee.getDepartment());
		return StringUtils.isNotEmpty(sfEmployee.getDepartment()) && null == getDepartment(sfEmployee.getDepartment());
	}

	private boolean businessUnitShouldBeCreated(SFEmployeeDto sfEmployee, Employee employee) {
		//		return null == employee.getBusinessUnit() && StringUtils.isNotEmpty(sfEmployee.getBusinessUnit());
		return StringUtils.isNotEmpty(sfEmployee.getBusinessUnit()) && null == getBusinessUnit(sfEmployee.getBusinessUnit());
	}

	@SuppressWarnings("unchecked")
	private <T> T initialize(Class<T> class1, String name) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		logger.info("Adding Master record {} to {} table", name, class1.getSimpleName());
		final NamedAuditable<Long> instance = (NamedAuditable<Long>)class1.getDeclaredConstructor().newInstance();
		instance.setIsActive(true);
		instance.setCreatedBy(securityUtil.getLoggedInEmployee().getEmployeeId());
		instance.setName(name);
		return (T)instance;
	}


	public Location getLocation(String locationGroup) {
		return locationRepository.findByNameIgnoringCase(StringUtils.trim(locationGroup));
	}

	public Title getTitle(String title) {
		return titleRepository.findByNameIgnoringCase(StringUtils.trim(title));
	}

	public BusinessUnit getBusinessUnit(String businessUnit) {
		return businessUnitRepository.findByNameIgnoringCase(StringUtils.trim(businessUnit));
	}

	public Division getDivision(String division) {
		return divisionRepository.findByNameIgnoringCase(StringUtils.trim(division));
	}

	public Department getDepartment(String department) {
		return departmentRepository.findByNameIgnoringCase(StringUtils.trim(department));
	}


}
