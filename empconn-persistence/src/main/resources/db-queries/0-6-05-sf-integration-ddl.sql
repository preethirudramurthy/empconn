CREATE TABLE cranium.manager_change
(
    manager_change_id bigint NOT NULL,
    employee_id bigint NOT NULL,
    is_gdm boolean NOT NULL,
    new_manager_id bigint NOT NULL,
    effective_start_date date NOT NULL,
    status character varying NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    is_active boolean NOT NULL,
    CONSTRAINT manager_change_pk PRIMARY KEY (manager_change_id),
    CONSTRAINT employee_manager_change_fk FOREIGN KEY (employee_id)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT employee_manager_change_fk1 FOREIGN KEY (new_manager_id)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE cranium.manager_change_id_sequence INCREMENT 1 START 100;
    
CREATE TABLE cranium.project_change
(
    project_change_id bigint NOT NULL,
    employee_id bigint NOT NULL,
    effective_start_date date NOT NULL,
    project_id bigint NOT NULL,
    account_id integer NOT NULL,
    status character varying NOT NULL,
    created_on timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_on timestamp without time zone,
    modified_by bigint,
    is_active boolean NOT NULL,
    CONSTRAINT project_change_pk PRIMARY KEY (project_change_id),
    CONSTRAINT account_project_change_fk FOREIGN KEY (account_id)
        REFERENCES cranium.account (account_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT employee_project_change_fk FOREIGN KEY (employee_id)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT project_project_change_fk FOREIGN KEY (project_id)
        REFERENCES cranium.project (project_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
   
CREATE SEQUENCE cranium.project_change_id_sequence INCREMENT 1 START 100;
