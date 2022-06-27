
\set ON_ERROR_STOP 1
begin;
	
	\i 0-0-00-base-ddl.sql
	\i 0-0-01-base-ddl-sequence_creation.sql
    \i 0-0-02-master-vertical.sql
    \i 0-0-03-master-primary_skill.sql
    \i 0-0-04-master-secondary_skill.sql
    \i 0-0-05-master-checklist.sql
    \i 0-0-06-master-location.sql
    \i 0-0-07-master-title.sql
    \i 0-0-08-master-horizontal.sql
    \i 0-0-09-master-project-sub-category.sql
    \i 0-0-10-master-role.sql
    \i 0-0-11-master-account.sql
    \i 0-0-12-master-business-unit.sql
    \i 0-0-13-master-division.sql
    \i 0-0-14-master-department.sql
    \i 0-0-15-master-system-user.sql
    \i 0-3-01-master-work-group.sql
    \i 0-3-02-master-allocation-status.sql
    \i 0-3-03-master-project-deliverybench.sql
    \i 0-3-04-master-project-nondeliverybench.sql
    \i 0-3-05-base-ddl.sql
    \i 0-3-06-base-ddl-sequence_creation.sql
    \i 0-4-01-employee-ddl.sql
    \i 0-4-02-refactor-ddl.sql
    \i 0-4-03-refactor-ddl.sql
    \i 0-4-04-refactor-ddl.sql
    \i 0-5-01-locationHr-ddl.sql
    \i 0-5-02-master-location.sql
    \i 0-5-03-master-project-delivery-nd-bench.sql
    \i 0-5-04-master-ddl.sql
    \i 0-5-05-master-role.sql
    \i 0-5-06-map-ddl.sql
    \i 0-6-01-allocation-ddl.sql
    \i 0-6-02-allocation-hours-ddl.sql
    \i 0-6-03-allocation-hours-ddl.sql
    \i 0-6-04-allocation-allocationDetail-ddl.sql
    \i 0-6-05-sf-integration-ddl.sql
    \i 0-6-06-earmark-salesforce-refactor-ddl.sql
    \i 0-7-01-employee_ddl.sql
    \i 0-7-02-dummy_employee_nd_reporting_manager_dml.sql
    \i 0-7-02-employee_nd_reporting_manager_dml.sql
    \i 0-7-03-master-allocation-status.sql
    \i 0-8-01-nd-salesforce-refactor-ddl.sql
    
commit;
