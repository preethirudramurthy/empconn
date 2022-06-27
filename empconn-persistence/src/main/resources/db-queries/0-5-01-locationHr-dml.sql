
-- Insert scripts

--Bangalore Location HR
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='Bangalore'), (select employee_id from cranium.employee where email='padmavathi.v@tavant.com'), 100, now(), 100, now(), true);
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='Bangalore'), (select employee_id from cranium.employee where email='gina.xavier@tavant.com'), 100, now(), 100, now(), true);
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='Bangalore'), (select employee_id from cranium.employee where email='priyadarshini.v@tavant.com'), 100, now(), 100, now(), true);
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='Bangalore'), (select employee_id from cranium.employee where email='akhila.bhat@tavant.com'), 100, now(), 100, now(), true);

--Noida Location HR
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='Noida'), (select employee_id from cranium.employee where email='somya.batra@tavant.com'), 100, now(), 100, now(), true);

--Hyderabad Location HR
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='Hyderabad'), (select employee_id from cranium.employee where email='somya.batra@tavant.com'), 100, now(), 100, now(), true);
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='Hyderabad'), (select employee_id from cranium.employee where email='raghavendra.m@tavant.com'), 100, now(), 100, now(), true);

--Kolkata Location HR
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='Kolkata'), (select employee_id from cranium.employee where email='somya.batra@tavant.com'), 100, now(), 100, now(), true);
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='Kolkata'), (select employee_id from cranium.employee where email='raghavendra.m@tavant.com'), 100, now(), 100, now(), true);

--US Location HR
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='United States'), (select employee_id from cranium.employee where email='papiya.mitra@tavant.com'), 100, now(), 100, now(), true);
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='United States'), (select employee_id from cranium.employee where email='anamaria.maireanu@tavant.com'), 100, now(), 100, now(), true);

--UK Location HR
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='United Kingdom'), (select employee_id from cranium.employee where email='gina.xavier@tavant.com'), 100, now(), 100, now(), true);

--Australia Location HR
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='Australia'), (select employee_id from cranium.employee where email='padmavathi.v@tavant.com'), 100, now(), 100, now(), true);

--Japan Location HR
INSERT INTO cranium.location_hr
(location_hr_id, location_id, employee_id, created_by, modified_on, modified_by, created_on, is_active)
VALUES((select nextval('cranium.location_hr_id_sequence')), (select location_id from cranium."location" where name='Japan'), (select employee_id from cranium.employee where email='gina.xavier@tavant.com'), 100, now(), 100, now(), true);