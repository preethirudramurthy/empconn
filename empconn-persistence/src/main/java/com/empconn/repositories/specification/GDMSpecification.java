package com.empconn.repositories.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.empconn.constants.Roles;
import com.empconn.dto.earmark.DropdownGDMDto;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;
import com.empconn.persistence.entities.Project;

@Component
public class GDMSpecification {

	public static Specification<Employee> getAllGDMSpec(DropdownGDMDto request) {

		return (root, query, cb) -> {
			final List<Predicate> finalPredicate = new ArrayList<Predicate>();
			finalPredicate.add(cb.and(cb.equal(root.get("isActive"), true)));
			final Join<Employee,EmployeeRole> rolesJoin = root.join("employeeRoles");
			finalPredicate.add(rolesJoin.get("role").get("name").in(Roles.GDM.name()));

			final Join<Employee,Project> devGDMJoin = root.join("projects1", JoinType.LEFT);
			final Join<Employee,Project> qaGDMJoin = root.join("projects2", JoinType.LEFT);

			if((request.getProjectNameList() == null || request.getProjectNameList().isEmpty()) &&
					(request.getAccountNameList() == null || request.getAccountNameList().isEmpty()) &&
					(request.getVerticalIdList() == null || request.getVerticalIdList().isEmpty())) {
				if (request.getAllProjects() != null) {
					final List<Predicate> gdmForProjectPredicate = new ArrayList<Predicate>();
					gdmForProjectPredicate.add(devGDMJoin.get("projectId").in(
							request.getAllProjects().stream().map(Project::getProjectId).collect(Collectors.toList())));
					gdmForProjectPredicate.add(qaGDMJoin.get("projectId").in(
							request.getAllProjects().stream().map(Project::getProjectId).collect(Collectors.toList())));

					finalPredicate.add(cb.or(gdmForProjectPredicate.toArray(new Predicate[gdmForProjectPredicate.size()])));
				}

			}

			if(request.getProjectNameList() != null && !request.getProjectNameList().isEmpty()) {

				final List<Predicate> gdmForProjectPredicate = new ArrayList<Predicate>();
				gdmForProjectPredicate.add(devGDMJoin.get("projectId").in(
						request.getProjectNameList().stream().map(Long::parseLong).collect(Collectors.toList())));
				gdmForProjectPredicate.add(qaGDMJoin.get("projectId").in(
						request.getProjectNameList().stream().map(Long::parseLong).collect(Collectors.toList())));

				finalPredicate.add(cb.or(gdmForProjectPredicate.toArray(new Predicate[gdmForProjectPredicate.size()])));

			}

			if(request.getAccountNameList() != null && !request.getAccountNameList().isEmpty()) {

				final List<Predicate> gdmForAccountPredicate = new ArrayList<Predicate>();
				gdmForAccountPredicate.add(devGDMJoin.get("account").get("accountId").in(
						request.getAccountNameList().stream().map(Long::parseLong).collect(Collectors.toList())));
				gdmForAccountPredicate.add(qaGDMJoin.get("account").get("accountId").in(
						request.getAccountNameList().stream().map(Long::parseLong).collect(Collectors.toList())));

				finalPredicate.add(cb.or(gdmForAccountPredicate.toArray(new Predicate[gdmForAccountPredicate.size()])));

			}

			if(request.getVerticalIdList() != null && !request.getVerticalIdList().isEmpty()) {


				final List<Predicate> gdmForVerticalPredicate = new ArrayList<Predicate>();
				gdmForVerticalPredicate.add(devGDMJoin.get("account").get("vertical").get("verticalId").in(
						request.getVerticalIdList().stream().map(Long::parseLong).collect(Collectors.toList())));
				gdmForVerticalPredicate.add(qaGDMJoin.get("account").get("vertical").get("verticalId").in(
						request.getVerticalIdList().stream().map(Long::parseLong).collect(Collectors.toList())));

				finalPredicate.add(cb.or(gdmForVerticalPredicate.toArray(new Predicate[gdmForVerticalPredicate.size()])));

			}

			query.distinct(true);
			return cb.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));
		};
	}

}
