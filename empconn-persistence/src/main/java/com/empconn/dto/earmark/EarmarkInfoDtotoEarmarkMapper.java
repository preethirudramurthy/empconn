package com.empconn.dto.earmark;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.Earmark;

@Mapper(componentModel = "spring")
public abstract class EarmarkInfoDtotoEarmarkMapper {
	
	
//	public abstract List<Earmark> earmarkProjectDtoToEarmarkList(List<EarmarkInfoDto> earmarkList);
//	
////	@Mapping(source = "allocationId", target = "value")
//	@Mapping(source = "clientInterviewNeeded", target = "isClientInterviewNeeded")
//	public abstract Earmark earmarkProjectDtoToEarmark(EarmarkInfoDto earmarkInfoDto);

}
