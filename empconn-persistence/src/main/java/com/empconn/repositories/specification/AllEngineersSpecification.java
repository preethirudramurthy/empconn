package com.empconn.repositories.specification;

import java.time.LocalDate;
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
import org.springframework.util.CollectionUtils;

import com.empconn.dto.earmark.AvailableResourceReqDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;
import com.empconn.persistence.entities.EmployeeSkill;

public class AllEngineersSpecification implements Specification<Allocation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4690148986784792977L;
	private static final String ALLOCATION_DETAILS = "allocationDetails";
	private static final String BENCH = "Bench";
	private static final String PROJECT = "project";
	private static final String IS_ACTIVE = "isActive";
	private static final String EMPLOYEE = "employee";
	private final AvailableResourceReqDto filter;

	public AllEngineersSpecification(AvailableResourceReqDto filter) {
		super();
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<Allocation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		root.fetch(EMPLOYEE);
		root.fetch(ALLOCATION_DETAILS);

		final List<Predicate> finalPredicate = new ArrayList<>();
		finalPredicate.add(cb.equal(root.get(IS_ACTIVE), true));

		final Predicate deliveryEmployeePredicate = cb.and(cb.equal(root.get(EMPLOYEE).get(IS_ACTIVE), true),
				cb.equal(root.get(EMPLOYEE).get("businessUnit").get("name"), "Delivery"),
				cb.equal(root.get(EMPLOYEE).get("division").get("name"), "Delivery"),
				cb.in(root.get(EMPLOYEE).get("department").get("name")).value(Arrays.asList("PMO", "SEPG")).not());
		finalPredicate.add(deliveryEmployeePredicate);

		final Predicate predicateForBenchAccount = cb.equal(root.get(PROJECT).get("account").get("name"), BENCH);
		final Predicate predicateForNotBenchAccount = cb.notEqual(root.get(PROJECT).get("account").get("name"),
				BENCH);

		final Predicate predicateForNotNDProject = cb.notEqual(root.get(PROJECT).get("name"), "NDBench");
		finalPredicate
		.add(cb.or(predicateForNotBenchAccount, cb.and(predicateForBenchAccount, predicateForNotNDProject)));

		if (filter.getBenchAgeLower() != null && filter.getBenchAgeHigher() != null) {

			finalPredicate.add(predicateForBenchAccount);

			final Date fromStartDate = getDateBeforeToday(filter.getBenchAgeHigher());
			final Date toStartDate = getDateBeforeToday(filter.getBenchAgeLower());
			final Subquery<Date> sq = query.subquery(Date.class);
			final Root<Allocation> sqEmp = sq.correlate(root);
			final Join<Allocation, AllocationDetail> details = sqEmp.join(ALLOCATION_DETAILS);

			final Predicate predicateForActiveDetails = cb.equal(details.get(IS_ACTIVE), true);

			final Predicate predicateForBenchAgeFrom = cb.greaterThanOrEqualTo(
					sq.where(predicateForActiveDetails).select(cb.least(details.<Date>get("startDate"))),
					fromStartDate);

			final Predicate predicateForBenchAgeTo = cb.lessThanOrEqualTo(
					sq.where(predicateForActiveDetails).select(cb.least(details.<Date>get("startDate"))), toStartDate);

			finalPredicate.add(cb.and(predicateForBenchAgeFrom, predicateForBenchAgeTo));

		} else if (filter.getResourceType() != null) {
			if (filter.getResourceType().equalsIgnoreCase(BENCH)) {

				finalPredicate.add(predicateForBenchAccount);

			} else if (filter.getResourceType().equalsIgnoreCase("Allocated")) {

				finalPredicate.add(predicateForNotBenchAccount);
			}
		}
		if (!CollectionUtils.isEmpty(filter.getTitleId())) {
			finalPredicate.add(root.get(EMPLOYEE).get("title").get("titleId")
					.in(filter.getTitleId().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}
		if (filter.getPrimarySkillId() != null || !CollectionUtils.isEmpty(filter.getSecondarySkillIdList())) {
			final Join<Allocation, EmployeeSkill> empSkillJoin = root.join(EMPLOYEE).join("employeeSkills");
			final Predicate primarySkillPredicate = cb.equal(
					empSkillJoin.get("secondarySkill").get("primarySkill").get("primarySkillId"),
					Integer.valueOf(filter.getPrimarySkillId()));
			finalPredicate.add(primarySkillPredicate);
		}
		if (!CollectionUtils.isEmpty(filter.getSecondarySkillIdList())) {
			final Join<Allocation, EmployeeSkill> empSkillJoin = root.join(EMPLOYEE).join("employeeSkills");
			final Predicate secondarySkillPredicate = cb.in(empSkillJoin.get("secondarySkill").get("secondarySkillId"))
					.value(filter.getSecondarySkillIdList().stream().map(Integer::parseInt)
							.collect(Collectors.toList()));
			finalPredicate.add(secondarySkillPredicate);
		}
		if (!CollectionUtils.isEmpty(filter.getOrgLocationIdList())) {
			final Predicate locationPredicate = cb.in(root.get(EMPLOYEE).get("location").get("locationId"))
					.value(filter.getOrgLocationIdList().stream().map(Integer::parseInt).collect(Collectors.toList()));
			finalPredicate.add(locationPredicate);
		}
		if (filter.getAvailableFrom() != null) {
			/*
			 * Bench people will be available at any time, so all bench records should come
			 */
			final Predicate predicateForAvailableAllocated = cb.lessThan(root.get("releaseDate"), Date
					.from(filter.getAvailableFrom().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			final Predicate predicateForAvailableFrom = cb.or(predicateForBenchAccount, predicateForAvailableAllocated);
			finalPredicate.add(predicateForAvailableFrom);
		}

		if (filter.getAvailablePercentage() != null) {
			final Subquery<Integer> sq = query.subquery(Integer.class);
			final Root<Allocation> sqEmp = sq.correlate(root);
			final Join<Allocation, AllocationDetail> details = sqEmp.join(ALLOCATION_DETAILS);
			final Predicate predicateForActiveDetails = cb.equal(details.get(IS_ACTIVE), true);
			final Predicate predicateForAvailablePercent = cb.equal(
					sq.where(predicateForActiveDetails).select(cb.sum(details.get("allocatedPercentage"))),
					filter.getAvailablePercentage());
			finalPredicate.add(cb.and(predicateForAvailablePercent));
		}

		if (!CollectionUtils.isEmpty(filter.getResourceId())) {
			final Predicate resourcePredicate = cb.in(root.get(EMPLOYEE).get("employeeId"))
					.value(filter.getResourceId().stream().map(Long::parseLong).collect(Collectors.toList()));
			finalPredicate.add(resourcePredicate);
		}

		query.distinct(true);
		return cb.and(finalPredicate.toArray(new Predicate[0]));
	}

	public Date getDateBeforeToday(Integer count) {
		final LocalDate date = LocalDate.now().minusDays(count);
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
