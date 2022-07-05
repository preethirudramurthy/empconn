package com.empconn.repositories.specification;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.ResourceViewRequestDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Project;

public class ResourceViewSpecification implements Specification<Allocation>{

	private static final String PROJECT_ID = "projectId";
	private static final String PROJECT = "project";
	private static final String EMPLOYEE = "employee";
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final ResourceViewRequestDto filter;

	public ResourceViewSpecification(ResourceViewRequestDto filter) {
		super();
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<Allocation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		root.fetch("allocationDetails");
		root.fetch("reportingManagerId");
		root.fetch(PROJECT);
		root.fetch(EMPLOYEE);
		root.fetch(EMPLOYEE).fetch("primaryAllocation").fetch("reportingManagerId");

		final List<Predicate> finalPredicate = new ArrayList<>();
		finalPredicate.add(cb.equal(root.get("isActive"), true));
		finalPredicate.add(cb.equal(root.get(EMPLOYEE).get("isActive"), true));
		finalPredicate.add(cb.not(root.get(PROJECT).get("name").in(Collections.singletonList("NDBench"))));

		final List<Integer> primarySkillIds = convertToIntegers(filter.getPrimarySkillList());
		final List<Integer> secondarySkillIds = convertToIntegers(filter.getSecondarySkillIdList());

		final Join<Object, Object> employeeJoin = root.join(EMPLOYEE);
		final Join<Object, Object> employeeSkillsJoin = employeeJoin.join("employeeSkills", JoinType.LEFT);
		final Path<Object> primarySkillId = employeeSkillsJoin.get("secondarySkill").get("primarySkill")
				.get("primarySkillId");
		final Path<Object> secondarySkillId = employeeSkillsJoin.get("secondarySkill").get("secondarySkillId");

		finalPredicate.add((root.get(PROJECT).get("currentStatus").in(Arrays.asList("PMO_APPROVED", "PROJECT_ON_HOLD"))));
		if (filter.getProhectIdsForManager() != null && !filter.getProhectIdsForManager().isEmpty()) {

			finalPredicate.add(root.get(PROJECT).get(PROJECT_ID)
					.in(filter.getProhectIdsForManager().stream().map(Long::parseLong).collect(Collectors.toList())));
		}

		if (filter.getVerticalIdList() != null && !filter.getVerticalIdList().isEmpty()) {
			finalPredicate.add(root.get(PROJECT).get("account").get("vertical").get("verticalId")
					.in(filter.getVerticalIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (!CollectionUtils.isEmpty(primarySkillIds))
			finalPredicate.add(getInPredicate(cb, primarySkillIds, primarySkillId));

		if (!CollectionUtils.isEmpty(secondarySkillIds))
			finalPredicate.add(getInPredicate(cb, secondarySkillIds, secondarySkillId));

		if (filter.getTitleIdList() != null && !filter.getTitleIdList().isEmpty()) {
			finalPredicate.add(root.get(EMPLOYEE).get("title").get("titleId")
					.in(filter.getTitleIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getProjectIdList() != null && !filter.getProjectIdList().isEmpty()) {
			finalPredicate.add(root.get(PROJECT).get(PROJECT_ID)
					.in(filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList())));
		}

		if (filter.getAccountIdList() != null && !filter.getAccountIdList().isEmpty()) {
			finalPredicate.add(root.get(PROJECT).get("account").get("accountId")
					.in(filter.getAccountIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getOrgLocationIdList() != null && !filter.getOrgLocationIdList().isEmpty()) {
			final Join<?,?> projLocationJoin = root.join("projectLocation");
			final Join<?,?> locationJoin = projLocationJoin.join("location");
			finalPredicate.add(locationJoin.get("locationId")
					.in(filter.getOrgLocationIdList().stream().map(Long::parseLong).collect(Collectors.toList())));
		}

		if (filter.getWorkgroup() != null && !filter.getWorkgroup().isEmpty()) {
			finalPredicate.add(root.get("workGroup").get("name").in(new ArrayList<>(filter.getWorkgroup())));
		}


		if (filter.getReleaseDateBefore() != null) {
			finalPredicate.add(cb.lessThan(root.get("releaseDate"),
					Date.from(filter.getReleaseDateBefore().atStartOfDay(ZoneId.systemDefault()).toInstant())));
		}
		if (filter.getManagerId() != null && !filter.getManagerId().isEmpty()) {
			finalPredicate.add(root.get("allocationManagerId").get("employeeId").in(filter.getManagerId().stream().map(Long::parseLong).collect(Collectors.toList())));
		}

		if (filter.getProjectsByGdms() != null && !filter.getProjectsByGdms().isEmpty()) {

			finalPredicate.add(root.get(PROJECT).get(PROJECT_ID)
					.in(filter.getProjectsByGdms().stream().map(Project::getProjectId).collect(Collectors.toList())));
		}

		query.distinct(true);
		return cb.and(finalPredicate.toArray(new Predicate[0]));

	}

	private List<Integer> convertToIntegers(final List<String> input) {
		if (CollectionUtils.isEmpty(input))
			return new ArrayList<>();
		return input.stream().map(Integer::parseInt).collect(Collectors.toList());
	}


	private Predicate getInPredicate(CriteriaBuilder cb, List<? extends Number> primarySkillIds,
			Path<Object> primarySkillId) {
		return cb.in(primarySkillId).value(primarySkillIds);
	}

}
