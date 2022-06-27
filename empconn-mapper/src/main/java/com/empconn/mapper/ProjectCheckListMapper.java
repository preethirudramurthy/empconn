package com.empconn.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.empconn.dto.CheckItemDto;
import com.empconn.dto.CheckListItemDto;
import com.empconn.persistence.entities.ProjectChecklist;

@Mapper(componentModel = "spring", uses = { ChecklistUnitValueMapper.class })
public abstract class ProjectCheckListMapper {
	@Mapping(source = "projectChecklistId", target = "checkListItemId")
	@Mapping(source = "isSelected", target = "checked")
	@Mapping(source = "checklist", target = "checkListItem")
	public abstract CheckListItemDto projectChecklistToChecklistItemDto(ProjectChecklist source);

	List<CheckListItemDto> projectChecklistsToChecklistItemDtos(Set<ProjectChecklist> set) {
		if (set == null) {
			return null;
		}
		final List<CheckListItemDto> list = new ArrayList<CheckListItemDto>(set.size());
		for (final ProjectChecklist projectChecklist : set) {
			if (projectChecklist.getIsSelected() == true)
				list.add(projectChecklistToChecklistItemDto(projectChecklist));
		}
		return list;
	}

	@Mapping(source = "checkListItemId", target = "projectChecklistId")
	@Mapping(source = "checked", target = "isSelected")
	@Mapping(source = "comment", target = "comment")
	public abstract ProjectChecklist checkItemDtoToProjectChecklist(CheckItemDto source);

}
