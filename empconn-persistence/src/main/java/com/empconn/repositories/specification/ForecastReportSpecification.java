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

		root.fetch("employee");
		//root.fetch("allocationDetails");
		root.fetch("employee").fetch("primaryAllocation").fetch("reportingManagerId");

		final List<Predicate> finalPredicate = new ArrayList<Predicate>();
		finalPredicate.add(cb.equal(root.get("isActive"), true));

		final List<Integer> titleIds = convertToIntegers(filter.getTitleIdList());
		final List<Integer> primarySkillIds = convertToIntegers(filter.getPrimarySkillIdList());
		final List<Integer> secondarySkillIds = convertToIntegers(filter.getSecondarySkillIdList());
		final List<Integer> locationIds = convertToIntegers(filter.getOrgLocationIdList());
		final List<Integer> verticalIds = convertToIntegers(filter.getVerticalIdList());
		final String monthYear = filter.getMonthYear();

		// Prep the specification
		final Join<Object, Object> employeeJoin = root.join("employee");
		final Join<Object, Object> employeeSkillsJoin = employeeJoin.join("employeeSkills", JoinType.LEFT);
		final Path<Object> titleId = employeeJoin.get("title").get("titleId");
		final Path<Object> primarySkillId = employeeSkillsJoin.get("secondarySkill").get("primarySkill")
				.get("primarySkillId");
		final Path<Object> secondarySkillId = employeeSkillsJoin.get("secondarySkill").get("secondarySkillId");
		final Path<Object> locationId = employeeJoin.get("location").get("locationId");

		//final Join<Object, Object> allocationDetailsJoin = root.join("allocationDetails", JoinType.LEFT);
		final Predicate benchProjects = cb.equal(root.get("project").get("name"), ApplicationConstants.DELIVERY_BENCH_PROJECT_NAME);

		// Define and add the predicates
		finalPredicate.add(cb.equal(employeeJoin.get("isActive"), true));
		finalPredicate.add(cb.not(root.get("project").get("name").in(Arrays.asList(ApplicationConstants.NON_DELIVERY_BENCH_PROJECT_NAME))));

		final List<Predicate> predicate = new ArrayList<Predicate>();

		final LocalDate current_date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue(), Integer.parseInt(ApplicationConstants.FORECAST_FIRSTDAY_OF_MONTH));
		final Date startDate = Date.from(current_date.atStartOfDay(ZoneId.systemDefault()).toInstant());

		if(Strings.isNullOrEmpty(monthYear)) {
			final LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue(), forecastReportDays);
			final LocalDate plusMonthDate = date.plusMonths(Integer.parseInt(ApplicationConstants.FORECAST_ADD_MONTH));
			final Date endDate = Date.from(plusMonthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

			predicate.add(benchProjects);

			final List<Predicate> insidePredicate = new ArrayList<Predicate>();
			insidePredicate.add(
					cb.greaterThanOrEqualTo(root.get("releaseDate"), startDate));
			insidePredicate
			.add(cb.lessThanOrEqualTo(root.get("releaseDate"), endDate));
			predicate.add(cb.and(insidePredicate.toArray(new Predicate[insidePredicate.size()])));

		}else {
			final String[] str = monthYear.split("_");
			final LocalDate date = LocalDate.of(Integer.parseInt(str[1]), Integer.parseInt(str[0]), forecastReportDays);
			final Date endDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

			predicate.add(benchProjects);

			final List<Predicate> insidePredicate = new ArrayList<Predicate>();
			insidePredicate.add(
					cb.greaterThanOrEqualTo(root.get("releaseDate"), startDate));
			insidePredicate
			.add(cb.lessThanOrEqualTo(root.get("releaseDate"), endDate));
			predicate.add(cb.and(insidePredicate.toArray(new Predicate[insidePredicate.size()])));
		}

		finalPredicate.add(cb.or(predicate.toArray(new Predicate[predicate.size()])));

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
					root.get("project").get("account").get("vertical").get("verticalId")));

		query.distinct(true);
		final Predicate and = cb.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));
		return and;
	}

	private List<Integer> convertToIntegers(final List<String> input) {
		if (CollectionUtils.isEmpty(input))
			return new ArrayList<>();
		return input.stream().map(Integer::parseInt).collect(Collectors.toList());
	}

	private Predicate getInPredicate(CriteriaBuilder cb, List<? extends Number> ids,
			Path<Object> id) {
		final Predicate predicate = cb.in(id).value(ids);
		return predicate;
	}


}
