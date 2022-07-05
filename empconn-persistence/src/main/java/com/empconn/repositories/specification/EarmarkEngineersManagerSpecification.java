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

	/**
	 * 
	 */
	private static final long serialVersionUID = -3848005676056590933L;
	private static final String VALUE = "value";
	private static final String PROJECT_ID = "projectId";
	private static final String PROJECT = "project";
	private static final String OPPORTUNITY_ID = "opportunityId";
	private static final String OPPORTUNITY = "opportunity";
	private static final String EMPLOYEE_ROLES = "employeeRoles";
	private static final String EMPLOYEE_ID = "employeeId";
	private static final String CREATED_BY = "createdBy";
	private final EarmarkEngineersManagerReqDto filter;
	private final Long loginEmpId;

	public EarmarkEngineersManagerSpecification(EarmarkEngineersManagerReqDto filter, Long loginEmpId) {
		super();
		this.filter = filter;
		this.loginEmpId = loginEmpId;
	}

	@Override
	public Predicate toPredicate(Root<Earmark> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		final List<Predicate> finalPredicate = new ArrayList<>();
		finalPredicate.add(cb.equal(root.get("isActive"), true));

		final Predicate managerPredicate = cb.equal(root.get("employee2").get(EMPLOYEE_ID), loginEmpId);
		final Predicate createdByPredicate = cb.equal(root.get(CREATED_BY), loginEmpId);
		final Predicate notCreatedByPredicate = cb.notEqual(root.get(CREATED_BY), loginEmpId);

		if (filter.getIsOpp()) {
			if (filter.getEarmarkedByMe()) {
				finalPredicate.add(cb.and(cb.isNotNull(root.get(OPPORTUNITY)), cb.isNull(root.get(PROJECT))));
				final Predicate earmarkByMePredicate = cb.and(managerPredicate, createdByPredicate);
				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate opportunityIdPredicate = cb.in(root.get(OPPORTUNITY).get(OPPORTUNITY_ID)).value(
							filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList()));
					finalPredicate.add(cb.and(opportunityIdPredicate, earmarkByMePredicate));
				} else
					finalPredicate.add(earmarkByMePredicate);
			}
			if (filter.getEarmarkedByGdm()) {
				final Subquery<Long> subquery = query.subquery(Long.class);
				final Root<Earmark> sqEmp = subquery.correlate(root);
				final Root<Employee> subqueryRoot = subquery.from(Employee.class);
				final Join<Employee, EmployeeRole> subqueryRootRoleJoin = subqueryRoot.join(EMPLOYEE_ROLES);
				final Predicate gdmCreatedPredicate = cb.and(
						cb.equal(subqueryRoot.get(EMPLOYEE_ID), sqEmp.get(CREATED_BY)),
						cb.equal(subqueryRootRoleJoin.get("role").get("name"), "GDM"));
				final Predicate isGdmCreatedPredicate = cb.ge(
						subquery.where(gdmCreatedPredicate).select(cb.countDistinct(subqueryRootRoleJoin.get("role"))),
						1);
				final Predicate isNotManagerAndGdmCreatedPredicate = cb.and(notCreatedByPredicate,
						isGdmCreatedPredicate);

				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate opportunityIdPredicate = cb.in(root.get(OPPORTUNITY).get(OPPORTUNITY_ID)).value(
							filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList()));
					finalPredicate.add(cb.and(opportunityIdPredicate,
							cb.and(managerPredicate, isNotManagerAndGdmCreatedPredicate)));
				} else
					finalPredicate.add(cb.and(managerPredicate, isNotManagerAndGdmCreatedPredicate));
			}
			if (filter.getEarmarkedByRmg()) {
				final Subquery<Long> subquery = query.subquery(Long.class);
				final Root<Earmark> sqEmp = subquery.correlate(root);
				final Root<Employee> subqueryRoot = subquery.from(Employee.class);
				final Join<Employee, EmployeeRole> subqueryRootRoleJoin = subqueryRoot.join(EMPLOYEE_ROLES);
				final Predicate rmgCreatedPredicate = cb.and(
						cb.equal(subqueryRoot.get(EMPLOYEE_ID), sqEmp.get(CREATED_BY)),
						cb.equal(subqueryRootRoleJoin.get("role").get("name"), "RMG"));
				final Predicate isERmgCreatedPredicate = cb.ge(
						subquery.where(rmgCreatedPredicate).select(cb.countDistinct(subqueryRootRoleJoin.get("role"))),
						1);
				final Predicate isNotManagerAndRmgCreatedPredicate = cb.and(notCreatedByPredicate,
						isERmgCreatedPredicate);

				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate opportunityIdPredicate = cb.in(root.get(OPPORTUNITY).get(OPPORTUNITY_ID)).value(
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

				subqueryEsf.select(subqueryRootEsf.get("earmark").get(OPPORTUNITY).get(OPPORTUNITY_ID))
						.distinct(true).where(cb.in(subqueryRootEsf.get(VALUE)).value(filter.getSalesforceIdList()));

				finalPredicate.add(cb.in(root.get(OPPORTUNITY).get(OPPORTUNITY_ID)).value(subqueryEsf));
			}
		}

		else {
			finalPredicate.add(cb.and(cb.isNotNull(root.get(PROJECT)), cb.isNull(root.get(OPPORTUNITY))));
			if (filter.getEarmarkedByMe()) {
				final Predicate earmarkByMePredicate = cb.and(managerPredicate, createdByPredicate);

				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate projectIdPredicate = cb.in(root.get(PROJECT).get(PROJECT_ID)).value(
							filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList()));
					finalPredicate.add(cb.and(earmarkByMePredicate, projectIdPredicate));
				} else
					finalPredicate.add(earmarkByMePredicate);
			}
			if (filter.getEarmarkedByGdm()) {
				final Subquery<Long> subquery = query.subquery(Long.class);
				final Root<Earmark> sqEmp = subquery.correlate(root);
				final Root<Employee> subqueryRoot = subquery.from(Employee.class);
				final Join<Employee, EmployeeRole> subqueryRootRoleJoin = subqueryRoot.join(EMPLOYEE_ROLES);
				final Predicate gdmCreatedPredicate = cb.and(
						cb.equal(subqueryRoot.get(EMPLOYEE_ID), sqEmp.get(CREATED_BY)),
						cb.equal(subqueryRootRoleJoin.get("role").get("name"), "GDM"));
				final Predicate isGdmCreatedPredicate = cb.ge(
						subquery.where(gdmCreatedPredicate).select(cb.countDistinct(subqueryRootRoleJoin.get("role"))),
						1);
				final Predicate isNotManagerAndGdmCreatedPredicate = cb.and(notCreatedByPredicate,
						isGdmCreatedPredicate);

				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate projectIdPredicate = cb.in(root.get(PROJECT).get(PROJECT_ID)).value(
							filter.getProjectIdList().stream().map(Long::parseLong).collect(Collectors.toList()));
					finalPredicate.add(
							cb.and(cb.and(managerPredicate, isNotManagerAndGdmCreatedPredicate), projectIdPredicate));
				} else
					finalPredicate.add(cb.and(managerPredicate, isNotManagerAndGdmCreatedPredicate));
			}
			if (filter.getEarmarkedByRmg()) {
				final Subquery<Long> subquery = query.subquery(Long.class);
				final Root<Earmark> sqEmp = subquery.correlate(root);
				final Root<Employee> subqueryRoot = subquery.from(Employee.class);
				final Join<Employee, EmployeeRole> subqueryRootRoleJoin = subqueryRoot.join(EMPLOYEE_ROLES);
				final Predicate rmgCreatedPredicate = cb.and(
						cb.equal(subqueryRoot.get(EMPLOYEE_ID), sqEmp.get(CREATED_BY)),
						cb.equal(subqueryRootRoleJoin.get("role").get("name"), "RMG"));
				final Predicate isERmgCreatedPredicate = cb.ge(
						subquery.where(rmgCreatedPredicate).select(cb.countDistinct(subqueryRootRoleJoin.get("role"))),
						1);
				final Predicate isNotManagerAndRmgCreatedPredicate = cb.and(notCreatedByPredicate,
						isERmgCreatedPredicate);

				if (!CollectionUtils.isEmpty(filter.getProjectIdList())) {
					final Predicate projectIdPredicate = cb.in(root.get(PROJECT).get(PROJECT_ID)).value(
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
				subquerySf.select(subqueryRootSf.get(PROJECT).get(PROJECT_ID)).distinct(true)
						.where(cb.in(subqueryRootSf.get(VALUE)).value(filter.getSalesforceIdList()));

				subqueryEsf.select(subqueryRootEsf.get("earmark").get(PROJECT).get(PROJECT_ID)).distinct(true)
						.where(cb.in(subqueryRootEsf.get(VALUE)).value(filter.getSalesforceIdList()));

				finalPredicate.add(cb.or(cb.in(root.get(PROJECT).get(PROJECT_ID)).value(subquerySf),
						cb.in(root.get(PROJECT).get(PROJECT_ID)).value(subqueryEsf)));
			}
		}

		query.distinct(true);
		return cb.and(finalPredicate.toArray(new Predicate[0]));
	}

}
