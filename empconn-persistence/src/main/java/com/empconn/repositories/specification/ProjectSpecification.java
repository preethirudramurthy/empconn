package com.empconn.repositories.specification;

import java.util.ArrayList;
import java.util.Arrays;
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

	public static Specification<Project> getEmployeesByPhoneTypeSpec() {
		return (root, query, criteriaBuilder) -> {
			final List<Long> l = new ArrayList<Long>();
			l.add(1L);
			final Join<Project, ProjectLocation> phoneJoin = root.join("projectLocations");
			final Predicate equalPredicate = (phoneJoin.get("projectLocationId").in(l));
			query.distinct(true);
			return equalPredicate;
		};
	}

	public static Specification<Project> nonBenchSpec(Boolean withBench) {
		return (root, query, criteriaBuilder) -> {
			final List<Predicate> finalPredicate = new ArrayList<Predicate>();
			finalPredicate.add(criteriaBuilder.equal(root.get("project").get("isActive"), true));
			if (!withBench) {
				final List<String> benchProjects = Arrays.asList("Central Bench", "NDBench");
				finalPredicate.add(root.get("project").get("name").in(benchProjects).not());
			}
			query.distinct(true);
			final Predicate and = criteriaBuilder.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));
			return and;
		};
	}

	public static Specification<Project> getProjectsSpec(ProjectDropdownDto request) {
		return (root, query, cb) -> {
			final java.util.function.Predicate<? super String> notEmpty = v -> !StringUtils.isEmpty(v);
			final List<Predicate> finalPredicate = new ArrayList<Predicate>();
			finalPredicate.add(cb.equal(root.get("isActive"), true));
			final List<String> benchProjects = Arrays.asList("Central Bench", "NDBench");
			if (request.getIsActive() == null || (request.getIsActive() != null && request.getIsActive())) {
				finalPredicate.add(root.get("currentStatus")
						.in(Arrays.asList(ProjectStatus.PMO_APPROVED.name(), ProjectStatus.PROJECT_ON_HOLD.name())));
			}
			if (!CollectionUtils.isEmpty(request.getVerticalIdList())) {
				finalPredicate.add(root.get("account").get("vertical").get("verticalId").in(request.getVerticalIdList()
						.stream().filter(notEmpty).map(Integer::parseInt).collect(Collectors.toList())));
			}
			if (request.getAccountIdList() != null && request.getAccountIdList().size() > 0) {
				finalPredicate.add(root.get("account").get("accountId").in(request.getAccountIdList().stream()
						.filter(notEmpty).map(Integer::parseInt).collect(Collectors.toList())));
			}
			if (request.getProjectIds() != null && request.getProjectIds().size() > 0) {
				finalPredicate.add(root.get("projectId").in(request.getProjectIds()));
			}
			if (request.getIncludeBench() != null && request.getIncludeBench()) {
				finalPredicate.add(cb.or(cb.in(root.get("name")).value(benchProjects),
						cb.in(root.get("name")).value(benchProjects).not()));
			} else if (request.getIncludeBench() != null && !request.getIncludeBench()) {
				finalPredicate.add(cb.in(root.get("name")).value(benchProjects).not());
			}
			if (request.getIncludePracticeBench() != null && request.getIncludePracticeBench()) {
				final Predicate practiceBenchPredicate = cb.and(cb.equal(root.get("account").get("name"), "Bench"),
						cb.in(root.get("name")).value(benchProjects).not());
				finalPredicate.add(cb.or(practiceBenchPredicate, practiceBenchPredicate.not()));
			} else if (request.getIncludePracticeBench() != null && !request.getIncludePracticeBench()) {
				final Predicate practiceBenchPredicate = cb.and(cb.equal(root.get("account").get("name"), "Bench"),
						cb.in(root.get("name")).value(benchProjects).not());
				finalPredicate.add(practiceBenchPredicate.not());
			}

			if (request.getOnlyFutureReleaseDate() != null) {
				finalPredicate.add(cb.greaterThanOrEqualTo(root.get("project").get("endDate"), cb.currentDate()));
			}
			if (request.getPartial() != null && !StringUtils.isEmpty(request.getPartial())) {
				finalPredicate.add(cb.like(root.get("name"), request.getPartial() + "%"));
			}
			query.distinct(true);
			final Predicate and = cb.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));
			return and;
		};
	}

	public static Specification<Project> getProjectsSpec(ProjectForAllocationDto request, Employee loggedInUser) {
		return (root, query, cb) -> {
			final Long loggedInEmployeeId = loggedInUser.getEmployeeId();
			final List<Predicate> finalPredicate = new ArrayList<Predicate>();
			finalPredicate.add(cb.equal(root.get("isActive"), true));
			if (request.getIsActive() == null || (request.getIsActive() != null && request.getIsActive())) {
				finalPredicate.add(root.get("currentStatus").in(Arrays.asList(ProjectStatus.PMO_APPROVED.name())));
			}

			if (request.getAccountId() != null) {
				finalPredicate.add(cb.equal(root.get("account").get("accountId"),
						Integer.parseInt(request.getAccountId())));
			}

			if (request.getWithBench() != null && request.getWithBench()) {
				final List<String> benchProjects = Arrays.asList("Central Bench", "NDBench");
				finalPredicate.add(root.get("name").in(benchProjects));
			} else if (request.getWithBench() != null && !request.getWithBench()) {
				final List<String> benchProjects = Arrays.asList("Central Bench", "NDBench");
				finalPredicate.add(root.get("name").in(benchProjects).not());
			}
			if (request.getOnlyFutureReleaseDate() != null) {
				finalPredicate.add(cb.greaterThanOrEqualTo(root.get("project").get("endDate"),
						cb.currentDate()));
			}
			if (request.getPartial() != null && !StringUtils.isEmpty(request.getPartial())) {
				finalPredicate.add(cb.like(root.get("name"), request.getPartial() + "%"));
			}

			if (RolesUtil.isGDMAndManager(loggedInUser)) {

				final List<Predicate> managerAndGdmPredicate = new ArrayList<Predicate>();

				final Join<Project, ProjectLocation> location = root.join("projectLocations");
				finalPredicate.add(cb.equal(location.get("isActive"), true));
				final List<Predicate> managerPredicate = new ArrayList<Predicate>();
				managerPredicate.add(cb.equal(location.get("employee1").get("employeeId"), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee2").get("employeeId"), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee3").get("employeeId"), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee4").get("employeeId"), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee5").get("employeeId"), loggedInEmployeeId));
				managerAndGdmPredicate.add(cb.or(managerPredicate.toArray(new Predicate[managerPredicate.size()])));

				final List<Predicate> gdmPredicate = new ArrayList<Predicate>();
				gdmPredicate.add(cb.equal(root.get("employee1").get("employeeId"), loggedInEmployeeId));
				gdmPredicate.add(cb.equal(root.get("employee2").get("employeeId"), loggedInEmployeeId));
				managerAndGdmPredicate.add(cb.or(gdmPredicate.toArray(new Predicate[gdmPredicate.size()])));

				finalPredicate.add(cb.or(managerAndGdmPredicate.toArray(new Predicate[managerAndGdmPredicate.size()])));

			} else if (RolesUtil.isOnlyGDM(loggedInUser)) {
				final List<Predicate> gdmPredicate = new ArrayList<Predicate>();
				gdmPredicate.add(cb.equal(root.get("employee1").get("employeeId"), loggedInEmployeeId));
				gdmPredicate.add(cb.equal(root.get("employee2").get("employeeId"), loggedInEmployeeId));

				finalPredicate.add(cb.or(gdmPredicate.toArray(new Predicate[gdmPredicate.size()])));
			} else if (RolesUtil.isAManager(loggedInUser)) {
				final Join<Project, ProjectLocation> location = root.join("projectLocations");
				finalPredicate.add(cb.equal(location.get("isActive"), true));
				final List<Predicate> managerPredicate = new ArrayList<Predicate>();
				managerPredicate.add(cb.equal(location.get("employee1").get("employeeId"), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee2").get("employeeId"), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee3").get("employeeId"), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee4").get("employeeId"), loggedInEmployeeId));
				managerPredicate.add(cb.equal(location.get("employee5").get("employeeId"), loggedInEmployeeId));
				finalPredicate.add(cb.or(managerPredicate.toArray(new Predicate[managerPredicate.size()])));
			}

			query.distinct(true);
			final Predicate and = cb.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));
			return and;
		};
	}

}
