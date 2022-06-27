package com.empconn.repositories.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.empconn.dto.allocation.EarmarkSearchDto;
import com.empconn.persistence.entities.Earmark;
import com.empconn.persistence.entities.EarmarkSalesforceIdentifier;
import com.empconn.persistence.entities.EmployeeSkill;
import com.empconn.persistence.entities.SalesforceIdentifier;

public class EarmarkSpecification implements Specification<Earmark> {

	private final EarmarkSearchDto filter;

	public EarmarkSpecification(EarmarkSearchDto filter) {
		super();
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<Earmark> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		final List<Predicate> finalPredicate = new ArrayList<Predicate>();
		finalPredicate.add(cb.equal(root.get("allocation").get("employee").get("isActive"), true));
		finalPredicate.add(cb.equal(root.get("allocation").get("project").get("account").get("name"), "Bench"));
		finalPredicate.add(cb.equal(root.get("isActive"), true));

		if (!filter.getIsOpp()) {

			finalPredicate.add(cb.isNotNull(root.get("project")));

			if (filter.getAccountNameList() != null && !filter.getAccountNameList().isEmpty()) {
				finalPredicate.add(root.get("project").get("account").get("name").in(filter.getAccountNameList()));
			}

			if (filter.getProjOppNameList() != null && !filter.getProjOppNameList().isEmpty()) {
				finalPredicate.add(root.get("project").get("name").in(filter.getProjOppNameList()));
			}

			if (filter.getVerticalIdList() != null && !filter.getVerticalIdList().isEmpty()) {
				finalPredicate.add(root.get("project").get("account").get("vertical").get("verticalId")
						.in(filter.getVerticalIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
			}

			if (filter.getSalesforceIdList() != null && !filter.getSalesforceIdList().isEmpty()) {
				final Join<Earmark, SalesforceIdentifier> projectSfJoin = root.join("project")
						.join("salesforceIdentifiers");

				final Predicate projectSfPredicate = cb.in(projectSfJoin.get("value"))
						.value(filter.getSalesforceIdList());
				finalPredicate.add(projectSfPredicate);
//				final Join<Earmark, EarmarkSalesforceIdentifier> earmarkSfJoin = root
//						.join("earmarkSalesforceIdentifiers");
//				final Predicate sfPredicate = cb.or(projectSfJoin.get("value").in(filter.getSalesforceIdList()),
//						earmarkSfJoin.get("value").in(filter.getSalesforceIdList()));
//				finalPredicate.add(sfPredicate);
			}

			if (filter.getGdmIdList() != null && !filter.getGdmIdList().isEmpty()) {
				finalPredicate.add(root.get("project").get("employee1").get("employeeId")
						.in(filter.getGdmIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
			}

		} else {

			finalPredicate.add(cb.isNotNull(root.get("opportunity")));

			if (filter.getAccountNameList() != null && !filter.getAccountNameList().isEmpty()) {
				finalPredicate.add(root.get("opportunity").get("accountName").in(filter.getAccountNameList()));
			}

			if (filter.getProjOppNameList() != null && !filter.getProjOppNameList().isEmpty()) {
				finalPredicate.add(root.get("opportunity").get("name").in(filter.getProjOppNameList()));
			}

			if (filter.getVerticalIdList() != null && !filter.getVerticalIdList().isEmpty()) {
				finalPredicate.add(root.get("opportunity").get("vertical").get("verticalId")
						.in(filter.getVerticalIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
			}

			if (filter.getSalesforceIdList() != null && !filter.getSalesforceIdList().isEmpty()) {
				final Join<Earmark, EarmarkSalesforceIdentifier> earmarkSfJoin = root
						.join("earmarkSalesforceIdentifiers");
				finalPredicate.add(earmarkSfJoin.get("value").in(filter.getSalesforceIdList()));
			}

			if (filter.getGdmIdList() != null && !filter.getGdmIdList().isEmpty()) {
				finalPredicate.add(root.get("opportunity").get("employee1").get("employeeId")
						.in(filter.getGdmIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
			}
		}
		if (filter.getResourceIdList() != null && !filter.getResourceIdList().isEmpty()) {
			finalPredicate.add(root.get("allocation").get("employee").get("employeeId").in(filter.getResourceIdList()));
		}

		if (filter.getPrimarySkillIdList() != null && !filter.getPrimarySkillIdList().isEmpty()) {
			final Join<Earmark, EmployeeSkill> empSkillJoin = root.join("allocation").join("employee")
					.join("employeeSkills");
			final Predicate primarySkillPredicate = cb
					.in(empSkillJoin.get("secondarySkill").get("primarySkill").get("primarySkillId"))
					.value(filter.getPrimarySkillIdList().stream().map(Integer::parseInt).collect(Collectors.toList()));
			finalPredicate.add(primarySkillPredicate);
		}

		if (filter.getSecondarySkillIdList() != null && !filter.getSecondarySkillIdList().isEmpty()) {
			final Join<Earmark, EmployeeSkill> empSkillJoin = root.join("allocation").join("employee")
					.join("employeeSkills");
			final Predicate secondarySkillPredicate = cb.in(empSkillJoin.get("secondarySkill").get("secondarySkillId"))
					.value(filter.getSecondarySkillIdList().stream().map(Integer::parseInt)
							.collect(Collectors.toList()));
			finalPredicate.add(secondarySkillPredicate);
		}

		if (filter.getManagerIdList() != null && !filter.getManagerIdList().isEmpty()) {
			finalPredicate.add(root.get("employee2").get("employeeId")
					.in(filter.getManagerIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getBillable() != null) {
			finalPredicate.add(cb.equal(root.get("billable"), filter.getBillable()));
		}

		query.distinct(true);
		final Predicate and = cb.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));
		return and;
	}

}
