package com.empconn.constants;

public class ApplicationConstants {

	public static final String EMAIL_TO = "EmailTo";
	public static final String EMAIL_CC = "EmailCc";
	public static final String TESTING_TEAM_MAILS = "testing-team-mails";

	public static final String DISPLAY_ORIGINAL_MAIL_ADDRESSES_IN_MAIL_CONTENT = "displayMailAddresses";
	public static final String TRUE = "T";
	public static final String FALSE = "F";
	public static final String TO_MAIL_ADDRESSES = "toMailList";
	public static final String CC_MAIL_ADDRESSES = "ccMailList";

	public static final String FINAL_CC_MAIL_ADDRESS = "FinalToMailAddress";
	public static final String FINAL_TO_MAIL_ADDRESS = "FinalCcMailAddress";

	public static final String MAIL_MODEL_VALUES = "mailModelValues";
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

	public static final String CONTENT_TEMPLATE = "contentTemplate";
	public static final String ATTACHMENT_TEMPLATE = "attachmentTemplate";

	public static final String CRAN_SESSION = "CranSession";

	public static final String ON_HOLD = "ON-HOLD";
	public static final String ACTIVE = "ACTIVE";
	public static final String INACTIVE = "INACTIVE";

	public static final String PINKLINK = "http://empconnect#";

	public static final int PROJECT_INIT_DEADLINE_BY = 12;

	public static final String DELIVERY_BENCH_PROJECT_NAME = "Central Bench";

	public static final String NON_DELIVERY_BENCH_PROJECT_NAME = "NDBench";

	public static final String INTERNAL_CATEGORY = "Internal";

	public static final String ROLE_GENERAL = "GENERAL";
	public static final String ROLE_MANAGER = "MANAGER";
	public static final String DEFAULT_ROLE = ROLE_GENERAL;

	public static final String LOCATION_GLOBAL = "Global";

	public static final String ALLOCATION_STATUS_PB = "PB";
	public static final String WORK_GROUP_DEV = "DEV";
	public static final String WORK_GROUP_UI = "UI";
	public static final String WORK_GROUP_SUPPORT_1 = "SUPPORT1";
	public static final String WORK_GROUP_SUPPORT_2 = "SUPPORT2";

	public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	public static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd-HH-mm-ss";
	public static final String DATE_FORMAT_SLASH_MM_DD_YYYY = "MM/dd/yyyy";
	public static final String DATE_FORMAT_DD_MMM_YYYY = "dd-MMM-yyyy";

	public static final String QA_WORK_GROUP = "QA";

	public static final String SYSTEM_USER_FIRST_NAME = "SYSTEM";

	public static final String ALLOCATION_STATUS_LONG_LEAVE = "LL";
	public static final String ALLOCATION_STATUS_SABBATICAL = "SBL";
	public static final String ALLOCATION_STATUS_PUREBENCH = "PB";

	public static final String BU_DELIVERY = "Delivery";
	public static final String DIVISION_DELIVERY = "Delivery";
	public static final String DEPT_PMO = "PMO";
	public static final String DEPT_SEPG = "SEPG";
	public static final String SYSTEM = "SYSTEM";
	public static final String USER = "USER";
	public static final String UNEARMARK_USER_COMMENT = "Manually unearmarked";
	public static final String UNEARMARK_EDIT_RELEASE_DATE_PROJECT_COMMENT = "Project Release date changed";
	public static final String UNEARMARK_EDIT_RELEASE_DATE_RESOURCE_COMMENT = "Resource Allocation Release date changed";
	public static final String UNEARMARK_ALLOCATE_EARMARK_COMMENT = "Earmarked resource is allocated";
	public static final String UNEARMARK_ALLOCATE_NOT_AVAILABLE_PERCENTAGE_COMMENT = "Earmark available percentage is not satisfied on Allocating earmarked resource";
	public static final String UNEARMARK_DEALLOCATE_NOT_AVAILABLE_PERCENTAGE_COMMENT = "Earmark available percentage is not satisfied on De-allocation";
	public static final String UNEARMARK_ON_LONG_LEAVE_COMMENT = "Resource go on long leave";
	public static final String UNEARMARK_ON_SABATICAL_LEAVE_COMMENT = "Resource go on sabatical leave";
	public static final String UNEARMARK_ON_EMPLOYEE_UPDATE_COMMENT = "Resource information updated";

	public static final String MAP_PRACTICE = "Vertical";

	public static final String STATUS_PENDING = "Pending";

	public static final String PROFILE_DEV = "dev";
	public static final String PROFILE_QA = "qa";
	public static final String PROFILE_PROD = "prod";

	public static final String ENV_NAME = "environmentName";

	public static final String DEFAULT_SECONDARY_SKILL = "DefaultSecondarySkill";

	public static final String FORECAST_ADD_MONTH = "17";
	public static final String FORECAST_MINUS_MONTH = "1";
	public static final String FORECAST_FIRSTDAY_OF_MONTH = "1";
	public static final String FORECAST_NEXT_MONTH = "1";

	public enum SF_FTP {
		GDM, MANAGER, PROJECT
	}

}
