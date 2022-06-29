package com.empconn.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.dto.LocationManagersDto;
import com.empconn.dto.LocationManagersValidateDto;
import com.empconn.dto.ManagerInfoDto;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Location;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.LocationRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.security.SecurityUtil;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public abstract class ProjectLocationDtoMapper {

	@Autowired
	ProjectLocationRespository projectLocationRespository;

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "manager2", target = "employee5", qualifiedByName = "managerInfoToEmployee")
	@Mapping(source = "manager1", target = "employee4", qualifiedByName = "managerInfoToEmployee")
	@Mapping(source = "uiManager", target = "employee3", qualifiedByName = "managerInfoToEmployee")
	@Mapping(source = "qaManager", target = "employee2", qualifiedByName = "managerInfoToEmployee")
	@Mapping(source = "devManager", target = "employee1", qualifiedByName = "managerInfoToEmployee")
	@Mapping(source = "location", target = "location", qualifiedByName = "locationUnitValueToLocation")
	@Mapping(target = "isActive", constant = "true")
	public abstract ProjectLocation locationManagersDtoToProjectLocation(
			LocationManagersValidateDto locationManagersDto);

	@Mapping(target = "modifiedBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "modifiedOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "manager2", target = "employee5", qualifiedByName = "managerInfoToEmployee")
	@Mapping(source = "manager1", target = "employee4", qualifiedByName = "managerInfoToEmployee")
	@Mapping(source = "uiManager", target = "employee3", qualifiedByName = "managerInfoToEmployee")
	@Mapping(source = "qaManager", target = "employee2", qualifiedByName = "managerInfoToEmployee")
	@Mapping(source = "devManager", target = "employee1", qualifiedByName = "managerInfoToEmployee")
	@Mapping(source = "location", target = "location", qualifiedByName = "locationUnitValueToLocation")
	public abstract ProjectLocation locationManagersDtoToProjectLocation(LocationManagersValidateDto dto,
			@MappingTarget ProjectLocation projectLocation);

	public Set<ProjectLocation> locationsManagersDtoToProjectLocations(
			List<LocationManagersValidateDto> locationsManagersDto) {
		if (locationsManagersDto == null) {
			return Collections.emptySet();
		}
		final Set<ProjectLocation> set = new HashSet<>();
		for (final LocationManagersValidateDto dto : locationsManagersDto) {
			if (dto.getProjectLocationId() != null && projectLocationRespository
					.findById(Long.valueOf(dto.getProjectLocationId())).isPresent()) {
				final ProjectLocation location = projectLocationRespository
						.findById(Long.valueOf(dto.getProjectLocationId())).get();
				set.add(locationManagersDtoToProjectLocation(dto, location));
			} else
				set.add(locationManagersDtoToProjectLocation(dto));
		}
		if (set.isEmpty())
			return Collections.emptySet();
		return set;
	}

	@Mapping(source = "location", target = "location", qualifiedByName = "locationToUnitValue")
	@Mapping(source = "employee1", target = "devManager", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee2", target = "qaManager", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee3", target = "uiManager", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee4", target = "manager1", qualifiedByName = "employeeToUnitValue")
	@Mapping(source = "employee5", target = "manager2", qualifiedByName = "employeeToUnitValue")
	public abstract LocationManagersDto projectLocationToLocationManagersDto(ProjectLocation projectLocation);

	public abstract List<LocationManagersDto> projectLocationsToLocationManagersDto(
			Set<ProjectLocation> projectLocation);

	@Named("employeeToUnitValue")
	UnitValue locationToUnitValue(Employee employee) {
		if (employee != null) {
			final String firstName = (null == employee.getFirstName()) ? "" : employee.getFirstName();
			final String lastName = (null == employee.getLastName()) ? "" : employee.getLastName();
			return new UnitValue(employee.getEmployeeId().toString(), firstName + " " + lastName);
		}
		return null;
	}

	@Named("locationToUnitValue")
	UnitValue locationToUnitValue(Location location) {
		if (location != null) {
			return new UnitValue(location.getLocationId().toString(), location.getName());
		}
		return null;
	}

	@Named("locationUnitValueToLocation")
	Location locationUnitValueToLocation(UnitValue location) {
		if (location != null && location.getId() != null && locationRepository.findById(Integer.valueOf(location.getId())).isPresent())
		{
			return locationRepository.findById(Integer.valueOf(location.getId())).get();
		}
		return null;

	}

	@Named("managerInfoToEmployee")
	Employee managerInfoToEmployee(ManagerInfoDto manager) {
		if (manager != null && manager.getId() != null && employeeRepository.findById(Long.valueOf(manager.getId())).isPresent()) {
			return employeeRepository.findById(Long.valueOf(manager.getId())).get();
		}
		return null;
	}

}
