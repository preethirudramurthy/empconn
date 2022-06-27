alter table cranium.employee drop CONSTRAINT primary_allocation_fk;
update cranium.employee set primary_allocation_id=null;
alter table cranium.earmark drop constraint allocation_earmark_fk;
ALTER TABLE cranium.earmark
    ALTER COLUMN allocation_id DROP NOT NULL;
update cranium.earmark set allocation_id = null;
truncate table cranium.allocation;
drop table cranium.allocation;	
CREATE TABLE cranium.allocation
(
    allocation_id bigint NOT NULL,
    project_id bigint NOT NULL,
    employee_id bigint NOT NULL,
    project_location_id bigint NOT NULL,
    work_group_id smallint NOT NULL,
    reporting_manager_id bigint NOT NULL,
    allocation_manager_id bigint NOT NULL,
    allocation_status_id bigint,
	release_date timestamp without time zone,
    is_billable boolean NOT NULL,
    created_on timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_on timestamp without time zone,
    modified_by bigint,
    is_active boolean NOT NULL,
    CONSTRAINT allocation_new_pk PRIMARY KEY (allocation_id),
    CONSTRAINT allocation_status_allocation_fk FOREIGN KEY (allocation_status_id)
        REFERENCES cranium.allocation_status (allocation_status_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT employee_allocation_fk FOREIGN KEY (employee_id)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT employee_allocation_fk1 FOREIGN KEY (reporting_manager_id)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT employee_allocation_fk2 FOREIGN KEY (allocation_manager_id)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT project_allocation_fk FOREIGN KEY (project_id)
        REFERENCES cranium.project (project_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT project_location_allocation_fk FOREIGN KEY (project_location_id)
        REFERENCES cranium.project_location (project_location_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT work_group_allocation_fk FOREIGN KEY (work_group_id)
        REFERENCES cranium.work_group (work_group_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE cranium.allocation_detail
(
    allocation_detail_id bigint NOT NULL,
    allocation_id bigint NOT NULL,
    deallocated_by_id bigint,
    allocated_percentage smallint NOT NULL,
    start_date date NOT NULL,
    deallocated_on timestamp without time zone,
    created_on timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_on timestamp without time zone,
    modified_by bigint,
    is_active boolean NOT NULL,
    CONSTRAINT allocation_detail_pk PRIMARY KEY (allocation_detail_id),
    CONSTRAINT allocation_id_allocation_fk FOREIGN KEY (allocation_id)
        REFERENCES cranium.allocation (allocation_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT employee_allocation_fk3 FOREIGN KEY (deallocated_by_id)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
    
);


ALTER TABLE cranium.earmark ADD CONSTRAINT allocation_earmark_fk
FOREIGN KEY (allocation_id)
REFERENCES cranium.allocation (allocation_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee ADD CONSTRAINT primary_allocation_fk
FOREIGN KEY (primary_allocation_id)
REFERENCES cranium.allocation (allocation_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

create SEQUENCE cranium.allocation_detail_id_sequence INCREMENT 1 START 100;

truncate table cranium.earmark;
ALTER TABLE cranium.earmark
    alter COLUMN allocation_id set  NOT NULL;

	-- Table: cranium.allocation_feedback

-- DROP TABLE cranium.allocation_feedback;

CREATE TABLE cranium.allocation_feedback
(
    allocation_feedback_id bigint NOT NULL,
    allocation_id bigint NOT NULL,
    soft_skill_rating smallint NOT NULL,
    soft_skill_feedback character varying COLLATE pg_catalog."default" NOT NULL,
    tech_rating smallint NOT NULL,
    tech_feedback character varying COLLATE pg_catalog."default" NOT NULL,
    created_on timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_on timestamp without time zone,
    modified_by bigint,
    is_active boolean NOT NULL,
    CONSTRAINT allocation_feedback_id_pk PRIMARY KEY (allocation_feedback_id),
    CONSTRAINT allocation_id_feedback_fk FOREIGN KEY (allocation_id)
        REFERENCES cranium.allocation (allocation_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE cranium.allocation_feedback_id_sequence
    INCREMENT 1
    START 100;

