
\set ON_ERROR_STOP 1
begin;
	
    \copy cranium.vertical FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/vertical V0.1.csv' delimiter ',' csv header
    \copy cranium.primary_skill FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/primary_skill V0.1.csv' delimiter ',' csv header
    \copy cranium.secondary_skill FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/secondary_skill V0.1.csv' delimiter ',' csv header
    \copy cranium.checklist FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/checklist v0.1.csv' delimiter ',' csv header
    \copy cranium.location FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/location V0.1.csv' delimiter ',' csv header
    \copy cranium.title FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/title V0.1.csv' delimiter ',' csv header
    \copy cranium.horizontal FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/horizontal V0.1.csv' delimiter ',' csv header
    \copy cranium.project_sub_category FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/project_sub_category V0.1.csv' delimiter ',' csv header
    \copy cranium.role FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/role V0.1.csv' delimiter ',' csv header
    \copy cranium.account FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/account V0.1.csv' delimiter ',' csv header
    \copy cranium.business_unit FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/business_unit V0.1.csv' delimiter ',' csv header
    \copy cranium.division FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/division V0.1.csv' delimiter ',' csv header
    \copy cranium.department FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/department V0.1.csv' delimiter ',' csv header
    \copy cranium.work_group FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/work_group.csv' delimiter ',' csv header
    \copy cranium.allocation_status FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/allocation_status v0.1.csv' delimiter ',' csv header
    \copy cranium.employee FROM 'D:/Cranium/Data Migration/SeedAndTransactionalData/employee V0.1.csv' delimiter ',' csv header
    
commit;
