DROP TABLE cranium.gdm_comment;

DROP SEQUENCE IF EXISTS cranium.gdm_comment_id_sequence;

CREATE TABLE cranium.project_comment (
    project_comment_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    value VARCHAR NOT NULL,
	status VARCHAR NOT NULL,
    created_on TIMESTAMP NOT NULL,
    created_by BIGINT NOT NULL,
    modified_on TIMESTAMP,
    modified_by BIGINT,
    is_active BOOLEAN DEFAULT true NOT NULL,
    CONSTRAINT project_comment_pk PRIMARY KEY (project_comment_id)
);

ALTER TABLE cranium.project_comment ADD CONSTRAINT project_project_comment_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

create SEQUENCE cranium.project_comment_id_sequence INCREMENT 1 START 100;
