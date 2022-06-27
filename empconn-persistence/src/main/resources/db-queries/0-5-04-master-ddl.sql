
ALTER TABLE cranium.employee ADD COLUMN last_working_day DATE;
ALTER TABLE cranium.employee ADD COLUMN middle_name VARCHAR;
ALTER TABLE cranium.employee ADD COLUMN gdm_id BIGINT;


ALTER TABLE cranium.employee ADD CONSTRAINT employee_employee_fk1
FOREIGN KEY (gdm_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
