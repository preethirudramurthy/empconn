package com.empconn.successfactor.service;

import org.springframework.stereotype.Service;

import com.empconn.constants.ApplicationConstants;
import com.empconn.utilities.DateUtils;

@Service
public class ManagerChangeFileCreatorService extends CsvFileCreatorService {

	@Override
	public String getHeader() {
		return "EmpId,NewManagerId,Date";
	}

	@Override
	public String getFileName() {
		return "Manager_" + DateUtils.currentDatetoString(ApplicationConstants.DATE_FORMAT_YYYYMMDD_HHMMSS) + ".csv";
	}

}
