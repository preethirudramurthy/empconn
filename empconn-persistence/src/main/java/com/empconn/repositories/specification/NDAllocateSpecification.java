package com.empconn.repositories.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.empconn.dto.earmark.NdRequestListForAllocationDto;
import com.empconn.persistence.entities.NdRequest;

public class NDAllocateSpecification implements Specification<NdRequest> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6808492665103989695L;
	private static final String EMPLOYEE1 = "employee1";
	private final NdRequestListForAllocationDto filter;

	public NDAllocateSpecification(NdRequestListForAllocationDto filter) {
		super();
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<NdRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		root.fetch(EMPLOYEE1);
		final List<Predicate> finalPredicate = new ArrayList<>();
		finalPredicate.add(cb.equal(root.get("isActive"), true));
		finalPredicate.add(cb.equal(root.get(EMPLOYEE1).get("isActive"), true));

		if (filter.getTitleIdList() != null && !filter.getTitleIdList().isEmpty()) {
			finalPredicate.add(root.get(EMPLOYEE1).get("title").get("titleId")
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

		if (filter.getLocationIdList() != null && !filter.getLocationIdList().isEmpty()) {
			finalPredicate.add(root.get(EMPLOYEE1).get("location").get("locationId")
					.in(filter.getLocationIdList().stream().map(Integer::parseInt).collect(Collectors.toList())));
		}
		query.distinct(true);
		return cb.and(finalPredicate.toArray(new Predicate[0]));

	}



}
