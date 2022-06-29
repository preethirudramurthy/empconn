package com.empconn.response;

import java.util.List;

import com.empconn.dto.AccountSummaryDto;
import com.empconn.dto.ManagerDto;
import com.empconn.dto.MyPinDto;
import com.empconn.dto.ProjectSummaryDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
