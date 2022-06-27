package com.empconn.response;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.empconn.dto.AccountDetailsDto;
import com.empconn.dto.AccountSummaryDto;
import com.empconn.dto.IsValidDto;
import com.empconn.dto.ManagerDto;
import com.empconn.dto.MyPinDto;
import com.empconn.dto.PinCountDto;
import com.empconn.dto.PinDetailsDto;
import com.empconn.dto.PinStatusChangedDto;
import com.empconn.dto.ProjectDetailsDto;
import com.empconn.dto.ProjectEndDateChangedDto;
import com.empconn.dto.ProjectSummaryDto;
import com.empconn.dto.SavedAccountDto;
import com.empconn.dto.SavedPinDto;
import com.empconn.dto.ValidatedPinDto;

public class PinResponse {

	public static ObjectMapper mapper = new ObjectMapper();

	
	public static TypeReference<List<MyPinDto>> myPinType = new TypeReference<List<MyPinDto>>() {
	};
	public static TypeReference<List<AccountSummaryDto>> accountSummaryType = new TypeReference<List<AccountSummaryDto>>() {
	};
	public static TypeReference<List<ProjectSummaryDto>> projectSummaryType = new TypeReference<List<ProjectSummaryDto>>() {
	};
	public static TypeReference<List<ManagerDto>> managerType = new TypeReference<List<ManagerDto>>() {
	};

	
}
