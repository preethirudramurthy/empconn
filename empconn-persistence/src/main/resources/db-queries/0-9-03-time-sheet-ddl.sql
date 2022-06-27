ALTER TABLE cranium.sync_project_allocation
    drop COLUMN allocated_percentage;
	
ALTER TABLE cranium.allocation
    ADD COLUMN timesheet_allocation_id bigint;

ALTER TABLE cranium.allocation
    ADD CONSTRAINT timesheet_allocation_id_fk FOREIGN KEY (timesheet_allocation_id)
    REFERENCES cranium.allocation (allocation_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;
    
CREATE TABLE cranium.timesheet_allocation
(
    timesheet_allocation_id bigint NOT NULL,
    allocation_id bigint NOT NULL,
    allocated_percentage smallint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    is_active boolean NOT NULL,
    CONSTRAINT timesheet_allocation_pk PRIMARY KEY (timesheet_allocation_id)
);

CREATE SEQUENCE cranium.timesheet_allocation_id_sequence INCREMENT 1 START 100;