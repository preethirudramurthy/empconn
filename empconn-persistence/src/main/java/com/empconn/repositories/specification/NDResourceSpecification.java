package com.empconn.repositories.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.empconn.dto.ResourceDto;
import com.empconn.persistence.entities.Employee;

public class NDResourceSpecification implements Specification<Employee> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5172555354500088939L;
	private final ResourceDto filter;

	public NDResourceSpecification(ResourceDto filter) {
		super();
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		root.fetch("department");
		final List<Predicate> finalPredicate = new ArrayList<>();
		finalPredicate.add(cb.equal(root.get("isActive"), true));

		if (filter.getTitleIdList() != null && !filter.getTitleIdList().isEmpty()) {
			finalPredicate.add(root.get("title").get("titleId")
					.in(filter.getTitleIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getOrgLocationIdList() != null && !filter.getOrgLocationIdList().isEmpty()) {
			finalPredicate.add(root.get("location").get("locationId")
					.in(filter.getOrgLocationIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		if (filter.getResourceIdList() != null && !filter.getResourceIdList().isEmpty()) {
			finalPredicate.add(root.get("employeeId")
					.in(filter.getResourceIdList().stream().map(Long::parseLong).collect(Collectors.toList())));
		}

		if (filter.getDepartmentIdList() != null && !filter.getDepartmentIdList().isEmpty()) {
			finalPredicate.add(root.get("department").get("departmentId")
					.in(filter.getDepartmentIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}

		query.distinct(true);
		query.orderBy(cb.asc(root.get("firstName")), cb.asc(root.get("lastName")));
		return cb.and(finalPredicate.toArray(new Predicate[finalPredicate.size()]));

	}

}
