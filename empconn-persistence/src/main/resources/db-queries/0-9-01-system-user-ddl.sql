	
CREATE TABLE cranium.system_user
(
    system_user_id INTEGER NOT NULL,
    employee_id BIGINT NOT NULL,
    password VARCHAR NOT NULL,
    created_on TIMESTAMP NOT NULL,
    created_by BIGINT NOT NULL,
    modified_on TIMESTAMP,
    modified_by BIGINT,
    is_active BOOLEAN NOT NULL,
    CONSTRAINT system_user_pk PRIMARY KEY (system_user_id)
); 

ALTER TABLE cranium.system_user ADD CONSTRAINT system_user_employee_fk
FOREIGN KEY (employee_id)  
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE SEQUENCE cranium.system_user_id_sequence INCREMENT 1 START 100;


