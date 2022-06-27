
\set ON_ERROR_STOP 1
begin;

    \i 0-0-00-dummy-users.sql
    \i 0-0-01-resource-loading.sql
    \i 0-0-02-resource-primary-manager-allocation.sql
    \i 0-0-03-dummy-nd-request.sql
    \i 0-3-01-master-project-deliverybench.sql
    \i 0-3-02-master-project-nondeliverybench.sql
    \i 0-3-03-resource-allocation-to-central-bench.sql
    \i 0-3-04-email-configuration.sql
    \i 0-4-01-email-configuration.sql
    \i 0-4-02-bench-projects.sql   

commit;
