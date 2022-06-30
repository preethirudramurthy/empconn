package com.empconn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.empconn.dto.map.MapProjectDto;
import com.empconn.enums.ProjectStatus;
import com.empconn.persistence.entities.Project;

@Mapper(componentModel = "spring")
public abstract class ProjectMapProjectDtoMapper {

	@Mapping(source = "horizontal.name", target = "horizontal")
	@Mapping(source = "name", target = "projectName")
	@Mapping(source = "operatingSystem", target = "os")
	@Mapping(source = "database", target = "db")
	@Mapping(source = "currentStatus", target = "isProjectActive", qualifiedByName = "currentStatusToIsProjectActive")
	@Mapping(source = "account.name", target = "account")
	@Mapping(source = "account.mapAccountId", target = "clientId")
	@Mapping(source = "description", target = "projectDescription")
	@Mapping(source = "mapProjectId", target = "id")
	public abstract MapProjectDto projectToMapProjectDto(Project source);

	@Named("currentStatusToIsProjectActive")
	public boolean currentStatusToIsProjectActive(String status) {
		if (status.equals(ProjectStatus.PMO_APPROVED.name()))
			return true;
		
		return false;
	}
}
