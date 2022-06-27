DROP TABLE cranium.salesforce_identifier_opportunity;
DROP SEQUENCE IF EXISTS cranium.salesforce_identifier_opportunity_id_sequence;

CREATE TABLE cranium.earmark_salesforce_identifier (
	earmark_salesforce_identifier_id BIGINT NOT NULL,
	earmark_id BIGINT NOT NULL,
	value VARCHAR NOT NULL,
	created_on TIMESTAMP NOT NULL,
	created_by BIGINT NOT NULL,
	modified_on TIMESTAMP,
	modified_by BIGINT,
	is_active BOOLEAN NOT NULL,
	CONSTRAINT earmark_salesforce_identifier_pk PRIMARY KEY (earmark_salesforce_identifier_id)
);

ALTER TABLE cranium.earmark_salesforce_identifier ADD CONSTRAINT earmark_salesforce_identifier_earmark_fk
FOREIGN KEY (earmark_id)
REFERENCES cranium.earmark (earmark_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE SEQUENCE cranium.earmark_salesforce_identifier_id_sequence INCREMENT 1 START 100;