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

	public static Specification<Project> filterSoftDeleted() {
		return (project, cq, cb) -> cb.notEqual(project.get("isActive"), false);
	}

	public static Specification<Project> filterVertical(Integer verticalId) {
		return (project, cq, cb) -> cb.equal(project.get("account").get("vertical").get("verticalId"), verticalId);
	}

	public static Specification<Project> filterHorizontal(Set<Integer> horizontalIds) {
		return (project, cq, cb) -> cb.in(project.get("horizontal").get("horizontalId")).value(horizontalIds);
	}

	public static Specification<Project> filterAccountId(Integer accountId) {
		return (project, cq, cb) -> cb.equal(project.get("account").get("accountId"), accountId);
	}

	public static Specification<Project> filterSubCategory(Set<Integer> projectSubCategoryIds) {
		return (project, cq, cb) -> cb.in(project.get("projectSubCategory").get("projectSubCategoryId"))
				.value(projectSubCategoryIds);
	}

	public static Specification<Project> filterNotInactive() {
		return (project, cq, cb) -> cb.notEqual(project.get("currentStatus"), ProjectStatus.PROJECT_INACTIVE.name());
	}

	public static Specification<Project> filterOnHold() {
		return (project, cq, cb) -> cb.equal(project.get("currentStatus"), ProjectStatus.PROJECT_ON_HOLD.name());
	}

	public static Specification<Project> filterActive() {
		return (project, cq, cb) -> cb.equal(project.get("currentStatus"), ProjectStatus.PMO_APPROVED.name());
	}

	public static Specification<Project> filterCurrentStatusIn(Set<String> statuses) {
		return (project, cq, cb) -> cb.in(project.get("currentStatus")).value(statuses);
	}

	public static Specification<Project> filterStartDateBetween(Date fromDate, Date toDate) {
		return (project, cq, cb) -> cb.between(project.get("startDate"), fromDate, toDate);
	}

	public static Specification<Project> filterProjectIn(Set<Long> projectIds) {
		return (project, cq, cb) -> cb.in(project.get("projectId")).value(projectIds);
	}

	public static Specification<Project> isDevGdm(Long employeeId) {
		return (project, cq, cb) -> cb.equal(project.get("employee1").get("employeeId"), employeeId);
	}

	public static Specification<Project> isQaGdm(Long employeeId) {
		return (project, cq, cb) -> cb.equal(project.get("employee2").get("employeeId"), employeeId);
	}

	public static Specification<Project> isDevGdmNotPresent() {
		return (project, cq, cb) -> cb.isNull(project.get("employee1"));
	}

	public static Specification<Checklist> orderbyChecklistId() {
		return (cl, cq, cb) -> {
			Predicate p = cb.conjunction();
			p = cb.and(p, cb.notEqual(cl.get("isActive"), false));
			cq.orderBy(cb.asc(cl.get("checklistId")));
			return p;
		};
	}

	public static Specification<Project> isOneOfManager(Long employeeId) {
		return (project, cq, cb) -> {
			Predicate p = cb.conjunction();
			final Join<Project, ProjectLocation> location = project.join("projectLocations");
			p = cb.and(p, cb.notEqual(location.get("isActive"), false));
			p = cb.and(p, cb.equal(location.get("employee1").get("employeeId"), employeeId));
			p = cb.or(p, cb.equal(location.get("employee2").get("employeeId"), employeeId));
			p = cb.or(p, cb.equal(location.get("employee3").get("employeeId"), employeeId));
			p = cb.or(p, cb.equal(location.get("employee4").get("employeeId"), employeeId));
			p = cb.or(p, cb.equal(location.get("employee5").get("employeeId"), employeeId));
			return p;
		};

	}

	public static Specification<Project> filterSoftDeletedProjectLocationJoin() {
		return (project, cq, cb) -> {
			final Join<Project, ProjectLocation> location = project.join("projectLocations");
			return cb.notEqual(location.get("isActive"), false);
		};
	}

	public static String[] searchStatuses = new String[] { ProjectStatus.PMO_APPROVED.name(),
			ProjectStatus.PROJECT_ON_HOLD.name(), ProjectStatus.PROJECT_INACTIVE.name() };

	public static String[] searchStatusesWithOutInactive = new String[] { ProjectStatus.PMO_APPROVED.name(),
			ProjectStatus.PROJECT_ON_HOLD.name() };

	public static Specification<Project> getProjectManagerByAccountProjectVertical(
			DropdownManagerDto dropdownManagerDto) {
		return (cl, cq, cb) -> {
			Predicate p = cb.conjunction();
			cl.fetch("projectLocations");
			p = cb.and(p, cb.notEqual(cl.get("isActive"), false));
			if (dropdownManagerDto.getProjectNameList() != null) {
				p = cb.and(p, cb.in(cl.get("projectId")).value(dropdownManagerDto.getProjectNameList().stream()
						.map(Long::parseLong).collect(Collectors.toList())));
			}
			if (dropdownManagerDto.getAccountNameList() != null) {
				p = cb.and(p, cb.in(cl.get("account").get("accountId")).value(dropdownManagerDto.getAccountNameList()
						.stream().map(Integer::parseInt).collect(Collectors.toList())));
			}
			if (dropdownManagerDto.getVerticalIdList() != null) {
				p = cb.and(p, cb.in(cl.get("account").get("vertical").get("verticalId")).value(dropdownManagerDto
						.getVerticalIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
			}
			return p;
		};
	}
}
