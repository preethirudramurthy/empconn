
Insert into cranium.role values(nextval('cranium.role_id_sequence'), 'SYSTEM_USER', now(), 1, null, null, TRUE);

insert into cranium.employee(employee_id, emp_code, email, first_name, last_name, login_id, date_of_joining, title_id, business_unit_id, division_id, department_id, location_id, is_active, is_manager, created_on, created_by) 
values(nextval('cranium.employee_id_sequence'), 'system1', 'system.successfactor@tavant.com', 'system', 'successfactor', 'system.successfactor', '12/21/2015', (select title_id from cranium.title where name = 'Sr. Business Architect'), (select business_unit_id from cranium.business_unit where name = 'Delivery'), (select division_id from cranium.division where name = 'Delivery'), (select department_id from cranium.department where name = 'Dev'), (select location_id from cranium.location where name = 'Bangalore'), true, false, now(), 1);
insert into cranium.employee(employee_id, emp_code, email, first_name, last_name, login_id, date_of_joining, title_id, business_unit_id, division_id, department_id, location_id, is_active, is_manager, created_on, created_by) 
values(nextval('cranium.employee_id_sequence'), 'system3', 'system.pulse@tavant.com', 'system', 'pulse', 'system.pulse', '12/21/2015', (select title_id from cranium.title where name = 'Sr. Business Architect'), (select business_unit_id from cranium.business_unit where name = 'Delivery'), (select division_id from cranium.division where name = 'Delivery'), (select department_id from cranium.department where name = 'Dev'), (select location_id from cranium.location where name = 'Bangalore'), true, false, now(), 1);
insert into cranium.employee(employee_id, emp_code, email, first_name, last_name, login_id, date_of_joining, title_id, business_unit_id, division_id, department_id, location_id, is_active, is_manager, created_on, created_by) 
values(nextval('cranium.employee_id_sequence'), 'system5', 'system.fms@tavant.com', 'system', 'fms', 'system.fms', '12/21/2015', (select title_id from cranium.title where name = 'Sr. Business Architect'), (select business_unit_id from cranium.business_unit where name = 'Delivery'), (select division_id from cranium.division where name = 'Delivery'), (select department_id from cranium.department where name = 'Dev'), (select location_id from cranium.location where name = 'Bangalore'), true, false, now(), 1);

INSERT INTO cranium.employee_roles(
	employee_roles_id, employee_id, role_id, created_on, created_by, modified_on, is_active, modified_by)
	VALUES (nextval('cranium.employee_roles_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.successfactor'), (select role_id from cranium.role where name = 'SYSTEM_USER'), now(), 1, null, TRUE, null);
INSERT INTO cranium.employee_roles(
	employee_roles_id, employee_id, role_id, created_on, created_by, modified_on, is_active, modified_by)
	VALUES (nextval('cranium.employee_roles_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.pulse'), (select role_id from cranium.role where name = 'SYSTEM_USER'), now(), 1, null, TRUE, null);
INSERT INTO cranium.employee_roles(
	employee_roles_id, employee_id, role_id, created_on, created_by, modified_on, is_active, modified_by)
	VALUES (nextval('cranium.employee_roles_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.fms'), (select role_id from cranium.role where name = 'SYSTEM_USER'), now(), 1, null, TRUE, null);

/* For Dev and local environment */
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.successfactor'), '$2a$10$DeUU4m1GItEXJkp7QF3l7ec2klpD0eCwBSw7C92GLvJGMmjxOwxra', now(), 101, null, null, TRUE);
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.pulse'), '$2a$10$dMI98BPjaY1FdZh/FWSelukifxmSDE1NWYiNipm/3hArmhIwZFsry', now(), 101, null, null, TRUE);	
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.fms'), '$2a$10$cByjNY/AijhywyKQbAn2Gur6yNVA0IJaxrPASZINfwbZfMl3MXnP6', now(), 101, null, null, TRUE);


/* For QA environment */
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.successfactor'), '$2a$10$bG9XjV6hy88.Mj4CosmDXeczEDblCCCsd3ZmAdFP0vaZMzloQ6DHm', now(), 101, null, null, TRUE);
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.pulse'), '$2a$10$j1NnUlx.otffHWmFD2PNWOw6y2PwQgUo7Vu57/IYwgrl99Vy6i2/G', now(), 101, null, null, TRUE);	
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.fms'), '$2a$10$SfgM9lE/B9EQuxrD5OeWJOTWv5e/7/5KElVuHLwoBW20h4HRywLSS', now(), 101, null, null, TRUE);

/* For Prod environment */
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.successfactor'), '$2a$10$s5L/lrIb4X2d.6dfCi4jJOTB8kPJsi3jYcCp501yEYQ5ZID2iEfUK', now(), 101, null, null, TRUE);
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.pulse'), '$2a$10$proThZ5.uUJrvMHniIPfz.yJdiyJ.rLTMOuqFIN25Usj6L/S7uZym', now(), 101, null, null, TRUE);	
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.fms'), '$2a$10$kR2.GShjB/dzf1qiT4nw.uEfChuno9vzrgLztmOzhUlDhrDVE5.g6', now(), 101, null, null, TRUE);


/* For UAT environment */
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.successfactor'), '$2a$10$PiTJjpKpueHVHkDTQOFv2eLpn82fgl3kpIEl6ByLNyQCgcKNtGQGS', now(), 101, null, null, TRUE);
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.pulse'), '$2a$10$M.swQ63Ic542DJZMbPCF9.5gCCNd/4/3GDbFoWgRlFjD20ZyFm7C2', now(), 101, null, null, TRUE);	
INSERT INTO cranium.system_user(
	system_user_id, employee_id, password, created_on, created_by, modified_on, modified_by, is_active)
	VALUES (nextval('cranium.system_user_id_sequence'), (select employee_id from cranium.employee where login_id = 'system.fms'), '$2a$10$M.57glewXrNXU789g4YR2e9J1oFRuTZJ7ZOPGx6RsiWfxN.dsHTkK', now(), 101, null, null, TRUE);

