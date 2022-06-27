alter table cranium.employee drop column primary_manager_id;

ALTER TABLE cranium.employee ADD COLUMN nd_reporting_manager_id bigint;
	
ALTER TABLE cranium.employee ADD CONSTRAINT nd_reporting_manager_id_fk
FOREIGN KEY (nd_reporting_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
