package com.empconn.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.empconn.enums.ProjectStatus;
import com.empconn.repositories.EarmarkSalesforceIdentifierRepository;
import com.empconn.repositories.NdRequestSalesforceIdentifierRepository;
import com.empconn.repositories.SalesforceIdentifierRepository;

@Component
public class SalesforceIdentifierService {

	@Autowired
	private SalesforceIdentifierRepository salesforceIdentifierRepository;

	@Autowired
	private EarmarkSalesforceIdentifierRepository earmarkSalesforceIdentifierRepository;

	@Autowired
	NdRequestSalesforceIdentifierRepository ndRequestSalesforceIdentifierRepository;

	List<String> notAllowedProjectStatus = Arrays.asList(ProjectStatus.GDM_REJECTED.name(),
			ProjectStatus.PMO_REJECTED.name());

	public boolean isValidSalesforceIdForProject(String sfId, Long projectId) {
		final Long projectSfCount = salesforceIdentifierRepository
				.countByValueAndProjectProjectIdNotAndProjectCurrentStatusNotInAndIsActiveIsTrue(sfId, projectId,
						notAllowedProjectStatus);
		if (projectSfCount > 0)
			return false;

		final Long earmarkProjectSfCount = earmarkSalesforceIdentifierRepository
				.countByValueAndEarmarkProjectProjectIdNotAndIsActiveIsTrue(sfId, projectId);
		if (earmarkProjectSfCount > 0)
			return false;

		final Long earmarkOppurtunitySfCount = earmarkSalesforceIdentifierRepository
				.countByValueAndEarmarkOpportunityNotNullAndEarmarkIsActiveTrueAndIsActiveIsTrue(sfId);
		if (earmarkOppurtunitySfCount > 0)
			return false;

		final Long ndRequestProjectSfCount = ndRequestSalesforceIdentifierRepository
				.countByValueAndNdRequestProjectProjectIdNotAndIsActiveIsTrue(sfId, projectId);
		return (ndRequestProjectSfCount <= 0);
	}

	public boolean isValidSalesforceId(String sfId) {
		final Long projectSfCount = salesforceIdentifierRepository
				.countByValueAndProjectCurrentStatusNotInAndIsActiveIsTrue(sfId, notAllowedProjectStatus);
		if (projectSfCount > 0)
			return false;

		final Long earmarkProjectOpportunitySfCount = earmarkSalesforceIdentifierRepository
				.countByValueAndIsActiveIsTrue(sfId);
		if (earmarkProjectOpportunitySfCount > 0)
			return false;

		final Long ndRequestProjectSfCount = ndRequestSalesforceIdentifierRepository
				.countByValueAndIsActiveIsTrue(sfId);
		return (ndRequestProjectSfCount <= 0);
	}

	public boolean isValidSalesforceIdForOppurtunity(String sfId, Long opportunityId) {
		final Long projectSfCount = salesforceIdentifierRepository
				.countByValueAndProjectCurrentStatusNotInAndIsActiveIsTrue(sfId, notAllowedProjectStatus);
		if (projectSfCount > 0)
			return false;

		final Long earmarkProjectSfCount = earmarkSalesforceIdentifierRepository
				.countByValueAndEarmarkProjectNotNullAndIsActiveIsTrue(sfId);
		if (earmarkProjectSfCount > 0)
			return false;

		final Long earmarkOppurtunitySfCount = earmarkSalesforceIdentifierRepository
				.countByValueAndEarmarkOpportunityOpportunityIdNotAndIsActiveIsTrue(sfId, opportunityId);
		if (earmarkOppurtunitySfCount > 0)
			return false;

		final Long ndRequestProjectSfCount = ndRequestSalesforceIdentifierRepository
				.countByValueAndIsActiveIsTrue(sfId);
		return (ndRequestProjectSfCount <=0);
	}

}
