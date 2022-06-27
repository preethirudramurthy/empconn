CREATE TABLE cranium.allocation_hours
(
    allocation_hours_id bigint NOT NULL,
    allocation_detail_id bigint NOT NULL,
    start_date date NOT NULL,
	end_date date NOT NULL,
	network_days smallint NOT NULL,
	billing_hours numeric NOT NULL,
	billing_hours_rounded smallint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_on timestamp without time zone,
    modified_by bigint,
    is_active boolean NOT NULL,
    CONSTRAINT allocation_hours_id_pk PRIMARY KEY (allocation_hours_id),
    CONSTRAINT allocation_detail_id_fk FOREIGN KEY (allocation_detail_id)
        REFERENCES cranium.allocation_detail (allocation_detail_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
    
);

create SEQUENCE cranium.allocation_hours_id_sequence INCREMENT 1 START 100;