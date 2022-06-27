ALTER TABLE cranium.employee_roles DROP COLUMN modified_by;
ALTER TABLE cranium.employee_roles ADD COLUMN modified_by BIGINT;
	
ALTER TABLE cranium.earmark DROP COLUMN modified_by;
ALTER TABLE cranium.earmark ADD COLUMN modified_by BIGINT;
	
ALTER TABLE cranium.email_configuration DROP COLUMN is_active;
ALTER TABLE cranium.email_configuration ADD COLUMN is_active BOOLEAN DEFAULT true NOT NULL;
	
