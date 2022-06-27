CREATE TABLE cranium.nd_request
(
    nd_request_id bigint NOT NULL,
    employee_id bigint NOT NULL,
    project_id BIGINT NOT NULL,
    percentage smallint NOT NULL,
    start_date date,
    release_date date,
    is_billable boolean,
    reporting_manager_id bigint NOT NULL,
    is_active boolean NOT NULL,
    created_on timestamp without time zone,
    created_by bigint NOT NULL,
    modified_on timestamp without time zone,
    modified_by bigint,
    CONSTRAINT "nd-request_pkey" PRIMARY KEY (nd_request_id),
    CONSTRAINT employee_nd_request_fk1 FOREIGN KEY (employee_id)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT employee_nd_request_fk2 FOREIGN KEY (reporting_manager_id)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT employee_nd_request_fk3 FOREIGN KEY (created_by)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT employee_nd_request_fk4 FOREIGN KEY (modified_by)
        REFERENCES cranium.employee (employee_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT project_nd_request_fk FOREIGN KEY (project_id)
        REFERENCES cranium.project (project_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

ALTER TABLE cranium.allocation
    ALTER COLUMN allocation_status_id DROP NOT NULL;
