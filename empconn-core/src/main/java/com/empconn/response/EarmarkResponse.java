package com.empconn.response;

import java.util.List;

import com.empconn.dto.earmark.AvailableResourceDto;
import com.empconn.dto.earmark.EarmarkItemDto;
import com.empconn.dto.earmark.ManagerInfoDto;
import com.empconn.vo.UnitValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EarmarkResponse {

	public static ObjectMapper mapper = new ObjectMapper();

	public static TypeReference<List<AvailableResourceDto>> availableResourceType = new TypeReference<List<AvailableResourceDto>>() {
	};
	public static TypeReference<List<EarmarkItemDto>> earmarkListAsType = new TypeReference<List<EarmarkItemDto>>() {
	};
	public static TypeReference<List<ManagerInfoDto>> dropdownManagerType = new TypeReference<List<ManagerInfoDto>>() {
	};
	public static TypeReference<List<UnitValue>> unitValueType = new TypeReference<List<UnitValue>>() {
	};

	
}
