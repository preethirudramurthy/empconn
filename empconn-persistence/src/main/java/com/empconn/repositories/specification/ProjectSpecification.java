package com.empconn.repositories.specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.empconn.dto.ProjectDropdownDto;
import com.empconn.dto.ProjectForAllocationDto;
import com.empconn.enums.ProjectStatus;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;
import com.empconn.util.RolesUtil;

@Component
public class ProjectSpecification {

	private static final String PROJECT_LOCATIONS = "projectLocations";
	private static final String PROJECT = "project";
	private static final String IS_ACTIVE = "isActive";
	private static final String EMPLOYEE_ID = "employeeId";
	private static final String EMPLOYEE2 = "employee2";
	private static final String EMPLOYEE1 = "employee1";
	private static final String ACCOUNT = "account";
	private static final String ND_BENCH = "NDBench";
	private static final String CENTRAL_BENCH = "Central Bench";

	public static Specification<Project> getEmployeesByPhoneTypeSpec() {
		return (root, query, criteriaBuilder) -> {
			final List<Long> l = new ArrayList<>();
			l.add(1L);
			final Join<Project, ProjectLocation> phoneJoin = root.join(PROJECT_LOCATIONS);
			final Predicate equalPredicate = (phoneJoin.get("projectLocationId").in(l));
			query.distinct(true);
			return equalPredicate;
		};
	}

	public static Specification<Project> nonBenchSpec(boolean withBench) {
		return (root, query, criteriaBuilder) -> {
			final List<Predicate> finalPredicate = new ArrayList<>();
			finalPredicate.add(criteriaBuilder.equal(root.get(PROJECT).get(IS_ACTIVE), true));
			if (!withBench) {
				final List<String> benchProjects = Arrays.asList(CENTRAL_BENCH, ND_BENCH);
				finalPredicate.add(root.get(PROJECT).get("name").in(benchProjects).not());
			}
			query.distinct(true);
			return criteriaBuilder.and(finalPredicate.toArray(new Predicate[0]));
		};
	}

	public static Specification<Project> getProjectsSpec(ProjectDropdownDto request) {
		return (root, query, cb) -> {
			final java.util.function.Predicate<? super String> notEmpty = v -> !StringUtils.isEmpty(v);
			final List<Predicate> finalPredicate = new ArrayList<>();
			finalPredicate.add(cb.equal(root.get(IS_ACTIVE), true));
			final List<String> benchProjects = Arrays.asList(CENTRAL_BENCH, ND_BENCH);
			if (request.getIsActive() == null || (request.getIsActive() != null && request.getIsActive())) {
				finalPredicate.add(root.get("currentStatus")
						.in(Arrays.asList(ProjectStatus.PMO_APPROVED.name(), ProjectStatus.PROJECT_ON_HOLD.name())));
			}
			if (!CollectionUtils.isEmpty(request.getVerticalIdList())) {
				finalPredicate.add(root.get(ACCOUNT).get("vertical").get("verticalId").in(request.getVerticalIdList()
						.stream().filter(notEmpty).map(Integer::parseInt).collect(Collectors.toList())));
			}
			if (request.getAccountIdList() != null && !request.getAccountIdList().isEmpty()) {
				finalPredicate.add(root.get(ACCOUNT).get("accountId").in(request.getAccountIdList().stream()
						.filter(notEmpty).map(Integer::parseInt).collect(Collectors.toList())));
			}
			if (request.getProjectIds() != null && !request.getProjectIds().isEmpty()) {
				finalPredicate.add(root.get("projectId").in(request.getProjectIds()));
			}
			if (request.getIncludeBench() != null && request.getIncludeBench()) {
				finalPredicate.add(cb.or(cb.in(root.get("name")).value(benchProjects),
						cb.in(root.get("name")).value(benchProjects).not()));
			} else if (request.getIncludeBench() != null && !request.getIncludeBench()) {
				finalPredicate.add(cb.in(root.get("name")).value(benchProjects).not());
			}
			if (request.getIncludePracticeBench() != null && request.getIncludePracticeBench()) {
				final Predicate practiceBenchPredicate = cb.and(cb.equal(root.get(ACCOUNT).get("name"), "Bench"),
						cb.in(root.get("name")).value(benchProjects).not());
				finalPredicate.add(cb.or(practiceBenchPredicate, practiceBenchPredicate.not()));
			} else if (request.getIncludePracticeBench() != null && !request.getIncludePracticeBench()) {
				final Predicate practiceBenchPredicate = cb.and(cb.equal(root.get(ACCOUNT).get("name"), "Bench"),
						cb.in(root.get("name")).value(benchProjects).not());
				finalPredicate.add(practiceBenchPredicate.not());
			}

			if (request.getOnlyFutureReleaseDate() != null) {
				finalPredicate.add(cb.greaterThanOrEqualTo(root.get(PROJECT).get("endDate"), cb.currentDate()));
			}
			if (request.getPartial() != null && !StringUtils.isEmpty(request.getPartial())) {
				finalPredicate.add(cb.like(root.get("name"), request.getPartial() + "%"));
			}
			query.distinct(true);
			return cb.and(finalPredicate.toArray(new Predicate[0]));
		};
	}

	public static Specification<Project> getProjectsSpec(ProjectForAllocationDto request, Employee loggedInUser) {
		return (root, query, cb) -> {
			final Long loggedInEmployeeId = loggedInUser.getEmployeeId();
			final List<Predicate> finalPredicate = new ArrayList<>();
			finalPredicate.add(cb.equal(root.get(IS_ACTIVE), true));
			if (request.getIsActive() == null || (request.getIsActive() != null && request.getIsActive())) {
				finalPredicate.add(root.get("currentStatus").in(Collections.singletonList(ProjectStatus.PMO_APPROVED.name())));
			}

			if (request.getAccountId() != null) {
				finalPredicate.add(cb.equal(root.get(ACCOUNT).get("accountId"),
						Integer.parseInt(request.getAccountId())));
			}

			if (request.getWithBench() != null && request.getWithBench()) {
				final List<String> benchProjects = Arrays.asList(CENTRAL_BENCH, ND_BENCH);
				finalPredicate.add(root.get("name").in(benchProjects));
			} else if (request.getWithBench() != null && !request.getWithBench()) {
				final List<String> benchProjects = Arrays.asList(CENTRAL_BENCH, ND_BENCH);
				finalPredicate.add(root.get("name").in(benchProjects).not());
			}
			if (request.getOnlyFutureReleaseDate() != null) {
				finalPredicate.add(cb.greaterThanOrEqualTo(root.get(PROJECT).get("endDate"),
						cb.currentDate()));
			}
			if (request.getPartial() != null && !StringUtils.isEmpty(request.getPartial())) {
				finalPredicate.add(cb.like(root.get("name"), request.getPartial() + "%"));
			}

			if (RolesUtil.isGDMAndManager(loggedInUser)) {

				final List<Predicate> managerAndGdmPredicate = new ArrayList<>();

				final Join<Project, ProjectLocation> location = root.join(PROJECT_LOCATIONS);
				finalPredicate.add(cb.equal(location.get(IS_ACTIVE), true));
				final List<Predicate> managerPredicate = new ArrayList<>();
				managerPredicate.add(cb.equal(location.get(EMPLOYEE1).get(EMPLOYEE_ID), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get(EMPLOYEE2).get(EMPLOYEE_ID), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee3").get(EMPLOYEE_ID), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee4").get(EMPLOYEE_ID), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee5").get(EMPLOYEE_ID), loggedInEmployeeId));
				managerAndGdmPredicate.add(cb.or(managerPredicate.toArray(new Predicate[0])));

				final List<Predicate> gdmPredicate = new ArrayList<>();
				gdmPredicate.add(cb.equal(root.get(EMPLOYEE1).get(EMPLOYEE_ID), loggedInEmployeeId));
				gdmPredicate.add(cb.equal(root.get(EMPLOYEE2).get(EMPLOYEE_ID), loggedInEmployeeId));
				managerAndGdmPredicate.add(cb.or(gdmPredicate.toArray(new Predicate[0])));

				finalPredicate.add(cb.or(managerAndGdmPredicate.toArray(new Predicate[0])));

			} else if (RolesUtil.isOnlyGDM(loggedInUser)) {
				final List<Predicate> gdmPredicate = new ArrayList<>();
				gdmPredicate.add(cb.equal(root.get(EMPLOYEE1).get(EMPLOYEE_ID), loggedInEmployeeId));
				gdmPredicate.add(cb.equal(root.get(EMPLOYEE2).get(EMPLOYEE_ID), loggedInEmployeeId));

				finalPredicate.add(cb.or(gdmPredicate.toArray(new Predicate[0])));
			} else if (RolesUtil.isAManager(loggedInUser)) {
				final Join<Project, ProjectLocation> location = root.join(PROJECT_LOCATIONS);
				finalPredicate.add(cb.equal(location.get(IS_ACTIVE), true));
				final List<Predicate> managerPredicate = new ArrayList<>();
				managerPredicate.add(cb.equal(location.get(EMPLOYEE1).get(EMPLOYEE_ID), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get(EMPLOYEE2).get(EMPLOYEE_ID), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee3").get(EMPLOYEE_ID), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee4").get(EMPLOYEE_ID), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee5").get(EMPLOYEE_ID), loggedInEmployeeId));
				finalPredicate.add(cb.or(managerPredicate.toArray(new Predicate[0])));
			}

			query.distinct(true);
			return cb.and(finalPredicate.toArray(new Predicate[0]));
		};
	}

}
