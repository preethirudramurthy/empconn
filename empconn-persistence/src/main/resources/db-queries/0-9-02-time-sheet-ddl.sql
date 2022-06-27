CREATE TABLE cranium.sync_account
(
    sync_account_id bigint NOT NULL,
    account_id bigint NOT NULL,
    status character varying NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    is_active boolean NOT NULL,
    CONSTRAINT sync_account_pk PRIMARY KEY (sync_account_id),
    CONSTRAINT account_sync_account_fk FOREIGN KEY (account_id)
        REFERENCES cranium.account (account_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE cranium.sync_account_id_sequence INCREMENT 1 START 100;

CREATE TABLE cranium.sync_project
(
    sync_project_id bigint NOT NULL,
    project_id bigint NOT NULL,
    status character varying NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    is_active boolean NOT NULL,
    CONSTRAINT sync_project_pk PRIMARY KEY (sync_project_id),
    CONSTRAINT project_sync_project_fk FOREIGN KEY (project_id)
        REFERENCES cranium.project (project_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
   
CREATE SEQUENCE cranium.sync_project_id_sequence INCREMENT 1 START 100;

CREATE TABLE cranium.sync_project_allocation
(
    sync_project_allocation_id bigint NOT NULL,
    allocation_id bigint NOT NULL,
    date_of_movement date,
    allocated_percentage smallint NOT NULL,
    status character varying NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    is_active boolean NOT NULL,
    CONSTRAINT sync_project_allocation_pk PRIMARY KEY (sync_project_allocation_id),
    CONSTRAINT project_sync_project_allocation_fk FOREIGN KEY (allocation_id)
        REFERENCES cranium.allocation (allocation_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE cranium.sync_project_allocation_id_sequence INCREMENT 1 START 100;
CREATE TABLE cranium.sync_project_allocation_hours
(
    sync_project_allocation_hours_id bigint NOT NULL,
    allocation_id bigint NOT NULL,
    status character varying NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    is_active boolean NOT NULL,
    CONSTRAINT sync_project_allocation_hours_pk PRIMARY KEY (sync_project_allocation_hours_id),
    CONSTRAINT project_sync_project_allocation_hours_fk FOREIGN KEY (allocation_id)
        REFERENCES cranium.allocation (allocation_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE cranium.sync_project_allocation_hours_id_sequence INCREMENT 1 START 100;

CREATE TABLE cranium.sync_project_manager
(
    sync_project_manager_id bigint NOT NULL,
    project_id bigint NOT NULL,
    project_location_id bigint NOT NULL,
    work_group_id smallint NOT NULL,
    manager_id bigint NOT NULL,
    status character varying NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    is_active boolean NOT NULL,
    CONSTRAINT sync_project_manager_pk PRIMARY KEY (sync_project_manager_id),
    CONSTRAINT employee_sync_project_manager_fk FOREIGN KEY (manager_id)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT project_location_sync_project_manager_fk FOREIGN KEY (project_location_id)
        REFERENCES cranium.project_location (project_location_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT project_sync_project_manager_fk FOREIGN KEY (project_id)
        REFERENCES cranium.project (project_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT work_group_sync_project_manager_fk FOREIGN KEY (work_group_id)
        REFERENCES cranium.work_group (work_group_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE SEQUENCE cranium.sync_project_manager_id_sequence INCREMENT 1 START 100;

CREATE TABLE cranium.sync_employee
(
    sync_employee_id bigint NOT NULL,
    employee_id bigint NOT NULL,
    status character varying NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    is_active boolean NOT NULL,
    CONSTRAINT sync_employee_pk PRIMARY KEY (sync_employee_id),
    CONSTRAINT employee_sync_employee_fk FOREIGN KEY (employee_id)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
   
CREATE SEQUENCE cranium.sync_employee_id_sequence INCREMENT 1 START 100;
