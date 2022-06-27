CREATE TABLE cranium.allocation_status (
                allocation_status_id BIGINT NOT NULL,
                status VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT allocation_status_pk PRIMARY KEY (allocation_status_id)
);


CREATE TABLE cranium.work_group (
                work_group_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT work_group_pk PRIMARY KEY (work_group_id)
);


CREATE TABLE cranium.role (
                role_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT role_pk PRIMARY KEY (role_id)
);


CREATE TABLE cranium.vertical (
                vertical_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT vertical_pk PRIMARY KEY (vertical_id)
);


CREATE TABLE cranium.location (
                location_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT location_pk PRIMARY KEY (location_id)
);


CREATE TABLE cranium.account (
                account_id INTEGER NOT NULL,
                vertical_id SMALLINT NOT NULL,
                client_website_link VARCHAR,
                description VARCHAR,
                name VARCHAR NOT NULL,
                start_date DATE,
                end_date DATE,
                status VARCHAR NOT NULL,
                category VARCHAR NOT NULL,
                account_tok_link VARCHAR,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT account_pk PRIMARY KEY (account_id)
);


CREATE TABLE cranium.client_location (
                client_location_id BIGINT NOT NULL,
                account_id INTEGER NOT NULL,
                latitude NUMERIC(15,13) NOT NULL,
                location VARCHAR NOT NULL,
                longitude NUMERIC(16,13) NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT client_location_pk PRIMARY KEY (client_location_id)
);


CREATE TABLE cranium.title (
                title_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT title_pk PRIMARY KEY (title_id)
);


CREATE TABLE cranium.employee (
                employee_id BIGINT NOT NULL,
                title_id SMALLINT NOT NULL,
                location_id SMALLINT NOT NULL,
				reporting_manager_id BIGINT NOT NULL,
                first_name VARCHAR NOT NULL,
                last_name VARCHAR NOT NULL,
                emp_code VARCHAR NOT NULL,
                email VARCHAR NOT NULL,
                login_id VARCHAR NOT NULL,
                date_of_joining DATE NOT NULL,
                is_manager BOOLEAN NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT employee_pk PRIMARY KEY (employee_id)
);


CREATE TABLE cranium.employee_roles (
                employee_roles_id BIGINT NOT NULL,
                employee_id BIGINT NOT NULL,
                role_id SMALLINT NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by VARCHAR,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT employee_roles_pk PRIMARY KEY (employee_roles_id)
);


CREATE TABLE cranium.horizontal (
                horizontal_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT horizontal_pk PRIMARY KEY (horizontal_id)
);


CREATE TABLE cranium.checklist (
                checklist_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT checklist_pk PRIMARY KEY (checklist_id)
);


CREATE TABLE cranium.project_sub_category (
                project_sub_category_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT project_sub_category_pk PRIMARY KEY (project_sub_category_id)
);


CREATE TABLE cranium.project (
                project_id BIGINT NOT NULL,
                account_id INTEGER NOT NULL,
                horizontal_id SMALLINT,
                project_sub_category_id SMALLINT,
                parent_project_id BIGINT,
                dev_gdm_id BIGINT,
                qe_gdm_id BIGINT,
                business_manager_id BIGINT,
                name VARCHAR NOT NULL,
                start_date DATE,
                end_date DATE,
                description VARCHAR,
                technology VARCHAR,
                operating_system VARCHAR,
                database VARCHAR,
                is_sub_project BOOLEAN,
                project_kickoff_is_required BOOLEAN,
                send_notification_to_pin_group BOOLEAN,
                current_status VARCHAR,
                current_comment VARCHAR,
                project_tok_link VARCHAR,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT project_pk PRIMARY KEY (project_id)
);


CREATE TABLE cranium.earmark (
                earmark_id BIGINT NOT NULL,
                employee_id BIGINT NOT NULL,
                manager_id BIGINT NOT NULL,
                project_id BIGINT,
                opportunity_id BIGINT,
                billable BOOLEAN NOT NULL,
                is_client_interview_needed BOOLEAN NOT NULL,
                percentage SMALLINT NOT NULL,
                start_date DATE NOT NULL,
                end_date DATE NOT NULL,
                created_on DATE NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on DATE,
                modified_by DATE,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT earmark_pk PRIMARY KEY (earmark_id)
);


CREATE TABLE cranium.salesforce_identifier (
                salesforce_identifier_id BIGINT NOT NULL,
                project_id BIGINT NOT NULL,
                value VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT salesforce_identifier_pk PRIMARY KEY (salesforce_identifier_id)
);


CREATE TABLE cranium.project_checklist (
                project_checklist_id BIGINT NOT NULL,
                checklist_id SMALLINT NOT NULL,
                project_id BIGINT NOT NULL,
                is_selected BOOLEAN NOT NULL,
                comment VARCHAR,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT project_checklist_pk PRIMARY KEY (project_checklist_id)
);


CREATE TABLE cranium.project_review (
                project_review_id BIGINT NOT NULL,
                project_id BIGINT NOT NULL,
                status VARCHAR NOT NULL,
                comment VARCHAR,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT project_review_pk PRIMARY KEY (project_review_id)
);


CREATE TABLE cranium.project_location (
                project_location_id BIGINT NOT NULL,
                project_id BIGINT NOT NULL,
                location_id SMALLINT NOT NULL,
                dev_manager_id BIGINT,
                qe_manager_id BIGINT,
                ui_manager_id BIGINT,
                manager1_id BIGINT,
                manager2_id BIGINT,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT project_location_pk PRIMARY KEY (project_location_id)
);


CREATE TABLE cranium.allocation (
                allocation_id BIGINT NOT NULL,
                project_id BIGINT NOT NULL,
                employee_id BIGINT NOT NULL,
                project_location_id BIGINT NOT NULL,
                work_group_id SMALLINT NOT NULL,
                reporting_manager_id BIGINT NOT NULL,
                allocation_manager_id BIGINT NOT NULL,
                allocation_status_id BIGINT NOT NULL,
                deallocated_by_id BIGINT,
                allocated_percentage SMALLINT NOT NULL,
                start_date DATE NOT NULL,
                release_date TIMESTAMP NOT NULL,
                deallocated_on TIMESTAMP,
                created_on DATE NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on DATE,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT allocation_pk PRIMARY KEY (allocation_id)
);


CREATE TABLE cranium.primary_skill (
                primary_skill_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT primary_skill_pk PRIMARY KEY (primary_skill_id)
);


CREATE TABLE cranium.secondary_skill (
                secondary_skill_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                primary_skill_id SMALLINT NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT secondary_skill_pk PRIMARY KEY (secondary_skill_id)
);


CREATE TABLE cranium.employee_skills (
                employee_skills_id BIGINT NOT NULL,
                employee_id BIGINT NOT NULL,
                secondary_skill_id SMALLINT NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT employee_skills_pk PRIMARY KEY (employee_skills_id)
);


CREATE TABLE cranium.project_resources (
                project_resources_id BIGINT NOT NULL,
                project_location_id BIGINT NOT NULL,
                title_id SMALLINT NOT NULL,
                number_of_resources SMALLINT NOT NULL,
                start_date DATE,
                end_date DATE,
                allocation_percentage SMALLINT,
                is_billable BOOLEAN NOT NULL,
                primary_skill_id SMALLINT NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT project_resources_pk PRIMARY KEY (project_resources_id)
);


CREATE TABLE cranium.project_resources_secondary_skill (
                project_resources_secondary_skill_id BIGINT NOT NULL,
                project_resources_id BIGINT NOT NULL,
                secondary_skill_id SMALLINT NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT project_resources_secondary_skill_pk PRIMARY KEY (project_resources_secondary_skill_id)
);


CREATE TABLE cranium.contact (
                contact_id BIGINT NOT NULL,
                client_location_id BIGINT NOT NULL,
                name VARCHAR NOT NULL,
                phone_number VARCHAR NOT NULL,
                email VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT contact_pk PRIMARY KEY (contact_id)
);


ALTER TABLE cranium.allocation ADD CONSTRAINT allocation_status_allocation_fk
FOREIGN KEY (allocation_status_id)
REFERENCES cranium.allocation_status (allocation_status_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.allocation ADD CONSTRAINT work_group_allocation_fk
FOREIGN KEY (work_group_id)
REFERENCES cranium.work_group (work_group_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee_roles ADD CONSTRAINT role_employee_roles_fk
FOREIGN KEY (role_id)
REFERENCES cranium.role (role_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.account ADD CONSTRAINT vertical_account_fk
FOREIGN KEY (vertical_id)
REFERENCES cranium.vertical (vertical_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.opportunity ADD CONSTRAINT vertical_opportunity_fk
FOREIGN KEY (vertical_id)
REFERENCES cranium.vertical (vertical_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT location_project_location_fk
FOREIGN KEY (location_id)
REFERENCES cranium.location (location_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee ADD CONSTRAINT location_employee_fk
FOREIGN KEY (location_id)
REFERENCES cranium.location (location_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT account_project_fk
FOREIGN KEY (account_id)
REFERENCES cranium.account (account_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.client_location ADD CONSTRAINT account_client_location_fk
FOREIGN KEY (account_id)
REFERENCES cranium.account (account_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.opportunity ADD CONSTRAINT account_opportunity_fk
FOREIGN KEY (account_id)
REFERENCES cranium.account (account_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.contact ADD CONSTRAINT client_location_contact_fk
FOREIGN KEY (client_location_id)
REFERENCES cranium.client_location (client_location_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_resources ADD CONSTRAINT title_project_resources_fk
FOREIGN KEY (title_id)
REFERENCES cranium.title (title_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee ADD CONSTRAINT title_employee_fk
FOREIGN KEY (title_id)
REFERENCES cranium.title (title_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT employee_project_location_fk
FOREIGN KEY (dev_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT employee_project_location_fk1
FOREIGN KEY (qe_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT employee_project_location_fk2
FOREIGN KEY (ui_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT employee_project_location_fk3
FOREIGN KEY (manager1_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT employee_project_location_fk4
FOREIGN KEY (manager2_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT employee_project_fk
FOREIGN KEY (dev_gdm_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT employee_project_fk1
FOREIGN KEY (qe_gdm_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT employee_project_fk2
FOREIGN KEY (business_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee ADD CONSTRAINT employee_employee_fk
FOREIGN KEY (primary_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee_roles ADD CONSTRAINT employee_employee_roles_fk
FOREIGN KEY (employee_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.earmark ADD CONSTRAINT employee_earmark_fk
FOREIGN KEY (employee_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.earmark ADD CONSTRAINT employee_earmark_fk1
FOREIGN KEY (manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.opportunity ADD CONSTRAINT employee_opportunity_fk
FOREIGN KEY (dev_gdm)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.opportunity ADD CONSTRAINT employee_opportunity_fk1
FOREIGN KEY (qa_gdm)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.allocation ADD CONSTRAINT employee_allocation_fk
FOREIGN KEY (employee_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.allocation ADD CONSTRAINT employee_allocation_fk1
FOREIGN KEY (reporting_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.allocation ADD CONSTRAINT employee_allocation_fk2
FOREIGN KEY (allocation_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee_skills ADD CONSTRAINT employee_employee_skills_fk
FOREIGN KEY (employee_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.allocation ADD CONSTRAINT employee_allocation_fk3
FOREIGN KEY (deallocated_by_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.earmark ADD CONSTRAINT opportunity_earmark_fk
FOREIGN KEY (opportunity_id)
REFERENCES cranium.opportunity (opportunity_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.salesforce_identifier_opportunity ADD CONSTRAINT opportunity_salesforce_identifier_opportunity_fk
FOREIGN KEY (opportunity_id)
REFERENCES cranium.opportunity (opportunity_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT horizontal_project_fk
FOREIGN KEY (horizontal_id)
REFERENCES cranium.horizontal (horizontal_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_checklist ADD CONSTRAINT checklist_project_checklist_fk
FOREIGN KEY (checklist_id)
REFERENCES cranium.checklist (checklist_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT project_sub_category_project_fk
FOREIGN KEY (project_sub_category_id)
REFERENCES cranium.project_sub_category (project_sub_category_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT project_project_location_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT project_project_fk
FOREIGN KEY (parent_project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_review ADD CONSTRAINT project_project_review_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_checklist ADD CONSTRAINT project_project_checklist_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.salesforce_identifier ADD CONSTRAINT project_salesforce_identifier_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.earmark ADD CONSTRAINT project_earmark_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.allocation ADD CONSTRAINT project_allocation_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_resources ADD CONSTRAINT project_location_project_resources_fk
FOREIGN KEY (project_location_id)
REFERENCES cranium.project_location (project_location_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.allocation ADD CONSTRAINT project_location_allocation_fk
FOREIGN KEY (project_location_id)
REFERENCES cranium.project_location (project_location_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_resources ADD CONSTRAINT primary_skill_project_resources_fk
FOREIGN KEY (primary_skill_id)
REFERENCES cranium.primary_skill (primary_skill_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.secondary_skill ADD CONSTRAINT primary_skill_secondary_skill_fk
FOREIGN KEY (primary_skill_id)
REFERENCES cranium.primary_skill (primary_skill_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_resources_secondary_skill ADD CONSTRAINT secondary_skill_project_resources_secondary_skill_fk
FOREIGN KEY (secondary_skill_id)
REFERENCES cranium.secondary_skill (secondary_skill_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee_skills ADD CONSTRAINT secondary_skill_employee_skills_fk
FOREIGN KEY (secondary_skill_id)
REFERENCES cranium.secondary_skill (secondary_skill_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_resources_secondary_skill ADD CONSTRAINT project_resources_project_resources_secondary_skill_fk
FOREIGN KEY (project_resources_id)
REFERENCES cranium.project_resources (project_resources_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.allocation ALTER COLUMN release_date DROP NOT NULL;
    
ALTER TABLE cranium.allocation ADD COLUMN is_billable boolean NOT NULL DEFAULT true;
=======

CREATE TABLE cranium.role (
                role_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT role_pk PRIMARY KEY (role_id)
);


CREATE TABLE cranium.vertical (
                vertical_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT vertical_pk PRIMARY KEY (vertical_id)
);


CREATE TABLE cranium.location (
                location_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT location_pk PRIMARY KEY (location_id)
);


CREATE TABLE cranium.account (
                account_id INTEGER NOT NULL,
                vertical_id SMALLINT NOT NULL,
                client_website_link VARCHAR,
                description VARCHAR,
                name VARCHAR NOT NULL,
                start_date DATE,
                end_date DATE,
                status VARCHAR NOT NULL,
                category VARCHAR NOT NULL,
                account_tok_link VARCHAR,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT account_pk PRIMARY KEY (account_id)
);


CREATE TABLE cranium.client_location (
                client_location_id BIGINT NOT NULL,
                account_id INTEGER NOT NULL,
                latitude NUMERIC(15,13) NOT NULL,
                location VARCHAR NOT NULL,
                longitude NUMERIC(16,13) NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT client_location_pk PRIMARY KEY (client_location_id)
);


CREATE TABLE cranium.title (
                title_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT title_pk PRIMARY KEY (title_id)
);


CREATE TABLE cranium.employee (
                employee_id BIGINT NOT NULL,
                title_id SMALLINT NOT NULL,
                location_id SMALLINT NOT NULL,
                reporting_manager_id BIGINT NOT NULL,
                first_name VARCHAR NOT NULL,
                last_name VARCHAR NOT NULL,
                emp_code VARCHAR NOT NULL,
                email VARCHAR NOT NULL,
                login_id VARCHAR NOT NULL,
                date_of_joining DATE NOT NULL,
                is_manager BOOLEAN NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT employee_pk PRIMARY KEY (employee_id)
);


CREATE TABLE cranium.employee_roles (
                employee_roles_id BIGINT NOT NULL,
                employee_id BIGINT NOT NULL,
                role_id SMALLINT NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by VARCHAR NOT NULL,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT employee_roles_pk PRIMARY KEY (employee_roles_id)
);


CREATE TABLE cranium.horizontal (
                horizontal_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT horizontal_pk PRIMARY KEY (horizontal_id)
);


CREATE TABLE cranium.checklist (
                checklist_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT checklist_pk PRIMARY KEY (checklist_id)
);


CREATE TABLE cranium.project_sub_category (
                project_sub_category_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT project_sub_category_pk PRIMARY KEY (project_sub_category_id)
);


CREATE TABLE cranium.project (
                project_id BIGINT NOT NULL,
                account_id INTEGER NOT NULL,
                horizontal_id SMALLINT,
                project_sub_category_id SMALLINT,
                parent_project_id BIGINT,
                dev_gdm_id BIGINT,
                qe_gdm_id BIGINT,
                business_manager_id BIGINT,
                name VARCHAR NOT NULL,
                start_date DATE,
                end_date DATE,
                description VARCHAR,
                technology VARCHAR,
                operating_system VARCHAR,
                database VARCHAR,
                is_sub_project BOOLEAN,
                project_kickoff_is_required BOOLEAN,
                send_notification_to_pin_group BOOLEAN,
                current_status VARCHAR,
                current_comment VARCHAR,
                project_tok_link VARCHAR,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT project_pk PRIMARY KEY (project_id)
);


CREATE TABLE cranium.salesforce_identifier (
                salesforce_identifier_id BIGINT NOT NULL,
                project_id BIGINT NOT NULL,
                value VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT salesforce_identifier_pk PRIMARY KEY (salesforce_identifier_id)
);

CREATE TABLE cranium.gdm_comment (
                gdm_comment_id BIGINT NOT NULL,
                project_id BIGINT NOT NULL,
                value VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT gdm_comment_pk PRIMARY KEY (gdm_comment_id)
);


CREATE TABLE cranium.project_checklist (
                project_checklist_id BIGINT NOT NULL,
                checklist_id SMALLINT NOT NULL,
                project_id BIGINT NOT NULL,
                is_selected BOOLEAN NOT NULL,
                comment VARCHAR,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT project_checklist_pk PRIMARY KEY (project_checklist_id)
);


CREATE TABLE cranium.project_review (
                project_review_id BIGINT NOT NULL,
                project_id BIGINT NOT NULL,
                status VARCHAR NOT NULL,
                comment VARCHAR,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT project_review_pk PRIMARY KEY (project_review_id)
);


CREATE TABLE cranium.project_location (
                project_location_id BIGINT NOT NULL,
                project_id BIGINT NOT NULL,
                location_id SMALLINT NOT NULL,
                dev_manager_id BIGINT,
                qe_manager_id BIGINT,
                ui_manager_id BIGINT,
                manager1_id BIGINT,
                manager2_id BIGINT,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT project_location_pk PRIMARY KEY (project_location_id)
);


CREATE TABLE cranium.primary_skill (
                primary_skill_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT primary_skill_pk PRIMARY KEY (primary_skill_id)
);


CREATE TABLE cranium.secondary_skill (
                secondary_skill_id SMALLINT NOT NULL,
                name VARCHAR NOT NULL,
                primary_skill_id SMALLINT NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT secondary_skill_pk PRIMARY KEY (secondary_skill_id)
);


CREATE TABLE cranium.project_resources (
                project_resources_id BIGINT NOT NULL,
                project_location_id BIGINT NOT NULL,
                title_id SMALLINT NOT NULL,
                number_of_resources SMALLINT,
                start_date DATE,
                end_date DATE,
                allocation_percentage SMALLINT,
                is_billable BOOLEAN NOT NULL,
                primary_skill_id SMALLINT NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN DEFAULT true NOT NULL,
                CONSTRAINT project_resources_pk PRIMARY KEY (project_resources_id)
);


CREATE TABLE cranium.project_resources_secondary_skill (
                project_resources_secondary_skill_id BIGINT NOT NULL,
                project_resources_id BIGINT NOT NULL,
                secondary_skill_id SMALLINT NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT project_resources_secondary_skill_pk PRIMARY KEY (project_resources_secondary_skill_id)
);


CREATE TABLE cranium.contact (
                contact_id BIGINT NOT NULL,
                client_location_id BIGINT NOT NULL,
                name VARCHAR NOT NULL,
                phone_number VARCHAR NOT NULL,
                email VARCHAR NOT NULL,
                created_on TIMESTAMP NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on TIMESTAMP,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT contact_pk PRIMARY KEY (contact_id)
);

CREATE TABLE cranium.employee_skill (
				employee_skills_id BIGINT NOT NULL,
				employee_id BIGINT NOT NULL,
				secondary_skill_id BIGINT NOT NULL,
				created_on TIMESTAMP NOT NULL,
				created_by BIGINT NOT NULL,
				modified_on TIMESTAMP ,
				modified_by BIGINT,
				is_active boolean NOT NULL,
				CONSTRAINT employee_skills_pk PRIMARY KEY (employee_skills_id)
);

CREATE TABLE cranium.allocation (
				allocation_id BIGINT NOT NULL,
				project_id BIGINT NOT NULL,
				employee_id BIGINT NOT NULL,
				project_location_id BIGINT NOT NULL,
				work_group_id BIGINT NOT NULL,
				reporting_manager_id BIGINT NOT NULL,
				allocation_manager_id BIGINT NOT NULL,
				allocation_status_id BIGINT NOT NULL,
				deallocated_by_id BIGINT NULL,
				allocated_percentage SMALLINT NOT NULL,
				start_date date NOT NULL,
				release_date TIMESTAMP NOT NULL,
				deallocated_on TIMESTAMP NULL,
				created_on DATE NOT NULL,
				created_by BIGINT NOT NULL,
				modified_on date NULL,
				modified_by BIGINT NULL,
				is_active boolean NOT NULL,
				CONSTRAINT allocation_pk PRIMARY KEY (allocation_id)
);

CREATE TABLE cranium.allocation_status (
				allocation_status_id BIGINT NOT NULL,
				status VARCHAR NOT NULL,
				created_on TIMESTAMP NOT NULL,
				created_by BIGINT NOT NULL,
				modified_on TIMESTAMP NULL,
				modified_by BIGINT NULL,
				is_active boolean NOT NULL,
				CONSTRAINT allocation_status_pk PRIMARY KEY (allocation_status_id)
);

CREATE TABLE cranium.work_group (
				work_group_id SMALLINT NOT NULL,
				"name" VARCHAR NOT NULL,
				created_on TIMESTAMP NOT NULL,
				created_by BIGINT NOT NULL,
				modified_on TIMESTAMP NULL,
				modified_by BIGINT NULL,
				is_active boolean NOT NULL,
				CONSTRAINT work_group_pk PRIMARY KEY (work_group_id)
);

CREATE TABLE cranium.earmark (
                earmark_id BIGINT NOT NULL,
                allocation_id BIGINT NOT NULL,
                manager_id BIGINT NOT NULL,
                project_id BIGINT ,
                opportunity_id BIGINT ,
                billable BOOLEAN NOT NULL,
                is_client_interview_needed BOOLEAN NOT NULL,
                percentage SMALLINT NOT NULL,
                start_date DATE NOT NULL,
                end_date DATE NOT NULL,
                created_on DATE NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on DATE,
                modified_by DATE,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT earmark_pk PRIMARY KEY (earmark_id)
);

CREATE TABLE cranium.opportunity (
                opportunity_id BIGINT NOT NULL,
                account_id INTEGER,
                vertical_id SMALLINT NOT NULL,
                dev_gdm BIGINT,
                qa_gdm BIGINT,
                account_name VARCHAR,
                name VARCHAR NOT NULL,
                created_on DATE NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on DATE,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT opportunity_pk PRIMARY KEY (opportunity_id)
);

CREATE TABLE cranium.salesforce_identifier_opportunity (
                salesforce_identifier_opportunity_id BIGINT NOT NULL,
                opportunity_id BIGINT NOT NULL,
                value VARCHAR NOT NULL,
                created_on DATE NOT NULL,
                created_by BIGINT NOT NULL,
                modified_on DATE,
                modified_by BIGINT,
                is_active BOOLEAN NOT NULL,
                CONSTRAINT salesforce_identifier_opportunity_pk PRIMARY KEY (salesforce_identifier_opportunity_id)
);

ALTER TABLE cranium.employee_roles ADD CONSTRAINT role_employee_roles_fk
FOREIGN KEY (role_id)
REFERENCES cranium.role (role_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.account ADD CONSTRAINT vertical_account_fk
FOREIGN KEY (vertical_id)
REFERENCES cranium.vertical (vertical_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT location_project_location_fk
FOREIGN KEY (location_id)
REFERENCES cranium.location (location_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee ADD CONSTRAINT location_employee_fk
FOREIGN KEY (location_id)
REFERENCES cranium.location (location_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT account_project_fk
FOREIGN KEY (account_id)
REFERENCES cranium.account (account_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.client_location ADD CONSTRAINT account_client_location_fk
FOREIGN KEY (account_id)
REFERENCES cranium.account (account_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.contact ADD CONSTRAINT client_location_contact_fk
FOREIGN KEY (client_location_id)
REFERENCES cranium.client_location (client_location_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_resources ADD CONSTRAINT title_project_resources_fk
FOREIGN KEY (title_id)
REFERENCES cranium.title (title_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee ADD CONSTRAINT title_employee_fk
FOREIGN KEY (title_id)
REFERENCES cranium.title (title_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT employee_project_location_fk
FOREIGN KEY (dev_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT employee_project_location_fk1
FOREIGN KEY (qe_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT employee_project_location_fk2
FOREIGN KEY (ui_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT employee_project_location_fk3
FOREIGN KEY (manager1_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT employee_project_location_fk4
FOREIGN KEY (manager2_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT employee_project_fk
FOREIGN KEY (dev_gdm_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT employee_project_fk1
FOREIGN KEY (qe_gdm_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT employee_project_fk2
FOREIGN KEY (business_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee ADD CONSTRAINT employee_employee_fk
FOREIGN KEY (reporting_manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee_roles ADD CONSTRAINT employee_employee_roles_fk
FOREIGN KEY (employee_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT horizontal_project_fk
FOREIGN KEY (horizontal_id)
REFERENCES cranium.horizontal (horizontal_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_checklist ADD CONSTRAINT checklist_project_checklist_fk
FOREIGN KEY (checklist_id)
REFERENCES cranium.checklist (checklist_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT project_sub_category_project_fk
FOREIGN KEY (project_sub_category_id)
REFERENCES cranium.project_sub_category (project_sub_category_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_location ADD CONSTRAINT project_project_location_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project ADD CONSTRAINT project_project_fk
FOREIGN KEY (parent_project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_review ADD CONSTRAINT project_project_review_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_checklist ADD CONSTRAINT project_project_checklist_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.salesforce_identifier ADD CONSTRAINT project_salesforce_identifier_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.gdm_comment ADD CONSTRAINT project_gdm_comment_fk
FOREIGN KEY (project_id)
REFERENCES cranium.project (project_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_resources ADD CONSTRAINT project_location_project_resources_fk
FOREIGN KEY (project_location_id)
REFERENCES cranium.project_location (project_location_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_resources ADD CONSTRAINT primary_skill_project_resources_fk
FOREIGN KEY (primary_skill_id)
REFERENCES cranium.primary_skill (primary_skill_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.secondary_skill ADD CONSTRAINT primary_skill_secondary_skill_fk
FOREIGN KEY (primary_skill_id)
REFERENCES cranium.primary_skill (primary_skill_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_resources_secondary_skill ADD CONSTRAINT secondary_skill_project_resources_secondary_skill_fk
FOREIGN KEY (secondary_skill_id)
REFERENCES cranium.secondary_skill (secondary_skill_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.project_resources_secondary_skill ADD CONSTRAINT project_resources_project_resources_secondary_skill_fk
FOREIGN KEY (project_resources_id)
REFERENCES cranium.project_resources (project_resources_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.employee_skill ADD CONSTRAINT employee_skills_employee_id_fk
FOREIGN KEY (employee_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE cranium.employee_skill ADD CONSTRAINT employee_skills_secondary_skill_fk 
FOREIGN KEY (secondary_skill_id)
REFERENCES cranium.secondary_skill (secondary_skill_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.earmark ADD CONSTRAINT employee_earmark_fk
FOREIGN KEY (allocation_id)
REFERENCES cranium.allocation (allocation_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.earmark ADD CONSTRAINT employee_earmark_fk1
FOREIGN KEY (manager_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.earmark ADD CONSTRAINT project_earmark_fk 
FOREIGN KEY (project_id)
    REFERENCES cranium.project (project_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE cranium.opportunity ADD CONSTRAINT employee_opportunity_fk
FOREIGN KEY (dev_gdm)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.opportunity ADD CONSTRAINT employee_opportunity_fk1
FOREIGN KEY (qa_gdm)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.allocation ADD CONSTRAINT employee_allocation_fk
FOREIGN KEY (employee_id)
REFERENCES cranium.employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.earmark ADD CONSTRAINT opportunity_earmark_fk
FOREIGN KEY (opportunity_id)
REFERENCES cranium.opportunity (opportunity_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE cranium.salesforce_identifier_opportunity ADD CONSTRAINT opportunity_salesforce_identifier_opportunity_fk
FOREIGN KEY (opportunity_id)
REFERENCES cranium.opportunity (opportunity_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
>>>>>>> origin/develop
