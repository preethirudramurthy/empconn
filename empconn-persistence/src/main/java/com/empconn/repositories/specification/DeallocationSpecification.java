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

import org.springframework.data.jpa.domain.Specification;

import com.empconn.constants.ApplicationConstants;
import com.empconn.dto.allocation.DeallocationResourceListRequestDto;
import com.empconn.persistence.entities.Allocation;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.WorkGroup;
import com.empconn.util.RolesUtil;

public class DeallocationSpecification implements Specification<Allocation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5043782252794107520L;

	private static final String EMPLOYEE = "employee";

	private static final String WORK_GROUP = "workGroup";

	private static final String PROJECT_ID = "projectId";

	private static final String PROJECT = "project";

	private static final String EMPLOYEE_ID = "employeeId";

	private static final String ALLOCATION_MANAGER_ID = "allocationManagerId";

	private final DeallocationResourceListRequestDto filter;
	private final Employee loggedInEmployee;

	public DeallocationSpecification(DeallocationResourceListRequestDto filter,Employee loggedInEmployee) {
		super();
		this.filter = filter;
		this.loggedInEmployee = loggedInEmployee;
	}

	@Override
	public Predicate toPredicate(Root<Allocation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		root.fetch(EMPLOYEE);
		root.fetch("allocationDetails");
		root.fetch(PROJECT);
		root.fetch("projectLocation");
		root.fetch(EMPLOYEE).fetch("primaryAllocation").fetch("reportingManagerId");


		final List<Predicate> finalPredicate = new ArrayList<>();
		finalPredicate.add(cb.equal(root.get("isActive"), true));
		finalPredicate.add(cb.equal(root.get(EMPLOYEE).get("isActive"), true));
		finalPredicate.add(cb.not(root.get(PROJECT).get("name").in(Arrays.asList("Central Bench", "NDBench"))));


		if (filter.getVerticalIdList() != null && !filter.getVerticalIdList().isEmpty()) {
			finalPredicate.add(root.get(PROJECT).get("account").get("vertical").get("verticalId")
					.in(filter.getVerticalIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

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
			final Predicate locationPredicate = cb.in(root.get(EMPLOYEE).get("location").get("locationId"))
					.value(filter.getOrgLocationIdList().stream().map(Integer::parseInt).collect(Collectors.toList()));
			finalPredicate.add(locationPredicate);
		}

		if (filter.getWorkgroup() != null && !filter.getWorkgroup().isEmpty()) {
			finalPredicate.add(root.get(WORK_GROUP).get("name").in(new ArrayList<>(filter.getWorkgroup())));
		}

		if (filter.getBillable() != null) {
			finalPredicate.add(root.get("isBillable").in(filter.getBillable()));
		}

		if (filter.getManagerId() != null && !filter.getManagerId().isEmpty()) {
			finalPredicate.add(root.get(ALLOCATION_MANAGER_ID).get(EMPLOYEE_ID)
					.in(filter.getManagerId().stream().map(Long::parseLong).collect(Collectors.toList())));
		}

		if (filter.getReporteeType() != null && !filter.getReporteeType().isEmpty()) {
			if (filter.getReporteeType().equalsIgnoreCase("direct")) {
				finalPredicate.add(root.get(ALLOCATION_MANAGER_ID).get(EMPLOYEE_ID).in(this.loggedInEmployee.getEmployeeId()));
			} else if (filter.getReporteeType().equalsIgnoreCase("indirect")) {
				final List<Predicate> gdmPredicate = gdmSpecification(root, cb);
				finalPredicate.add(cb.or(gdmPredicate.toArray(new Predicate[0])));
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
		return cb.and(finalPredicate.toArray(new Predicate[0]));

	}

	private List<Predicate>  gdmPredicate(Root<Allocation> root, CriteriaBuilder cb) {
		final List<Predicate> finalPredicate = new ArrayList<>();
		if (RolesUtil.isGDMAndManager(this.loggedInEmployee)) {

			final List<Predicate> gdmAndManagerPredicate = new ArrayList<>();
			gdmAndManagerPredicate.add(root.get(ALLOCATION_MANAGER_ID).get(EMPLOYEE_ID).in(this.loggedInEmployee.getEmployeeId()));

			final List<Predicate> gdmPredicate = gdmSpecification(root, cb);
			gdmAndManagerPredicate.add(cb.or(gdmPredicate.toArray(new Predicate[0])));

			finalPredicate.add(cb.or(gdmAndManagerPredicate.toArray(new Predicate[0])));

		} else if (RolesUtil.isAManager(this.loggedInEmployee)) {
			finalPredicate.add(root.get(ALLOCATION_MANAGER_ID).get(EMPLOYEE_ID).in(this.loggedInEmployee.getEmployeeId()));
		} else if (RolesUtil.isOnlyGDM(this.loggedInEmployee)) {

			final List<Predicate> gdmPredicate = gdmSpecification(root, cb);
			finalPredicate.add(cb.or(gdmPredicate.toArray(new Predicate[0])));
		}
		return finalPredicate;
	}

	private List<Predicate> gdmSpecification(Root<Allocation> root, CriteriaBuilder cb) {
		final List<Predicate> gdmPredicate = new ArrayList<>();

		gdmPredicate.add(root.get(PROJECT).get(PROJECT_ID).in(allWorkgroupSpec()));

		final List<Predicate> nonQAWorkgroupPredicate = nonQAWorkgroupSpec(root);

		gdmPredicate.add(cb.and(nonQAWorkgroupPredicate.toArray(new Predicate[0])));

		final List<Predicate> qAWorkgroupPredicate = qaWorkgroupSpec(root);

		gdmPredicate.add(cb.and(qAWorkgroupPredicate.toArray(new Predicate[0])));

		return gdmPredicate;
	}

	private List<Predicate> qaWorkgroupSpec(Root<Allocation> root) {
		final List<Predicate> qAWorkgroupPredicate = new ArrayList<>();

		//For projects with both DEV GDM and QA GDM, if the logged in user id QE GDM
		//Then can deallocate all employees under only QA workgroups
		final Set<Long> qAWorkGroups = filter.getAllProjects().stream().
				filter(a -> (a.getEmployee2() != null && a.getEmployee2().getEmployeeId().equals(this.loggedInEmployee.getEmployeeId()) &&
				a.getEmployee1() != null))
				.map(Project::getProjectId)
				.collect(Collectors.toSet());

		qAWorkgroupPredicate.add(root.get(PROJECT).get(PROJECT_ID).in(qAWorkGroups));

		qAWorkgroupPredicate.add(root.get(WORK_GROUP).get("workGroupId").in(filter.getAllWorkgroups().stream().filter(w -> w.getName().equalsIgnoreCase(ApplicationConstants.QA_WORK_GROUP))
				.map(WorkGroup::getWorkGroupId).collect(Collectors.toList())));
		return qAWorkgroupPredicate;
	}

	private List<Predicate> nonQAWorkgroupSpec(Root<Allocation> root) {
		final List<Predicate> nonQAWorkgroupPredicate = new ArrayList<>();

		//For projects with both DEV GDM and QA GDM, if the logged in user id DEV GDM
		//Then can deallocate all employees under all NON QA workgroups
		final Set<Long> nonQAWorkGroups = filter.getAllProjects().stream().
				filter(a -> (a.getEmployee1() != null && a.getEmployee1().getEmployeeId().equals(this.loggedInEmployee.getEmployeeId()) &&
				a.getEmployee2() != null))
				.map(Project::getProjectId)
				.collect(Collectors.toSet());

		nonQAWorkgroupPredicate.add(root.get(PROJECT).get(PROJECT_ID).in(nonQAWorkGroups));

		nonQAWorkgroupPredicate.add(root.get(WORK_GROUP).get("workGroupId").in(filter.getAllWorkgroups().stream().filter(w -> !w.getName().equalsIgnoreCase(ApplicationConstants.QA_WORK_GROUP))
				.map(WorkGroup::getWorkGroupId).collect(Collectors.toList())));
		return nonQAWorkgroupPredicate;
	}

	private Set<Long> allWorkgroupSpec() {
		//For projects with ONLY DEV GDM or only QE GDM, GDM can deallocate all employees under all workgroups
		final java.util.function.Predicate<Project> hasOnlyDEVGDM = a -> (a.getEmployee1() != null && a.getEmployee1().getEmployeeId().equals(this.loggedInEmployee.getEmployeeId()) &&
				a.getEmployee2() == null);

		final java.util.function.Predicate<Project> hasOnlyQEGDM = a -> (a.getEmployee2() != null && a.getEmployee2().getEmployeeId().equals(this.loggedInEmployee.getEmployeeId()) &&
				a.getEmployee1() == null);

		return filter.getAllProjects().stream()
				.filter(a -> ( hasOnlyDEVGDM.test(a) || hasOnlyQEGDM.test(a)))
				.map(Project::getProjectId)
				.collect(Collectors.toSet());
	}

}
