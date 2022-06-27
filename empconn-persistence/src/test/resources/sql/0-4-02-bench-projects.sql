insert into cranium.account(account_id, vertical_id, name, created_on, created_by, status, category, is_active, client_website_link, description, start_date)
values(nextval('cranium.account_id_sequence'), (select vertical_id from cranium.vertical where name = 'Tavant'), 'Bench', now(), 1, 'OPEN', 'Internal',true, 'http://tavant.com', 'Tavant Technologies', '2004-11-21');

update cranium.project set account_id=(select account_id from cranium.account where name='Bench') where name in ('Central Bench','NDBench');

INSERT INTO cranium.project(
	project_id, account_id, horizontal_id, project_sub_category_id,  
	dev_gdm_id, business_manager_id, name, start_date,  description, created_on, created_by,is_active)
	(select nextval('cranium.project_id_sequence'), (select account_id from cranium.account where name='Bench'), 
			(select horizontal_id from cranium.horizontal where name='App Dev'), 
			(select 
			project_sub_category_id from cranium.project_sub_category where name='App dev'), 
			(select employee_id from cranium.employee where login_id='gdm-1'), 
			(select employee_id from cranium.employee where login_id='gdm-1'), 
			'Bench - Fintech', now(), 'Fintech Practice bench', 
			now(), 1, '1');
INSERT INTO cranium.project(
	project_id, account_id, horizontal_id, project_sub_category_id,  
	dev_gdm_id, business_manager_id, name, start_date,  description, created_on, created_by,is_active)
	(select nextval('cranium.project_id_sequence'), (select account_id from cranium.account where name='Bench'), 
			(select horizontal_id from cranium.horizontal where name='App Dev'), 
			(select 
			project_sub_category_id from cranium.project_sub_category where name='App dev'), 
			(select employee_id from cranium.employee where login_id='gdm-1'), 
			(select employee_id from cranium.employee where login_id='gdm-1'), 
			'Bench - Manufacturing', now(), 'Manufacturing Practice bench', 
			now(), 1, '1');
update cranium.project set current_status='PMO_APPROVED' where account_id in 
(select account_id from cranium.account where name='Bench');