package com.empconn.successfactor.service;

import org.springframework.stereotype.Service;

import com.empconn.constants.ApplicationConstants;
import com.empconn.utilities.DateUtils;

@Service
public class ProjectChangeFileCreatorService extends CsvFileCreatorService {

	@Override
	public String getHeader() {
		return "[OPERATOR],externalCode,effectiveStartDate,cust_Project,cust_Customer\n"
				+ "\"Supported operators: Delimit, Clear and Delete\",externalCode,Start Date,Project,Customer";
	}

	@Override
	public String getFileName() {
		final String date = DateUtils.currentDatetoString(ApplicationConstants.DATE_FORMAT_SLASH_MM_DD_YYYY);
		return "ProjectInfo" + date.replace("/", "") + ".csv";
	}

}
