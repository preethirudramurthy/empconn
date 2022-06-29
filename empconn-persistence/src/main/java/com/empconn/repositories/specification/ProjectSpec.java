package com.empconn.repositories.specification;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.empconn.dto.earmark.DropdownManagerDto;
import com.empconn.enums.ProjectStatus;
import com.empconn.persistence.entities.Checklist;
import com.empconn.persistence.entities.Project;
import com.empconn.persistence.entities.ProjectLocation;

public class ProjectSpec {

	private static final String PROJECT_LOCATIONS = "projectLocations";

	private static final String IS_ACTIVE = "isActive";

	private static final String EMPLOYEE_ID = "employeeId";

	private static final String EMPLOYEE1 = "employee1";

	private static final String CURRENT_STATUS = "currentStatus";

	private static final String ACCOUNT = "account";

	public static Specification<Project> filterSoftDeleted() {
		return (project, cq, cb) -> cb.notEqual(project.get(IS_ACTIVE), false);
	}

	public static Specification<Project> filterVertical(Integer verticalId) {
		return (project, cq, cb) -> cb.equal(project.get(ACCOUNT).get("vertical").get("verticalId"), verticalId);
	}

	public static Specification<Project> filterHorizontal(Set<Integer> horizontalIds) {
		return (project, cq, cb) -> cb.in(project.get("horizontal").get("horizontalId")).value(horizontalIds);
	}

	public static Specification<Project> filterAccountId(Integer accountId) {
		return (project, cq, cb) -> cb.equal(project.get(ACCOUNT).get("accountId"), accountId);
	}

	public static Specification<Project> filterSubCategory(Set<Integer> projectSubCategoryIds) {
		return (project, cq, cb) -> cb.in(project.get("projectSubCategory").get("projectSubCategoryId"))
				.value(projectSubCategoryIds);
	}

	public static Specification<Project> filterNotInactive() {
		return (project, cq, cb) -> cb.notEqual(project.get(CURRENT_STATUS), ProjectStatus.PROJECT_INACTIVE.name());
	}

	public static Specification<Project> filterOnHold() {
		return (project, cq, cb) -> cb.equal(project.get(CURRENT_STATUS), ProjectStatus.PROJECT_ON_HOLD.name());
	}

	public static Specification<Project> filterActive() {
		return (project, cq, cb) -> cb.equal(project.get(CURRENT_STATUS), ProjectStatus.PMO_APPROVED.name());
	}

	public static Specification<Project> filterCurrentStatusIn(Set<String> statuses) {
		return (project, cq, cb) -> cb.in(project.get(CURRENT_STATUS)).value(statuses);
	}

	public static Specification<Project> filterStartDateBetween(Date fromDate, Date toDate) {
		return (project, cq, cb) -> cb.between(project.get("startDate"), fromDate, toDate);
	}

	public static Specification<Project> filterProjectIn(Set<Long> projectIds) {
		return (project, cq, cb) -> cb.in(project.get("projectId")).value(projectIds);
	}

	public static Specification<Project> isDevGdm(Long employeeId) {
		return (project, cq, cb) -> cb.equal(project.get(EMPLOYEE1).get(EMPLOYEE_ID), employeeId);
	}

	public static Specification<Project> isQaGdm(Long employeeId) {
		return (project, cq, cb) -> cb.equal(project.get("employee2").get(EMPLOYEE_ID), employeeId);
	}

	public static Specification<Project> isDevGdmNotPresent() {
		return (project, cq, cb) -> cb.isNull(project.get(EMPLOYEE1));
	}

	public static Specification<Checklist> orderbyChecklistId() {
		return (cl, cq, cb) -> {
			Predicate p = cb.conjunction();
			p = cb.and(p, cb.notEqual(cl.get(IS_ACTIVE), false));
			cq.orderBy(cb.asc(cl.get("checklistId")));
			return p;
		};
	}

	public static Specification<Project> isOneOfManager(Long employeeId) {
		return (project, cq, cb) -> {
			Predicate p = cb.conjunction();
			final Join<Project, ProjectLocation> location = project.join(PROJECT_LOCATIONS);
			p = cb.and(p, cb.notEqual(location.get(IS_ACTIVE), false));
			p = cb.and(p, cb.equal(location.get(EMPLOYEE1).get(EMPLOYEE_ID), employeeId));
			p = cb.or(p, cb.equal(location.get("employee2").get(EMPLOYEE_ID), employeeId));
			p = cb.or(p, cb.equal(location.get("employee3").get(EMPLOYEE_ID), employeeId));
			p = cb.or(p, cb.equal(location.get("employee4").get(EMPLOYEE_ID), employeeId));
			p = cb.or(p, cb.equal(location.get("employee5").get(EMPLOYEE_ID), employeeId));
			return p;
		};

	}

	public static Specification<Project> filterSoftDeletedProjectLocationJoin() {
		return (project, cq, cb) -> {
			final Join<Project, ProjectLocation> location = project.join(PROJECT_LOCATIONS);
			return cb.notEqual(location.get(IS_ACTIVE), false);
		};
	}

	public static final String[] searchStatuses = new String[] { ProjectStatus.PMO_APPROVED.name(),
			ProjectStatus.PROJECT_ON_HOLD.name(), ProjectStatus.PROJECT_INACTIVE.name() };

	public static final String[] searchStatusesWithOutInactive = new String[] { ProjectStatus.PMO_APPROVED.name(),
			ProjectStatus.PROJECT_ON_HOLD.name() };

	public static Specification<Project> getProjectManagerByAccountProjectVertical(
			DropdownManagerDto dropdownManagerDto) {
		return (cl, cq, cb) -> {
			Predicate p = cb.conjunction();
			cl.fetch(PROJECT_LOCATIONS);
			p = cb.and(p, cb.notEqual(cl.get(IS_ACTIVE), false));
			if (dropdownManagerDto.getProjectNameList() != null) {
				p = cb.and(p, cb.in(cl.get("projectId")).value(dropdownManagerDto.getProjectNameList().stream()
						.map(Long::parseLong).collect(Collectors.toList())));
			}
			if (dropdownManagerDto.getAccountNameList() != null) {
				p = cb.and(p, cb.in(cl.get(ACCOUNT).get("accountId")).value(dropdownManagerDto.getAccountNameList()
						.stream().map(Integer::parseInt).collect(Collectors.toList())));
			}
			if (dropdownManagerDto.getVerticalIdList() != null) {
				p = cb.and(p, cb.in(cl.get(ACCOUNT).get("vertical").get("verticalId")).value(dropdownManagerDto
						.getVerticalIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
			}
			return p;
		};
	}
}
