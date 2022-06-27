INSERT INTO cranium.project(
	project_id, account_id, horizontal_id, project_sub_category_id,  
	dev_gdm_id, business_manager_id, name, start_date,  description, created_on, created_by,is_active)
	(select nextval('cranium.project_id_sequence'), (select account_id from cranium.account where category='Internal'), 
			(select horizontal_id from cranium.horizontal where name='App Dev'), 
			(select 
			project_sub_category_id from cranium.project_sub_category where name='App dev'), 
			(select employee_id from cranium.employee where login_id='gdm-1'), 
			(select employee_id from cranium.employee where login_id='gdm-1'), 
			'NDBench', now(), 'Central NON Delivery Bench', 
			now(), 1, '1');
		