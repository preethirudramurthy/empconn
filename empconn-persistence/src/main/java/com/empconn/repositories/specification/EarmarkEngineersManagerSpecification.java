package com.empconn.repositories.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.empconn.dto.earmark.EarmarkEngineersManagerReqDto;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.EarmarkSalesforceIdentifier;
import com.empconn.persistence.entities.Employee;
import com.empconn.persistence.entities.EmployeeRole;
import com.empconn.persistence.entities.SalesforceIdentifier;

public class EarmarkEngineersManagerSpecification implements Specification<Earmark> {

	private final EarmarkEngineersManagerReqDto filter;
	private final Long loginEmpId;

	public EarmarkEngineersManagerSpecification(EarmarkEngineersManagerReqDto filter, Long loginEmpId) {
		super();
		this.filter = filter;
		this.loginEmpId = loginEmpId;
	}

	@Override
	public Predicate toPredicate(Root<Earmark> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		final List<Predicate> finalPredicate = new ArrayList<Predicate>();
		finalPredicate.add(cb.equal(root.get("isActive"), true));

		final Predicate managerPredicate = cb.equal(root.get("employee2").get("employeeId"), loginEmpId);
		final Predicate createdByPredicate = cb.equal(root.get("createdBy"), loginEmpId);
		final Predicate notCreatedByPredicate = cb.notEqual(root.get("createdBy"), loginEmpId);

		if (filter.getIsOpp() != null && filter.getIsOpp()) {
			if (filter.getEarmarkedByMe() != null && filter.getEarmarkedByMe() == true) {
				finalPredicate.add(cb.and(cb.isNotNull(root.get("opportunity")), cb.isNull(root.get("project"))));
				final Predicate earmarkByMePredicate = cb.and(managerPredicate, createdByPredicate);
				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate opportunityIdPredicate = cb.in(root.get("opportunity").get("opportunityId")).value(
							filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList()));
					finalPredicate.add(cb.and(opportunityIdPredicate, earmarkByMePredicate));
				} else
					finalPredicate.add(earmarkByMePredicate);
			}
			if (filter.getEarmarkedByGdm() != null && filter.getEarmarkedByGdm() == true) {
				final Subquery<Long> subquery = query.subquery(Long.class);
				final Root<Earmark> sqEmp = subquery.correlate(root);
				final Root<Employee> subqueryRoot = subquery.from(Employee.class);
				final Join<Employee, EmployeeRole> subqueryRootRoleJoin = subqueryRoot.join("employeeRoles");
				final Predicate gdmCreatedPredicate = cb.and(
						cb.equal(subqueryRoot.get("employeeId"), sqEmp.get("createdBy")),
						cb.equal(subqueryRootRoleJoin.get("role").get("name"), "GDM"));
				final Predicate isGdmCreatedPredicate = cb.ge(
						subquery.where(gdmCreatedPredicate).select(cb.countDistinct(subqueryRootRoleJoin.get("role"))),
						1);
				final Predicate isNotManagerAndGdmCreatedPredicate = cb.and(notCreatedByPredicate,
						isGdmCreatedPredicate);

				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate opportunityIdPredicate = cb.in(root.get("opportunity").get("opportunityId")).value(
							filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList()));
					finalPredicate.add(cb.and(opportunityIdPredicate,
							cb.and(managerPredicate, isNotManagerAndGdmCreatedPredicate)));
				} else
					finalPredicate.add(cb.and(managerPredicate, isNotManagerAndGdmCreatedPredicate));
			}
			if (filter.getEarmarkedByRmg() != null && filter.getEarmarkedByRmg() == true) {
				final Subquery<Long> subquery = query.subquery(Long.class);
				final Root<Earmark> sqEmp = subquery.correlate(root);
				final Root<Employee> subqueryRoot = subquery.from(Employee.class);
				final Join<Employee, EmployeeRole> subqueryRootRoleJoin = subqueryRoot.join("employeeRoles");
				final Predicate rmgCreatedPredicate = cb.and(
						cb.equal(subqueryRoot.get("employeeId"), sqEmp.get("createdBy")),
						cb.equal(subqueryRootRoleJoin.get("role").get("name"), "RMG"));
				final Predicate isERmgCreatedPredicate = cb.ge(
						subquery.where(rmgCreatedPredicate).select(cb.countDistinct(subqueryRootRoleJoin.get("role"))),
						1);
				final Predicate isNotManagerAndRmgCreatedPredicate = cb.and(notCreatedByPredicate,
						isERmgCreatedPredicate);

				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate opportunityIdPredicate = cb.in(root.get("opportunity").get("opportunityId")).value(
							filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList()));
					finalPredicate.add(cb.and(opportunityIdPredicate,
							cb.and(managerPredicate, isNotManagerAndRmgCreatedPredicate)));
				} else
					finalPredicate.add(cb.and(managerPredicate, isNotManagerAndRmgCreatedPredicate));
			}
			if (!CollectionUtils.isEmpty(filter.getSalesforceIdList())) {
				final Subquery<Long> subqueryEsf = query.subquery(Long.class);
				final Root<EarmarkSalesforceIdentifier> subqueryRootEsf = subqueryEsf
						.from(EarmarkSalesforceIdentifier.class);

				subqueryEsf.select(subqueryRootEsf.get("earmark").get("opportunity").get("opportunityId"))
						.distinct(true).where(cb.in(subqueryRootEsf.get("value")).value(filter.getSalesforceIdList()));

				finalPredicate.add(cb.in(root.get("opportunity").get("opportunityId")).value(subqueryEsf));
			}
		}

		else {
			finalPredicate.add(cb.and(cb.isNotNull(root.get("project")), cb.isNull(root.get("opportunity"))));
			if (filter.getEarmarkedByMe() != null && filter.getEarmarkedByMe() == true) {
				final Predicate earmarkByMePredicate = cb.and(managerPredicate, createdByPredicate);

				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate projectIdPredicate = cb.in(root.get("project").get("projectId")).value(
							filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList()));
					finalPredicate.add(cb.and(earmarkByMePredicate, projectIdPredicate));
				} else
					finalPredicate.add(earmarkByMePredicate);
			}
			if (filter.getEarmarkedByGdm() != null && filter.getEarmarkedByGdm() == true) {
				final Subquery<Long> subquery = query.subquery(Long.class);
				final Root<Earmark> sqEmp = subquery.correlate(root);
				final Root<Employee> subqueryRoot = subquery.from(Employee.class);
				final Join<Employee, EmployeeRole> subqueryRootRoleJoin = subqueryRoot.join("employeeRoles");
				final Predicate gdmCreatedPredicate = cb.and(
						cb.equal(subqueryRoot.get("employeeId"), sqEmp.get("createdBy")),
						cb.equal(subqueryRootRoleJoin.get("role").get("name"), "GDM"));
				final Predicate isGdmCreatedPredicate = cb.ge(
						subquery.where(gdmCreatedPredicate).select(cb.countDistinct(subqueryRootRoleJoin.get("role"))),
						1);
				final Predicate isNotManagerAndGdmCreatedPredicate = cb.and(notCreatedByPredicate,
						isGdmCreatedPredicate);

				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate projectIdPredicate = cb.in(root.get("project").get("projectId")).value(
							filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList()));
					finalPredicate.add(
							cb.and(cb.and(managerPredicate, isNotManagerAndGdmCreatedPredicate), projectIdPredicate));
				} else
					finalPredicate.add(cb.and(managerPredicate, isNotManagerAndGdmCreatedPredicate));
			}
			if (filter.getEarmarkedByRmg() != null && filter.getEarmarkedByRmg() == true) {
				final Subquery<Long> subquery = query.subquery(Long.class);
				final Root<Earmark> sqEmp = subquery.correlate(root);
				final Root<Employee> subqueryRoot = subquery.from(Employee.class);
				final Join<Employee, EmployeeRole> subqueryRootRoleJoin = subqueryRoot.join("employeeRoles");
				final Predicate rmgCreatedPredicate = cb.and(
						cb.equal(subqueryRoot.get("employeeId"), sqEmp.get("createdBy")),
						cb.equal(subqueryRootRoleJoin.get("role").get("name"), "RMG"));
				final Predicate isERmgCreatedPredicate = cb.ge(
						subquery.where(rmgCreatedPredicate).select(cb.countDistinct(subqueryRootRoleJoin.get("role"))),
						1);
				final Predicate isNotManagerAndRmgCreatedPredicate = cb.and(notCreatedByPredicate,
						isERmgCreatedPredicate);

				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate projectIdPredicate = cb.in(root.get("project").get("projectId")).value(
							filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList()));
					finalPredicate.add(
							cb.and(cb.and(managerPredicate, isNotManagerAndRmgCreatedPredicate), projectIdPredicate));
				} else
					finalPredicate.add(cb.and(managerPredicate, isNotManagerAndRmgCreatedPredicate));
			}
			if (!CollectionUtils.isEmpty(filter.getSalesforceIdList())) {
				final Subquery<Long> subquerySf = query.subquery(Long.class);
				final Subquery<Long> subqueryEsf = query.subquery(Long.class);
				final Root<SalesforceIdentifier> subqueryRootSf = subquerySf.from(SalesforceIdentifier.class);
				final Root<EarmarkSalesforceIdentifier> subqueryRootEsf = subqueryEsf
						.from(EarmarkSalesforceIdentifier.class);
				subquerySf.select(subqueryRootSf.get("project").get("projectId")).distinct(true)
						.where(cb.in(subqueryRootSf.get("value")).value(filter.getSalesforceIdList()));

				subqueryEsf.select(subqueryRootEsf.get("earmark").get("project").get("projectId")).distinct(true)
						.where(cb.in(subqueryRootEsf.get("value")).value(filter.getSalesforceIdList()));

				finalPredicate.add(cb.or(cb.in(root.get("project").get("projectId")).value(subquerySf),
						cb.in(root.get("project").get("projectId")).value(subqueryEsf)));
			}
		}

		query.distinct(true);
		final Predicate and = cb.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));
		return and;
	}

}
