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

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.AllocationReportRequestDto;
import com.empconn.persistence.entities.Allocation;

public class AllocationReportSpecification implements Specification<Allocation> {

	private static final long serialVersionUID = -3763123808668118215L;

	private final AllocationReportRequestDto filter;

	public AllocationReportSpecification(AllocationReportRequestDto filter) {
		super();
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<Allocation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		root.fetch("employee");
		//root.fetch("employee").fetch("title");
		//root.fetch("employee").fetch("location");
		//root.fetch("workGroup");
		//root.fetch("employee").fetch("employeeSkills", JoinType.LEFT);
		root.fetch("allocationDetails");
		root.fetch("employee").fetch("primaryAllocation").fetch("reportingManagerId");

		final List<Predicate> finalPredicate = new ArrayList<Predicate>();
		finalPredicate.add(cb.equal(root.get("isActive"), true));

		// Retrieve the filter values
		final List<Integer> verticalIds = convertToIntegers(filter.getVerticalIdList());
		final List<Integer> accountIds = convertToIntegers(filter.getAccountIdList());
		final List<Long> projectIds = convertToLong(filter.getProjectIdList());
		final List<Integer> titleIds = convertToIntegers(filter.getTitleIdList());
		final List<String> workGroupIds = filter.getWorkgroupList();
		final List<Integer> locationIds = convertToIntegers(filter.getOrgLocationIdList());
		final Date allocationFrom = localDateToDate(filter.getAllocationFrom());
		final List<Integer> primarySkillIds = convertToIntegers(filter.getPrimarySkillIdList());
		final List<Integer> secondarySkillIds = convertToIntegers(filter.getSecondarySkillIdList());
		final Boolean onlyActive = filter.isOnlyActive();

		// Prep the specification
		final Join<Object, Object> employeeJoin = root.join("employee");
		final Join<Object, Object> employeeSkillsJoin = employeeJoin.join("employeeSkills", JoinType.LEFT);
		final Path<Object> primarySkillId = employeeSkillsJoin.get("secondarySkill").get("primarySkill")
				.get("primarySkillId");
		final Path<Object> secondarySkillId = employeeSkillsJoin.get("secondarySkill").get("secondarySkillId");
		final Path<Object> titleId = employeeJoin.get("title").get("titleId");
		final Path<Object> locationId = employeeJoin.get("location").get("locationId");
		final Join<Object, Object> allocationDetailsJoin = root.join("allocationDetails", JoinType.LEFT);
		final Predicate benchProjects = cb.equal(root.get("project").get("account").get("name"), "Bench");
		final Path<Object> projectId = root.get("project").get("projectId");

		// Define and add the predicates
		finalPredicate.add(cb.equal(employeeJoin.get("isActive"), true));
		finalPredicate.add(benchProjects.not());

		if (!CollectionUtils.isEmpty(verticalIds))
			finalPredicate.add(getInPredicate(cb, verticalIds,
					root.get("project").get("account").get("vertical").get("verticalId")));

		if (!CollectionUtils.isEmpty(accountIds))
			finalPredicate.add(getInPredicate(cb, accountIds, root.get("project").get("account").get("accountId")));

		if (!CollectionUtils.isEmpty(projectIds))
			finalPredicate.add(getInPredicate(cb, projectIds, root.get("project").get("projectId")));

		if (!CollectionUtils.isEmpty(titleIds))
			finalPredicate.add(getInPredicate(cb, titleIds, titleId));

		if (!CollectionUtils.isEmpty(workGroupIds))
			finalPredicate.add(getStringInPredicate(cb, workGroupIds, root.get("workGroup").get("name")));

		if (!CollectionUtils.isEmpty(locationIds))
			finalPredicate.add(getInPredicate(cb, locationIds, locationId));

		if (null != allocationFrom)
			finalPredicate.add(cb.equal(allocationDetailsJoin.get("startDate"), allocationFrom));

		if (!CollectionUtils.isEmpty(primarySkillIds))
			finalPredicate.add(getInPredicate(cb, primarySkillIds, primarySkillId));

		if (!CollectionUtils.isEmpty(secondarySkillIds))
			finalPredicate.add(getInPredicate(cb, secondarySkillIds, secondarySkillId));

		// Include on hold projects too if the flag is false
		if (onlyActive)
			finalPredicate.add(cb.equal(root.get("project").get("currentStatus"), "PMO_APPROVED"));
		else
			finalPredicate.add(getStringInPredicate(cb, Arrays.asList("PMO_APPROVED", "PROJECT_ON_HOLD"),
					root.get("project").get("currentStatus")));

		query.distinct(true);
		final Predicate and = cb.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));
		return and;

	}

	private Predicate getInPredicate(CriteriaBuilder cb, List<? extends Number> primarySkillIds,
			Path<Object> primarySkillId) {
		final Predicate primarySkillsPredicate = cb.in(primarySkillId).value(primarySkillIds);
		return primarySkillsPredicate;
	}

	private Predicate getStringInPredicate(CriteriaBuilder cb, List<String> values, Path<Object> pathObject) {
		return cb.in(pathObject).value(values);
	}

	private List<Long> convertToLong(List<String> input) {
		if (CollectionUtils.isEmpty(input))
			return new ArrayList<>();
		return input.stream().filter(i -> (null != i)).map(Long::parseLong).collect(Collectors.toList());
	}

	private List<Integer> convertToIntegers(final String primarySkillId) {
		return convertToIntegers(convertToStringList(primarySkillId));
	}

	private List<Integer> convertToIntegers(final List<String> input) {
		if (CollectionUtils.isEmpty(input))
			return new ArrayList<>();
		return input.stream().filter(i -> (null != i)).map(Integer::parseInt).collect(Collectors.toList());
	}

	private List<String> convertToStringList(String commaSeparatedString) {
		if (StringUtils.isEmpty(StringUtils.trim(commaSeparatedString)))
			return new ArrayList<>();

		return Stream.of(commaSeparatedString.split(",")).map(String::trim).collect(Collectors.toList());

	}

	public static Date localDateToDate(LocalDate localDate) {
		if (null == localDate)
			return null;
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
