CREATE TABLE cranium.location_hr (
	location_hr_id SMALLINT NOT NULL,
	location_id SMALLINT NOT NULL,
	employee_id BIGINT NOT NULL,
	created_by BIGINT NULL,
	modified_on TIMESTAMP,
	modified_by BIGINT,
	created_on TIMESTAMP,
	is_active BOOLEAN NOT NULL DEFAULT true,
	CONSTRAINT location_hr_pk PRIMARY KEY (location_hr_id)
);


CREATE SEQUENCE cranium.location_hr_id_sequence INCREMENT 1 START 100;


-- cranium.location_hr foreign keys

ALTER TABLE cranium.location_hr ADD CONSTRAINT location_hr_fk 
FOREIGN KEY (employee_id) 
REFERENCES cranium.employee(employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;



ALTER TABLE cranium.location_hr ADD CONSTRAINT location_hr_fk_1 
FOREIGN KEY (location_id) 
REFERENCES cranium.location(location_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

