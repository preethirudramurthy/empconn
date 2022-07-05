package com.empconn.repositories.specification;

import java.time.LocalDate;
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

import com.google.common.base.Strings;
import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.ForecastReportRequestDto;
import com.empconn.persistence.entities.Allocation;

public class ForecastReportSpecification implements Specification<Allocation>{

	private static final String RELEASE_DATE = "releaseDate";

	private static final String EMPLOYEE = "employee";

	private static final String PROJECT = "project";

	private static final long serialVersionUID = 1L;

	private final ForecastReportRequestDto filter;
	private final int forecastReportDays;

	public ForecastReportSpecification(ForecastReportRequestDto filter,int forecastReportDays) {
		super();
		this.filter = filter;
		this.forecastReportDays = forecastReportDays;
	}

	@Override
	public Predicate toPredicate(Root<Allocation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		root.fetch(EMPLOYEE);
		root.fetch(EMPLOYEE).fetch("primaryAllocation").fetch("reportingManagerId");

		final List<Predicate> finalPredicate = new ArrayList<>();
		finalPredicate.add(cb.equal(root.get("isActive"), true));

		final List<Integer> titleIds = convertToIntegers(filter.getTitleIdList());
		final List<Integer> primarySkillIds = convertToIntegers(filter.getPrimarySkillIdList());
		final List<Integer> secondarySkillIds = convertToIntegers(filter.getSecondarySkillIdList());
		final List<Integer> locationIds = convertToIntegers(filter.getOrgLocationIdList());
		final List<Integer> verticalIds = convertToIntegers(filter.getVerticalIdList());
		final String monthYear = filter.getMonthYear();

		// Prep the specification
		final Join<Object, Object> employeeJoin = root.join(EMPLOYEE);
		final Join<Object, Object> employeeSkillsJoin = employeeJoin.join("employeeSkills", JoinType.LEFT);
		final Path<Object> titleId = employeeJoin.get("title").get("titleId");
		final Path<Object> primarySkillId = employeeSkillsJoin.get("secondarySkill").get("primarySkill")
				.get("primarySkillId");
		final Path<Object> secondarySkillId = employeeSkillsJoin.get("secondarySkill").get("secondarySkillId");
		final Path<Object> locationId = employeeJoin.get("location").get("locationId");

		final Predicate benchProjects = cb.equal(root.get(PROJECT).get("name"), ApplicationConstants.DELIVERY_BENCH_PROJECT_NAME);

		// Define and add the predicates
		finalPredicate.add(cb.equal(employeeJoin.get("isActive"), true));
		finalPredicate.add(cb.not(root.get(PROJECT).get("name").in(Collections.singletonList(ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME))));

		final List<Predicate> predicate = new ArrayList<>();

		final LocalDate currentDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue(), Integer.parseInt(ApplicationConstants.FORECAST_FIRSTDAY_OF_MONTH));
		final Date startDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		if(Strings.isNullOrEmpty(monthYear)) {
			final LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue(), forecastReportDays);
			final LocalDate plusMonthDate = date.plusMonths(Integer.parseInt(ApplicationConstants.FORECAST_ADD_MONTH));
			final Date endDate = Date.from(plusMonthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

			predicate.add(benchProjects);

			final List<Predicate> insidePredicate = new ArrayList<>();
			insidePredicate.add(
					cb.greaterThanOrEqualTo(root.get(RELEASE_DATE), startDate));
			insidePredicate
			.add(cb.lessThanOrEqualTo(root.get(RELEASE_DATE), endDate));
			predicate.add(cb.and(insidePredicate.toArray(new Predicate[0])));

		}else {
			final String[] str = monthYear.split("_");
			final LocalDate date = LocalDate.of(Integer.parseInt(str[1]), Integer.parseInt(str[0]), forecastReportDays);
			final Date endDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

			predicate.add(benchProjects);

			final List<Predicate> insidePredicate = new ArrayList<>();
			insidePredicate.add(
					cb.greaterThanOrEqualTo(root.get(RELEASE_DATE), startDate));
			insidePredicate
			.add(cb.lessThanOrEqualTo(root.get(RELEASE_DATE), endDate));
			predicate.add(cb.and(insidePredicate.toArray(new Predicate[0])));
		}

		finalPredicate.add(cb.or(predicate.toArray(new Predicate[0])));

		if (!CollectionUtils.isEmpty(titleIds))
			finalPredicate.add(getInPredicate(cb, titleIds, titleId));

		if (!CollectionUtils.isEmpty(primarySkillIds))
			finalPredicate.add(getInPredicate(cb, primarySkillIds, primarySkillId));

		if (!CollectionUtils.isEmpty(secondarySkillIds))
			finalPredicate.add(getInPredicate(cb, secondarySkillIds, secondarySkillId));

		if (!CollectionUtils.isEmpty(locationIds))
			finalPredicate.add(getInPredicate(cb, locationIds, locationId));

		if (!CollectionUtils.isEmpty(verticalIds))
			finalPredicate.add(getInPredicate(cb, verticalIds,
					root.get(PROJECT).get("account").get("vertical").get("verticalId")));

		query.distinct(true);
		return cb.and(finalPredicate.toArray(new Predicate[0]));
	}

	private List<Integer> convertToIntegers(final List<String> input) {
		if (CollectionUtils.isEmpty(input))
			return new ArrayList<>();
		return input.stream().map(Integer::parseInt).collect(Collectors.toList());
	}

	private Predicate getInPredicate(CriteriaBuilder cb, List<? extends Number> ids,
			Path<Object> id) {
		return cb.in(id).value(ids);
	}


}
