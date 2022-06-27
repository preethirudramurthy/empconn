drop table cranium.allocation_hours;

CREATE TABLE cranium.allocation_hours
(
    allocation_hours_id bigint NOT NULL,
    allocation_id bigint NOT NULL,
    year smallint NOT NULL,
    month varchar NOT NULL,
	billing_hours numeric NOT NULL,
	billing_hours_rounded smallint NOT NULL,
	max_hours smallint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_on timestamp without time zone,
    modified_by bigint,
    is_active boolean NOT NULL,
    CONSTRAINT allocation_hours_id_pk PRIMARY KEY (allocation_hours_id),
    CONSTRAINT allocation_id_fk FOREIGN KEY (allocation_id)
        REFERENCES cranium.allocation (allocation_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
    
);
