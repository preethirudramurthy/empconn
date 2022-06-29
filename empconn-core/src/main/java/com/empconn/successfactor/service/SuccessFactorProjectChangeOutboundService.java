package com.empconn.successfactor.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.constants.ApplicationConstants;
import com.empconn.successfactors.dto.ProjectChangeDto;
import com.empconn.successfactors.interfaces.SuccessFactorsOutboundData;

@Service
public class SuccessFactorProjectChangeOutboundService extends SuccessFactorOutboundService {

	@Autowired
	private ProjectChangeFileCreatorService projectChangeFileCreatorService;

	@Override
	public CsvFileCreatorService csvFileCreatorService() {
		return projectChangeFileCreatorService;
	}

	@Override
	public String description() {
		return "Project Change";
	}

	@SuppressWarnings("unchecked")
	public Boolean syncProjectChanges(Set<ProjectChangeDto> projectsDto) {
		final Object object = projectsDto;
		final Set<SuccessFactorsOutboundData> object2 = (Set<SuccessFactorsOutboundData>) object;
		return syncChanges(object2,ApplicationConstants.SF_FTP.PROJECT.name());
	}

}

