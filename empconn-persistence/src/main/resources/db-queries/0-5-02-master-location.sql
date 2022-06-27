
INSERT INTO cranium.location(
	location_id, name, hierarchy, created_on, modified_on, created_by, modified_by, is_active)
	VALUES (nextval('cranium.location_id_sequence'),'Global', (select max(hierarchy) from cranium.location), now(), null, 1, 1, '1');
	
	