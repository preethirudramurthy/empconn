package com.empconn.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.empconn.dto.map.MapAccountDto;
import com.empconn.dto.map.MapProjectDto;
import com.empconn.enums.AccountCategory;
import com.empconn.mapper.AccountMapAccountDtoMapper;
import com.empconn.mapper.ProjectMapProjectDtoMapper;
import com.empconn.persistence.entities.Project;

@Component
public class MapHandler {

	@Autowired
	MapService mapService;

	@Autowired
	AccountMapAccountDtoMapper accountMapper;

	@Autowired
	ProjectMapProjectDtoMapper projectMapper;

	public void integrateWithMap(Project project, String action) {
		if (project.getAccount().getCategory().equals(AccountCategory.INTERNAL.name()))
			return;

		final MapAccountDto mapAccountDto = accountMapper.accountToMapAccountDto(project.getAccount());
		final MapProjectDto mapProjectDto = projectMapper.projectToMapProjectDto(project);

		switch (action) {
		case "PMO-APPROVE":
			if (project.getAccount().getMapAccountId() == null) {
				mapService.saveAccountAndProject(mapAccountDto, mapProjectDto, project.getAccount().getAccountId(),
						project.getProjectId());
			} else {
				mapService.saveAccount(mapAccountDto, project.getAccount().getAccountId());
				mapService.saveProject(mapProjectDto, project.getProjectId());
			}
			break;
		case "EDIT-PROJECT":
			if (project.getAccount().getMapAccountId() == null) {
				mapService.saveAccountAndProject(mapAccountDto, mapProjectDto, project.getAccount().getAccountId(),
						project.getProjectId());
			} else {
				mapService.saveProject(mapProjectDto, project.getProjectId());
			}
			break;
		default:
			break;
		}

	}

}
