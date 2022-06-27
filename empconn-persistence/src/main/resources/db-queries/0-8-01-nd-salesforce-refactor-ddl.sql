CREATE TABLE cranium.nd_request_salesforce_identifier (
	nd_request_salesforce_identifier_id BIGINT NOT NULL,
	nd_request_id BIGINT NOT NULL,
	value VARCHAR NOT NULL,
	created_on TIMESTAMP NOT NULL,
	created_by BIGINT NOT NULL,
	modified_on TIMESTAMP,
	modified_by BIGINT,
	is_active BOOLEAN NOT NULL,
	CONSTRAINT nd_request_salesforce_identifier_pk PRIMARY KEY (nd_request_salesforce_identifier_id)
);

ALTER TABLE cranium.nd_request_salesforce_identifier ADD CONSTRAINT nd_request_salesforce_identifier_nd_fk
FOREIGN KEY (nd_request_id)
REFERENCES cranium.nd_request (nd_request_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE SEQUENCE cranium.nd_request_salesforce_identifier_id_sequence INCREMENT 1 START 100;