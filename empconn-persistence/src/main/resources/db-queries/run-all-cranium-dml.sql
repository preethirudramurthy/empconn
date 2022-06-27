
\set ON_ERROR_STOP 1
begin;
	
    \i 0-0-15-master-system-user.sql
    \i 0-3-02-master-allocation-status.sql
    \i 0-3-03-master-project-deliverybench.sql
    \i 0-3-04-master-project-nondeliverybench.sql
    
commit;
