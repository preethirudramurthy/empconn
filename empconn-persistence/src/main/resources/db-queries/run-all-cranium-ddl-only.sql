
\set ON_ERROR_STOP 1
begin;
	
	\i 0-0-00-base-ddl.sql
    \i 0-3-05-base-ddl.sql
    \i 0-3-07-location-hierarchy-ddl.sql
    \i 0-3-09-location-hierarchy-ddl.sql
    \i 0-4-01-employee-ddl.sql
    \i 0-4-02-refactor-ddl.sql
    \i 0-4-03-refactor-ddl.sql
    \i 0-4-04-refactor-ddl.sql
    \i 0-5-01-locationHr-ddl.sql
    \i 0-5-04-master-ddl.sql
    \i 0-5-06-map-ddl.sql
    \i 0-6-01-allocation-ddl.sql
    \i 0-6-02-allocation-hours-ddl.sql
    \i 0-6-03-allocation-hours-ddl.sql
    \i 0-6-04-allocation-allocationDetail-ddl.sql
    \i 0-6-05-sf-integration-ddl.sql
    \i 0-6-06-earmark-salesforce-refactor-ddl.sql
    \i 0-7-01-employee_ddl.sql
    \i 0-8-01-nd-salesforce-refactor-ddl.sql
    \i 0-8-02-horizontal-dropdown-refactor-ddl.sql
    \i 0-8-03-earmark-comment-ddl.sql
    
commit;
