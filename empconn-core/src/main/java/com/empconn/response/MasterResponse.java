package com.empconn.response;

import java.util.List;

import com.empconn.dto.MasterResourceDto;
import com.empconn.vo.UnitValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MasterResponse {

	public static ObjectMapper mapper = new ObjectMapper();

	public static TypeReference<List<UnitValue>> unitType = new TypeReference<List<UnitValue>>() {
	};
	public static TypeReference<List<MasterResourceDto>> resourceType = new TypeReference<List<MasterResourceDto>>() {
	};

}
