package com.empconn.repositories.specification;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.AvailablePercentageDto;
import com.empconn.dto.BenchAgeDto;
import com.empconn.dto.BenchReportRequestDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.AllocationDetail;

public class BenchReportSpecification implements Specification<Allocation> {

	private static final long serialVersionUID = -3763123808668118215L;

	private final BenchReportRequestDto filter;

	public BenchReportSpecification(BenchReportRequestDto filter) {
		super();
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<Allocation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		root.fetch("employee");
		//		root.fetch("employee").fetch("employeeAllocations");
		//root.fetch("project");
		//root.fetch("allocationDetails");
		final List<Predicate> finalPredicate = new ArrayList<Predicate>();
		finalPredicate.add(cb.equal(root.get("isActive"), true));

		// Retrieve the filter values
		final List<Integer> primarySkillIds = convertToIntegers(filter.getPrimarySkillIdList());
		final List<Integer> secondarySkillIds = convertToIntegers(filter.getSecondarySkillIdList());
		final List<Integer> titleIds = convertToIntegers(filter.getTitleIdList());
		final List<Integer> locationIds = convertToIntegers(filter.getOrgLocationIdList());
		final List<Long> benchProjectIds = convertToLong(filter.getBenchProjectIdList());

		// Prep the specification
		final Join<Object, Object> employeeJoin = root.join("employee");

		final Path<Object> titleId = employeeJoin.get("title").get("titleId");
		final Path<Object> locationId = employeeJoin.get("location").get("locationId");
		//final Join<Allocation, AllocationDetail> allocationDetailsJoin = root.join("allocationDetails", JoinType.LEFT);
		final Predicate benchProjects = cb.equal(root.get("project").get("account").get("name"), "Bench");
		final Path<Object> projectId = root.get("project").get("projectId");

		// Fetch only active allocation details
		//finalPredicate.add(cb.equal(allocationDetailsJoin.get("isActive"), true));
		finalPredicate.add(cb.equal(employeeJoin.get("isActive"), true));
		finalPredicate.add(cb.not(root.get("project").get("name").in(Arrays.asList("NDBench"))));

		if (!CollectionUtils.isEmpty(filter.getAvailablePercentage())) {
			final Subquery<Integer> sq = query.subquery(Integer.class);
			final Root<Allocation> sqEmp = sq.correlate(root);
			final Join<Allocation, AllocationDetail> details = sqEmp.join("allocationDetails");
			final Predicate predicateForActiveDetails = cb.equal(details.get("isActive"), true);


			final List<Predicate> predicate = new ArrayList<Predicate>();
			for (final AvailablePercentageDto a : filter.getAvailablePercentage()) {
				if (a.getLow().equals(a.getHigh())) {
					predicate.add(cb.equal(sq.where(predicateForActiveDetails).select(cb.sum(details.get("allocatedPercentage"))), Integer.parseInt(a.getHigh())));


				} else {
					final List<Predicate> insidePredicate = new ArrayList<Predicate>();
					insidePredicate.add(
							cb.greaterThanOrEqualTo(sq.where(predicateForActiveDetails).select(cb.sum(details.get("allocatedPercentage"))), Integer.parseInt(a.getLow())));
					insidePredicate
					.add(cb.lessThanOrEqualTo(sq.where(predicateForActiveDetails).select(cb.sum(details.get("allocatedPercentage"))), Integer.parseInt(a.getHigh())));
					predicate.add(cb.and(insidePredicate.toArray(new Predicate[insidePredicate.size()])));
				}
			}
			finalPredicate.add(cb.or(predicate.toArray(new Predicate[predicate.size()])));

		}

		if (!CollectionUtils.isEmpty(filter.getBenchAge())) {

			final List<Predicate> predicate = new ArrayList<Predicate>();
			final Subquery<Date> sq = query.subquery(Date.class);
			final Root<Allocation> sqEmp = sq.correlate(root);
			final Join<Allocation, AllocationDetail> details = sqEmp.join("allocationDetails");
			final Predicate predicateForActiveDetails = cb.equal(details.get("isActive"), true);

			for (final BenchAgeDto b : filter.getBenchAge()) {

				final Date fromStartDate = getDateBeforeToday(b.getTo());
				final Date toStartDate = getDateBeforeToday(b.getFrom());


				final Predicate predicateForBenchAgeFrom = cb.greaterThanOrEqualTo(
						sq.where(predicateForActiveDetails).select(cb.greatest(details.<Date>get("startDate"))),
						fromStartDate);

				final Predicate predicateForBenchAgeTo = cb.lessThanOrEqualTo(
						sq.where(predicateForActiveDetails).select(cb.greatest(details.<Date>get("startDate"))), toStartDate);

				predicate.add(cb.and(predicateForBenchAgeFrom, predicateForBenchAgeTo));
			}
			finalPredicate.add(cb.or(predicate.toArray(new Predicate[predicate.size()])));
		}

		if (!CollectionUtils.isEmpty(primarySkillIds)) {
			final Join<Object, Object> employeeSkillsJoin = employeeJoin.join("employeeSkills", JoinType.LEFT);
			final Path<Object> primarySkillId = employeeSkillsJoin.get("secondarySkill").get("primarySkill")
					.get("primarySkillId");
			finalPredicate.add(getInPredicate(cb, primarySkillIds, primarySkillId));
		}


		if (!CollectionUtils.isEmpty(secondarySkillIds)) {
			final Join<Object, Object> employeeSkillsJoin = employeeJoin.join("employeeSkills", JoinType.LEFT);
			final Path<Object> secondarySkillId = employeeSkillsJoin.get("secondarySkill").get("secondarySkillId");
			finalPredicate.add(getInPredicate(cb, secondarySkillIds, secondarySkillId));
		}


		if (!CollectionUtils.isEmpty(titleIds))
			finalPredicate.add(getInPredicate(cb, titleIds, titleId));

		if (!CollectionUtils.isEmpty(locationIds))
			finalPredicate.add(getInPredicate(cb, locationIds, locationId));

		if (!CollectionUtils.isEmpty(benchProjectIds))
			finalPredicate.add(getInPredicate(cb, benchProjectIds, projectId));
		else
			finalPredicate.add(benchProjects);

		query.distinct(true);
		final Predicate and = cb.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));
		return and;

	}

	private Predicate getInPredicate(CriteriaBuilder cb, List<? extends Number> primarySkillIds,
			Path<Object> primarySkillId) {
		final Predicate primarySkillsPredicate = cb.in(primarySkillId).value(primarySkillIds);
		return primarySkillsPredicate;
	}

	private List<Long> convertToLong(List<String> input) {
		if (CollectionUtils.isEmpty(input))
			return new ArrayList<>();
		return input.stream().map(Long::parseLong).collect(Collectors.toList());
	}

	private List<Integer> convertToIntegers(final String primarySkillId) {
		return convertToIntegers(convertToStringList(primarySkillId));
	}

	private List<Integer> convertToIntegers(final List<String> input) {
		if (CollectionUtils.isEmpty(input))
			return new ArrayList<>();
		return input.stream().map(Integer::parseInt).collect(Collectors.toList());
	}

	private List<String> convertToStringList(String commaSeparatedString) {
		if (StringUtils.isEmpty(StringUtils.trim(commaSeparatedString)))
			return new ArrayList<>();

		return Stream.of(commaSeparatedString.split(",")).map(String::trim).collect(Collectors.toList());

	}

	public Date getDateBeforeToday(String count) {
		final LocalDate date = LocalDate.now().minusDays(Integer.valueOf(count));
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
