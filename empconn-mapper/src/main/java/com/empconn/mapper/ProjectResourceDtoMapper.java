package com.empconn.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.ResourceItemDto;
import com.empconn.persistence.entities.PrimarySkill;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.persistence.entities.ProjectResource;
import com.empconn.persistence.entities.ProjectResourcesSecondarySkill;
import com.empconn.persistence.entities.SecondarySkill;
import com.empconn.persistence.entities.Title;
import com.empconn.repositories.PrimarySkillRepository;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.repositories.ProjectResourceRepository;
import com.empconn.repositories.SecondarySkillRepository;
import com.empconn.repositories.TitleRepository;
import com.empconn.security.SecurityUtil;

@Mapper(componentModel = "spring")
public abstract class ProjectResourceDtoMapper {

	@Autowired
	ProjectLocationRespository projectLocationRespository;

	@Autowired
	TitleRepository titleRepository;

	@Autowired
	PrimarySkillRepository primarySkillRepository;

	@Autowired
	SecondarySkillRepository secondarySkillRepository;

	@Autowired
	SecurityUtil jwtEmployeeUtil;

	@Autowired
	ProjectResourceRepository projectResourceRepository;

	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "rrId", target = "projectResourcesId")
	@Mapping(source = "noOfResources", target = "numberOfResources")
	@Mapping(source = "percentage", target = "allocationPercentage")
	@Mapping(source = "billable", target = "isBillable")
	@Mapping(source = "titleId", target = "title", qualifiedByName = "titleIdToTitle")
	@Mapping(source = "projectLocationId", target = "projectLocation", qualifiedByName = "projectLocationIdToProjectLocation")
	@Mapping(source = "primarySkillId", target = "primarySkill", qualifiedByName = "primarySkillIdToPrimarySkill")
	@Mapping(target = "projectResourcesSecondarySkills", expression = "java(secondarySkillIdsToProjectResourcesSecondarySkills(resourceItemDto.getSecondarySkillIdList(), projectResource))")
	@Mapping(target = "isActive", constant = "true")
	public abstract ProjectResource resourceItemDtoToProjectResource(ResourceItemDto resourceItemDto);

	@Mapping(target = "modifiedBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "modifiedOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(source = "noOfResources", target = "numberOfResources")
	@Mapping(source = "percentage", target = "allocationPercentage")
	@Mapping(source = "billable", target = "isBillable")
	@Mapping(source = "titleId", target = "title", qualifiedByName = "titleIdToTitle")
	@Mapping(source = "projectLocationId", target = "projectLocation", qualifiedByName = "projectLocationIdToProjectLocation")
	@Mapping(source = "primarySkillId", target = "primarySkill", qualifiedByName = "primarySkillIdToPrimarySkill")
	@Mapping(target = "projectResourcesSecondarySkills", expression = "java(secondarySkillIdsToProjectResourcesSecondarySkills(resourceItemDto.getSecondarySkillIdList(), projectResource))")
	public abstract ProjectResource resourceItemDtoToProjectResource(ResourceItemDto resourceItemDto,
			@MappingTarget ProjectResource projectResource);

	public Set<ProjectResource> resourceItemListToProjectResources(List<ResourceItemDto> resourceItemList) {
		final Set<ProjectResource> set = new HashSet<>();
		for (final ResourceItemDto dto : resourceItemList) {
			Optional<ProjectResource> prOpt = projectResourceRepository.findById(Long.valueOf(dto.getRrId()));
			if (dto.getRrId() != null && prOpt.isPresent()) {
				final ProjectResource projectResource = prOpt.get();
				set.add(resourceItemDtoToProjectResource(dto, projectResource));
			} else
				set.add(resourceItemDtoToProjectResource(dto));
		}
		return set;
	}

	@Mapping(target = "createdBy", expression = "java(jwtEmployeeUtil.getLoggedInEmployee().getEmployeeId())")
	@Mapping(target = "createdOn", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
	@Mapping(target = "modifiedOn", ignore = true)
	@Mapping(target = "modifiedBy", ignore = true)
	@Mapping(target = "isActive", constant = "true")
	@Mapping(source = "projectResource", target = "projectResource")
	@Mapping(source = "source", target = "secondarySkill")
	public abstract ProjectResourcesSecondarySkill secondarySkillToProjectResourceSecondarySkill(SecondarySkill source,
			ProjectResource projectResource);

	@Named("projectLocationIdToProjectLocation")
	public ProjectLocation projectLocationIdToProjectLocation(String locationId) {
		if (locationId != null) {
			final Optional<ProjectLocation> projectLocation = projectLocationRespository
					.findById(Long.parseLong(locationId));
			if (projectLocation.isPresent()) return projectLocation.get();
		}
		return null;

	}

	@Named("titleIdToTitle")
	public Title titleIdToTitle(String titleId) {
		if (titleId != null) {
			final Optional<Title> title = titleRepository.findById(Integer.parseInt(titleId));
			if (title.isPresent() )return title.get();
		}
		return null;

	}

	@Named("primarySkillIdToPrimarySkill")
	public PrimarySkill primarySkillIdToPrimarySkill(String primarySkillId) {
		if (primarySkillId != null) {
			final Optional<PrimarySkill> primarySkill = primarySkillRepository
					.findById(Integer.parseInt(primarySkillId));
			if (primarySkill.isPresent()) return primarySkill.get();
		}
		return null;

	}

	public Set<ProjectResourcesSecondarySkill> secondarySkillIdsToProjectResourcesSecondarySkills(
			List<String> secondarySkillIdList, ProjectResource projectResource) {
		if (CollectionUtils.isEmpty(secondarySkillIdList))
			return Collections.emptySet();
		final Set<ProjectResourcesSecondarySkill> set = new HashSet<>();
		final List<Integer> secondarySkillIds = secondarySkillIdList.stream().map(Integer::parseInt)
				.collect(Collectors.toList());
		final Set<SecondarySkill> secondarySkills = secondarySkillRepository
				.findBySecondarySkillIdInAndIsActiveTrue(secondarySkillIds);
		if (CollectionUtils.isEmpty(projectResource.getProjectResourcesSecondarySkills())) {
			for (final SecondarySkill secondarySkill : secondarySkills) {
				set.add(secondarySkillToProjectResourceSecondarySkill(secondarySkill, projectResource));
			}
			return set;
		} else {
			for (final SecondarySkill secondarySkill : secondarySkills) {
				final Optional<ProjectResourcesSecondarySkill> skillExist = projectResource
						.getProjectResourcesSecondarySkills().stream().filter(s -> s.getIsActive() && s
								.getSecondarySkill().getSecondarySkillId().equals(secondarySkill.getSecondarySkillId()))
						.findFirst();
				if (skillExist.isPresent())
					set.add(skillExist.get());
				else
					set.add(secondarySkillToProjectResourceSecondarySkill(secondarySkill, projectResource));
			}
			return set;
		}
	}
}
