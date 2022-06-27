
INSERT INTO cranium.project_location(
	project_location_id, project_id, location_id, dev_manager_id, created_on, created_by, is_active)
	 (select nextval('cranium.project_location_id_sequence'), 
			(select project_id from cranium.project where project.name='Central Bench'), (select location_id from cranium."location" where name='Bangalore' ), 
			(select employee_id from cranium.employee where login_id='man-1'), now(), 1, '1');

INSERT INTO cranium.project_location(
	project_location_id, project_id, location_id, dev_manager_id, created_on, created_by, is_active)
	 (select nextval('cranium.project_location_id_sequence'), 
			(select project_id from cranium.project where project.name='Central Bench'), (select location_id from cranium."location" where name='Noida' ), 
			(select employee_id from cranium.employee where login_id='man-1'), now(), 1, '1');

INSERT INTO cranium.project_location(
	project_location_id, project_id, location_id, dev_manager_id, created_on, created_by, is_active)
	 (select nextval('cranium.project_location_id_sequence'), 
			(select project_id from cranium.project where project.name='Central Bench'), (select location_id from cranium."location" where name='Hyderabad' ), 
			(select employee_id from cranium.employee where login_id='man-1'), now(), 1, '1');
			
INSERT INTO cranium.project_location(
	project_location_id, project_id, location_id, dev_manager_id, created_on, created_by, is_active)
	 (select nextval('cranium.project_location_id_sequence'), 
			(select project_id from cranium.project where project.name='Central Bench'), (select location_id from cranium."location" where name='Kolkata' ), 
			(select employee_id from cranium.employee where login_id='man-1'), now(), 1, '1');

INSERT INTO cranium.project_location(
	project_location_id, project_id, location_id, dev_manager_id, created_on, created_by, is_active)
	 (select nextval('cranium.project_location_id_sequence'), 
			(select project_id from cranium.project where project.name='Central Bench'), (select location_id from cranium."location" where name='United States' ), 
			(select employee_id from cranium.employee where login_id='man-1'), now(), 1, '1');

INSERT INTO cranium.project_location(
	project_location_id, project_id, location_id, dev_manager_id, created_on, created_by, is_active)
	 (select nextval('cranium.project_location_id_sequence'), 
			(select project_id from cranium.project where project.name='Central Bench'), (select location_id from cranium."location" where name='United Kingdom' ), 
			(select employee_id from cranium.employee where login_id='man-1'), now(), 1, '1');
			
INSERT INTO cranium.project_location(
	project_location_id, project_id, location_id, dev_manager_id, created_on, created_by, is_active)
	 (select nextval('cranium.project_location_id_sequence'), 
			(select project_id from cranium.project where project.name='Central Bench'), (select location_id from cranium."location" where name='Australia' ), 
			(select employee_id from cranium.employee where login_id='man-1'), now(), 1, '1');			
			
INSERT INTO cranium.project_location(
	project_location_id, project_id, location_id, dev_manager_id, created_on, created_by, is_active)
	 (select nextval('cranium.project_location_id_sequence'), 
			(select project_id from cranium.project where project.name='Central Bench'), (select location_id from cranium."location" where name='Japan' ), 
			(select employee_id from cranium.employee where login_id='man-1'), now(), 1, '1');			
			
			