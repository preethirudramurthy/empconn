package com.empconn.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.persistence.entities.Checklist;
import com.empconn.vo.UnitValue;

@Mapper(componentModel = "spring")
public interface ChecklistUnitValueMapper {

	@Mapping(source = "checklistId", target = "id")
	@Mapping(source = "name", target = "value")
	UnitValue checklistToUnitValue(Checklist source);

	List<UnitValue> checklistsToUnitValues(List<Checklist> checklists);

	@Mapping(source = "id", target = "checklistId")
	@Mapping(source = "value", target = "name")
	Checklist unitValueToChecklist(UnitValue destination);

}
