package com.empconn.successfactor.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empconn.constants.ApplicationConstants;
import com.empconn.successfactors.dto.GdmChangeDto;
import com.empconn.successfactors.interfaces.SuccessFactorsOutboundData;

@Service
public class SuccessFactorGdmChangeOutboundService extends SuccessFactorOutboundService {

	@Autowired
	private GdmChangeFileCreatorService gdmChangeFileCreatorService;

	@Override
	public CsvFileCreatorService csvFileCreatorService() {
		return gdmChangeFileCreatorService;
	}

	@Override
	public String description() {
		return "Gdm Change";
	}

	@SuppressWarnings("unchecked")
	public Boolean syncGdmChanges(Set<GdmChangeDto> gdmsDto) {
		final Object object = gdmsDto;
		final Set<SuccessFactorsOutboundData> object2 = (Set<SuccessFactorsOutboundData>) object;
		return syncChanges(object2,ApplicationConstants.SF_FTP.GDM.name());
	}

}

