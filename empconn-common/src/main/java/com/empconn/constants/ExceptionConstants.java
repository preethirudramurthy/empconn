package com.empconn.constants;

public interface ExceptionConstants {

	public static final String ACTIVE_ACCOUNT_EXISTS = "ACTIVE_AccountExists";
	public static final String TEMP_ACCOUNT_EXISTS = "TEMP_AccountExists";
	public static final String INACTIVE_ACCOUNT_EXISTS = "INACTIVE_AccountExists";
	public static final String NO_RESOURCE_ALLOCATED = "NoResourceAllocated";
	public static final String ALL_SUBPROJECTS_INACTIVE = "AllSubprojectsInactive";
	public static final String RESOURCE_NOT_ALREADY_EARMARKED_FOR_THE_PROJECT = "ResourceNotAlreadyEarmarkedForTheProject";
	public static final String NOT_ALREADY_REQUESTED_FOR_SAME_PROJECT = "NotAlreadyRequestedForSameProject";
	public static final String SALESFORCE_ID_MUST_NOT_ALREADY_EXIST_PROJ = "SalesforceIdMustNotAlreadyExistProj";
	public static final String SALESFORCE_ID_MUST_NOT_ALREADY_EXIST_OPPORTUNITY = "SalesforceIdMustNotAlreadyExistOpportunity";
	public static final String SALESFORCE_ID_NOT_EXISTS_IN_OTHER_PROJECT = "SalesforceIdNotExistsInOtherProject";
	public static final String NOT_ALLOCATED_TO_PROJECT = "NotAllocatedToProject";
	public static final String ALLOCATION_ID_EXISTS = "AllocationIdExists";
	public static final String SHOULD_NOT_BE_ALREADY_REQUESTED = "ShouldNotBeAlreadyRequested";
	public static final String PROJECT_STATUS_REVIEWED = "ProjectStatusReviewed";
	public static final String START_DATE_EARLIER_THAN_RELEASE_DATE = "StartDateEarlierThanReleaseDate";
	public static final String DEFAULT_ERROR = "DefaultError";
	public static final String SERVICE_AUTH_FAILURE = "ServiceAuthFailure";
	public static final String LDAP_NAMING_FAILURE = "LdapNamingFailure";
	public static final String LDAP_RETRY_FAILURE = "LdapRetryFailure";
	public static final String SKILL_NOT_AVAILABLE = "Primary/Secondary skill is not available";
	public static final String NEW_VERTICAL_ALREADY_AVAILABLE = "New Vertical already available";
	public static final String OLD_VERTICAL_NOT_AVAILABLE = "Old Vertical not available";
	public static final String VERTICAL_NOT_AVAILABLE = "Vertical not available";
	public static final String NEW_HORIZONTAL_ALREADY_AVAILABLE = "New Horizontal already available";
	public static final String OLD_HORIZONTAL_NOT_AVAILABLE = "Old Horizontal not available";
	public static final String HORIZONTAL_NOT_AVAILABLE = "Horizontal not available";

}
