package com.empconn.successfactor.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.constants.ApplicationConstants;
import com.empconn.successfactors.dto.ManagerChangeDto;
import com.empconn.successfactors.interfaces.SuccessFactorsOutboundData;

@Service
public class SuccessFactorManagerChangeOutboundService extends SuccessFactorOutboundService {

	@Autowired
	private ManagerChangeFileCreatorService managerChangeFileCreatorService;

	@Override
	public CsvFileCreatorService csvFileCreatorService() {
		return managerChangeFileCreatorService;
	}

	@Override
	public String description() {
		return "Manager Change";
	}

	@SuppressWarnings("unchecked")
	public Boolean syncManagerChanges(Set<ManagerChangeDto> managersDto) {
		final Object object = managersDto;
		final Set<SuccessFactorsOutboundData> object2 = (Set<SuccessFactorsOutboundData>) object;
		return syncChanges(object2,ApplicationConstants.SF_FTP.MANAGER.name());
	}

}

