package com.empconn.repositories.specification;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.allocation.SwitchoverSearchDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Earmark;

public class AllocationSpecification implements Specification<Allocation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8166704086629374518L;
	private static final String IS_ACTIVE = "isActive";
	private static final String PROJECT = "project";
	private static final String EMPLOYEE = "employee";
	private static final String ND_BENCH = "NDBench";
	private static final String ALLOCATION_ID = "allocationId";
	private final SwitchoverSearchDto filter;

	public AllocationSpecification(SwitchoverSearchDto filter) {
		super();
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<Allocation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		root.fetch(EMPLOYEE);
		root.fetch("allocationDetails");
		root.fetch("allocationStatus");
		root.fetch(PROJECT);
		final List<Predicate> finalPredicate = new ArrayList<>();
		finalPredicate.add(cb.equal(root.get(IS_ACTIVE), true));
		finalPredicate.add(cb.equal(root.get(EMPLOYEE).get(IS_ACTIVE), true));
		finalPredicate.add(cb.not(root.get(PROJECT).get("name").in(Arrays.asList(ND_BENCH))));

		// To exclude non delivery employees' allocations
		final List<Predicate> ndPredicates = new ArrayList<>();

		ndPredicates
		.add(cb.equal(root.get(EMPLOYEE).get("businessUnit").get("name"), ApplicationConstants.BU_DELIVERY));
		ndPredicates.add(
				cb.equal(root.get(EMPLOYEE).get("division").get("name"), ApplicationConstants.DIVISION_DELIVERY));
		ndPredicates.add(cb.not(root.get(EMPLOYEE).get("department").get("name")
				.in(Arrays.asList(ApplicationConstants.DEPT_PMO, ApplicationConstants.DEPT_SEPG))));
		finalPredicate.add(cb.and(ndPredicates.toArray(new Predicate[ndPredicates.size()])));

		// To exclude Bench+Earmark records from switchover list
		final Subquery<Earmark> subquery = query.subquery(Earmark.class);
		final Root<Earmark> subqueryRoot = subquery.from(Earmark.class);

		subquery.select(subqueryRoot.get("allocation").get(ALLOCATION_ID));

		final List<Predicate> subPredicate = new ArrayList<>();
		subPredicate.add(cb.equal(subqueryRoot.get("allocation").get(ALLOCATION_ID), root.get(ALLOCATION_ID)));
		subPredicate.add(cb.equal(subqueryRoot.get(IS_ACTIVE), true));
		subquery.where(cb.and(subPredicate.toArray(new Predicate[subPredicate.size()])));

		finalPredicate.add(cb.exists(subquery).not());

		if (filter.getResourceIdList() != null && !filter.getResourceIdList().isEmpty()) {
			finalPredicate.add(root.get(EMPLOYEE).get("employeeId")
					.in(filter.getResourceIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getTitleIdList() != null && !filter.getTitleIdList().isEmpty()) {
			finalPredicate.add(root.get(EMPLOYEE).get("title").get("titleId")
					.in(filter.getTitleIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getResourceType() != null && !filter.getResourceType().isEmpty()) {
			if (filter.getResourceType().equalsIgnoreCase("bench")) {
				finalPredicate.add(root.get(PROJECT).get("name").in(Arrays.asList("Central Bench", ND_BENCH)));
			} else if (filter.getResourceType().equalsIgnoreCase("allocated")) {
				finalPredicate
				.add(cb.not(root.get(PROJECT).get("name").in(Arrays.asList("Central Bench", ND_BENCH))));
			}
		}

		if (filter.getProjectIdList() != null && !filter.getProjectIdList().isEmpty()) {
			finalPredicate.add(root.get(PROJECT).get("projectId")
					.in(filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList())));
		}

		if (filter.getAccountIdList() != null && !filter.getAccountIdList().isEmpty()) {
			finalPredicate.add(root.get(PROJECT).get("account").get("accountId")
					.in(filter.getAccountIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getOrgLocationIdList() != null && !filter.getOrgLocationIdList().isEmpty()) {
			final Predicate locationPredicate = cb.in(root.get(EMPLOYEE).get("location").get("locationId"))
					.value(filter.getOrgLocationIdList().stream().map(Integer::parseInt).collect(Collectors.toList()));
			finalPredicate.add(locationPredicate);
		}

		if (filter.getSecondarySkillIdList() != null && !filter.getSecondarySkillIdList().isEmpty()) {

			final Join<?,?> employeeJoin = root.join(EMPLOYEE);
			final Join<?,?> skillJoin = employeeJoin.join("employeeSkills");

			finalPredicate.add(skillJoin.get("secondarySkill").get("secondarySkillId")
					.in(filter.getSecondarySkillIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getReleaseDateBefore() != null) {
			finalPredicate.add(cb.lessThan(root.get("releaseDate"),
					Date.from(filter.getReleaseDateBefore().atStartOfDay(ZoneId.systemDefault()).toInstant())));
		}

		query.distinct(true);
		return cb.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));
	}

}
