package com.empconn.repositories.specification;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.allocation.EditReleaseDateResourceListRequestDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.WorkGroup;
import com.empconn.repositories.ProjectLocationRespository;
import com.empconn.util.RolesUtil;

public class EditReleaseDateSpecification implements Specification<Allocation> {

	@Autowired
	private ProjectLocationRespository projectLocationRespository;

	private final EditReleaseDateResourceListRequestDto filter;
	private final Employee loggedInEmployee;

	public EditReleaseDateSpecification(EditReleaseDateResourceListRequestDto filter, Employee loggedInEmployee) {
		super();
		this.filter = filter;
		this.loggedInEmployee = loggedInEmployee;
	}

	@Override
	public Predicate toPredicate(Root<Allocation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		root.fetch("employee");
		final List<Predicate> finalPredicate = new ArrayList<Predicate>();
		finalPredicate.add(cb.equal(root.get("isActive"), true));
		finalPredicate.add(cb.equal(root.get("employee").get("isActive"), true));
		finalPredicate.add(cb.not(root.get("project").get("name").in(Arrays.asList("Central Bench", "NDBench"))));

		if (filter.getVerticalIdList() != null && !filter.getVerticalIdList().isEmpty()) {
			finalPredicate.add(root.get("project").get("account").get("vertical").get("verticalId")
					.in(filter.getVerticalIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getTitleIdList() != null && !filter.getTitleIdList().isEmpty()) {
			finalPredicate.add(root.get("employee").get("title").get("titleId")
					.in(filter.getTitleIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getProjectIdList() != null && !filter.getProjectIdList().isEmpty()) {
			finalPredicate.add(root.get("project").get("projectId")
					.in(filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList())));
		}

		if (filter.getAccountIdList() != null && !filter.getAccountIdList().isEmpty()) {
			finalPredicate.add(root.get("project").get("account").get("accountId")
					.in(filter.getAccountIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getOrgLocationIdList() != null && !filter.getOrgLocationIdList().isEmpty()) {
			final Predicate locationPredicate = cb.in(root.get("employee").get("location").get("locationId"))
					.value(filter.getOrgLocationIdList().stream().map(Integer::parseInt).collect(Collectors.toList()));
			finalPredicate.add(locationPredicate);
		}

		if (filter.getWorkgroup() != null && !filter.getWorkgroup().isEmpty()) {
			finalPredicate.add(
					root.get("workGroup").get("name").in(filter.getWorkgroup().stream().collect(Collectors.toList())));
		}

		if (filter.getBillable() != null) {
			finalPredicate.add(root.get("isBillable").in(filter.getBillable()));
		}

		if (filter.getManagerId() != null && !filter.getManagerId().isEmpty()) {
			finalPredicate.add(root.get("allocationManagerId").get("employeeId")
					.in(filter.getManagerId().stream().map(Long::parseLong).collect(Collectors.toList())));
		}

		if (filter.getReporteeType() != null && !filter.getReporteeType().isEmpty()) {
			if (filter.getReporteeType().equalsIgnoreCase("direct")) {
				finalPredicate.add(
						root.get("allocationManagerId").get("employeeId").in(this.loggedInEmployee.getEmployeeId()));
			} else if (filter.getReporteeType().equalsIgnoreCase("indirect")) {
				final List<Predicate> gdmPredicate = gdmSpecification(root, cb);
				finalPredicate.add(cb.or(gdmPredicate.toArray(new Predicate[gdmPredicate.size()])));
			} else if (filter.getReporteeType().equalsIgnoreCase("all")) {
				finalPredicate.addAll(gdmPredicate(root, cb));

			}
		} else {

			finalPredicate.addAll(gdmPredicate(root, cb));

		}

		if (filter.getReleaseDateBefore() != null) {
			finalPredicate.add(cb.lessThan(root.get("releaseDate"),
					Date.from(filter.getReleaseDateBefore().atStartOfDay(ZoneId.systemDefault()).toInstant())));
		}

		query.distinct(true);
		query.orderBy(cb.asc(root.get("employee").get("firstName")), cb.asc(root.get("employee").get("lastName")));
		final Predicate and = cb.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));
		return and;

	}

	private List<Predicate> gdmPredicate(Root<Allocation> root, CriteriaBuilder cb) {
		final List<Predicate> finalPredicate = new ArrayList<Predicate>();
		if (RolesUtil.isGDMAndManager(this.loggedInEmployee)) {

			final List<Predicate> gdmAndManagerPredicate = new ArrayList<Predicate>();
			gdmAndManagerPredicate
			.add(root.get("allocationManagerId").get("employeeId").in(this.loggedInEmployee.getEmployeeId()));

			final List<Predicate> gdmPredicate = gdmSpecification(root, cb);
			gdmAndManagerPredicate.add(cb.or(gdmPredicate.toArray(new Predicate[gdmPredicate.size()])));

			finalPredicate.add(cb.or(gdmAndManagerPredicate.toArray(new Predicate[gdmAndManagerPredicate.size()])));

		} else if (RolesUtil.isAManager(this.loggedInEmployee)) {
			finalPredicate
			.add(root.get("allocationManagerId").get("employeeId").in(this.loggedInEmployee.getEmployeeId()));
		} else if (RolesUtil.isOnlyGDM(this.loggedInEmployee)) {

			final List<Predicate> gdmPredicate = gdmSpecification(root, cb);
			finalPredicate.add(cb.or(gdmPredicate.toArray(new Predicate[gdmPredicate.size()])));
		}
		return finalPredicate;
	}

	private List<Predicate> gdmSpecification(Root<Allocation> root, CriteriaBuilder cb) {
		final List<Predicate> gdmPredicate = new ArrayList<Predicate>();

		gdmPredicate.add(root.get("project").get("projectId").in(allWorkgroupSpec()));

		final List<Predicate> nonQAWorkgroupPredicate = nonQAWorkgroupSpec(root);

		gdmPredicate.add(cb.and(nonQAWorkgroupPredicate.toArray(new Predicate[nonQAWorkgroupPredicate.size()])));

		final List<Predicate> qAWorkgroupPredicate = qaWorkgroupSpec(root);

		gdmPredicate.add(cb.and(qAWorkgroupPredicate.toArray(new Predicate[qAWorkgroupPredicate.size()])));

		return gdmPredicate;
	}

	private List<Predicate> qaWorkgroupSpec(Root<Allocation> root) {
		final List<Predicate> qAWorkgroupPredicate = new ArrayList<Predicate>();

		// For projects with both DEV GDM and QA GDM, if the logged in user id QE GDM
		// Then can deallocate all employees under only QA workgroups
		final Set<Long> qAWorkGroups = filter.getAllProjects().stream()
				.filter(a -> (a.getEmployee2() != null
				&& a.getEmployee2().getEmployeeId().equals(this.loggedInEmployee.getEmployeeId())
				&& a.getEmployee1() != null))
				.map(Project::getProjectId).collect(Collectors.toSet());

		qAWorkgroupPredicate.add(root.get("project").get("projectId").in(qAWorkGroups));

		qAWorkgroupPredicate.add(root.get("workGroup").get("workGroupId")
				.in(filter.getAllWorkgroups().stream()
						.filter(w -> w.getName().equalsIgnoreCase(ApplicationConstants.QA_WORK_GROUP))
						.map(WorkGroup::getWorkGroupId).collect(Collectors.toList())));
		return qAWorkgroupPredicate;
	}

	private List<Predicate> nonQAWorkgroupSpec(Root<Allocation> root) {
		final List<Predicate> nonQAWorkgroupPredicate = new ArrayList<Predicate>();

		// For projects with both DEV GDM and QA GDM, if the logged in user id DEV GDM
		// Then can deallocate all employees under all NON QA workgroups
		final Set<Long> nonQAWorkGroups = filter.getAllProjects().stream()
				.filter(a -> (a.getEmployee1() != null
				&& a.getEmployee1().getEmployeeId().equals(this.loggedInEmployee.getEmployeeId())
				&& a.getEmployee2() != null))
				.map(Project::getProjectId).collect(Collectors.toSet());

		nonQAWorkgroupPredicate.add(root.get("project").get("projectId").in(nonQAWorkGroups));

		nonQAWorkgroupPredicate.add(root.get("workGroup").get("workGroupId")
				.in(filter.getAllWorkgroups().stream()
						.filter(w -> !w.getName().equalsIgnoreCase(ApplicationConstants.QA_WORK_GROUP))
						.map(WorkGroup::getWorkGroupId).collect(Collectors.toList())));
		return nonQAWorkgroupPredicate;
	}

	private Set<Long> allWorkgroupSpec() {
		// For projects with ONLY DEV GDM or only QE GDM, GDM can deallocate all
		// employees under all workgroups
		final java.util.function.Predicate<Project> hasOnlyDEVGDM = a -> (a.getEmployee1() != null
				&& a.getEmployee1().getEmployeeId().equals(this.loggedInEmployee.getEmployeeId())
				&& a.getEmployee2() == null);

		final java.util.function.Predicate<Project> hasOnlyQEGDM = a -> (a.getEmployee2() != null
				&& a.getEmployee2().getEmployeeId().equals(this.loggedInEmployee.getEmployeeId())
				&& a.getEmployee1() == null);

		final Set<Long> allWorkgroups = filter.getAllProjects().stream()
				.filter(a -> (hasOnlyDEVGDM.test(a) || hasOnlyQEGDM.test(a))).map(Project::getProjectId)
				.collect(Collectors.toSet());
		return allWorkgroups;
	}

}
