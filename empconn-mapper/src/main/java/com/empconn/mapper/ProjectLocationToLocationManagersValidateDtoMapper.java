package com.empconn.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import com.empconn.dto.LocationManagersValidateDto;
import com.empconn.dto.ManagerInfoDto;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Location;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.repositories.EmployeeRepository;
import com.empconn.repositories.LocationRepository;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public abstract class ProjectLocationToLocationManagersValidateDtoMapper {

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Mapping(source = "locationManagersValidateDto.manager2", target = "employee5", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "locationManagersValidateDto.manager1", target = "employee4", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "locationManagersValidateDto.uiManager", target = "employee3", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "locationManagersValidateDto.qaManager", target = "employee2", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "locationManagersValidateDto.devManager", target = "employee1", qualifiedByName = "employeeIdToEmployee")
	@Mapping(source = "location", target = "location", qualifiedByName = "locationUnitValueToLocation")
	@Mapping(target = "isActive", constant = "true")
	public abstract ProjectLocation locationManagersDtoToProjectLocation(
			LocationManagersValidateDto locationManagersValidateDto);

	@Named("locationListToProjectLocations")
	public abstract Set<ProjectLocation> locationsManagersDtoToProjectLocations(
			List<LocationManagersValidateDto> locationsManagersValidateDto);

	@Named("locationUnitValueToLocation")
	Location locationUnitValueToLocation(UnitValue location) {
		if (location != null && location.getId() != null) {
			return locationRepository.findById(Integer.valueOf(location.getId())).get();
		}
		return null;

	}

	@Named("employeeIdToEmployee")
	Employee employeeIdToEmployee(ManagerInfoDto manager) {
		if (manager != null && manager.getId() != null) {
			return employeeRepository.findById(Long.valueOf(manager.getId())).get();
		}
		return null;
	}

}
