
INSERT INTO cranium.project_location(
	project_location_id, project_id, location_id, dev_manager_id, created_on, created_by, is_active)
	 (select nextval('cranium.project_location_id_sequence'), 
			(select project_id from cranium.project where project.name='Central Bench'), (select location_id from cranium."location" where name='Global' ), 
			(select employee_id from cranium.employee where login_id='sneha.satish'), now(), 1, '1');

INSERT INTO cranium.project_location(
	project_location_id, project_id, location_id, dev_manager_id, created_on, created_by, is_active)
	 (select nextval('cranium.project_location_id_sequence'), 
			(select project_id from cranium.project where project.name='NDBench'), (select location_id from cranium."location" where name='Global' ), 
			(select employee_id from cranium.employee where login_id='sneha.satish'), now(), 1, '1');

UPDATE 
	cranium.project 
SET 
	dev_gdm_id = (SELECT employee_id FROM cranium.employee WHERE login_id='kuttanda.thimmaiah'),
	business_manager_id = (SELECT employee_id FROM cranium.employee WHERE login_id='kuttanda.thimmaiah')
WHERE 
	name IN ('Central Bench', 'NDBench')
;