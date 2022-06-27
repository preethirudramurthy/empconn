INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'AWS Lambda', (select primary_skill_id from cranium.primary_skill where name = 'AWS'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Elastic Bean Stalk', (select primary_skill_id from cranium.primary_skill where name = 'AWS'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'ECS', (select primary_skill_id from cranium.primary_skill where name = 'AWS'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Amazon Aurora', (select primary_skill_id from cranium.primary_skill where name = 'AWS'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Amazon RDS', (select primary_skill_id from cranium.primary_skill where name = 'AWS'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Amazon DynamoDB', (select primary_skill_id from cranium.primary_skill where name = 'AWS'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Amazon ElastiCache', (select primary_skill_id from cranium.primary_skill where name = 'AWS'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Amazon Redshift', (select primary_skill_id from cranium.primary_skill where name = 'AWS'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Amazon Athena', (select primary_skill_id from cranium.primary_skill where name = 'AWS'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Amazon Kinesis', (select primary_skill_id from cranium.primary_skill where name = 'AWS'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Azure Function', (select primary_skill_id from cranium.primary_skill where name = 'Azure'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Azure Service Fabric', (select primary_skill_id from cranium.primary_skill where name = 'Azure'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Azure Web App', (select primary_skill_id from cranium.primary_skill where name = 'Azure'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Azure API App', (select primary_skill_id from cranium.primary_skill where name = 'Azure'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Azure Logic App', (select primary_skill_id from cranium.primary_skill where name = 'Azure'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Azure Storage Service', (select primary_skill_id from cranium.primary_skill where name = 'Azure'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Azure Document DB', (select primary_skill_id from cranium.primary_skill where name = 'Azure'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Azure Service Bus', (select primary_skill_id from cranium.primary_skill where name = 'Azure'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Azure ACS', (select primary_skill_id from cranium.primary_skill where name = 'Azure'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'OBIEE', (select primary_skill_id from cranium.primary_skill where name = 'BI_Analytics'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Tableau', (select primary_skill_id from cranium.primary_skill where name = 'BI_Analytics'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Pentaho', (select primary_skill_id from cranium.primary_skill where name = 'BI_Analytics'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Hive', (select primary_skill_id from cranium.primary_skill where name = 'BI_Analytics'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Pig', (select primary_skill_id from cranium.primary_skill where name = 'BI_Analytics'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Spark', (select primary_skill_id from cranium.primary_skill where name = 'BI_Analytics'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Hadoop', (select primary_skill_id from cranium.primary_skill where name = 'BigData'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Hbase', (select primary_skill_id from cranium.primary_skill where name = 'BigData'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Hortonworks', (select primary_skill_id from cranium.primary_skill where name = 'BigData'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Cloudera', (select primary_skill_id from cranium.primary_skill where name = 'BigData'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Presto', (select primary_skill_id from cranium.primary_skill where name = 'BigData'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Spark', (select primary_skill_id from cranium.primary_skill where name = 'BigData'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Oozie', (select primary_skill_id from cranium.primary_skill where name = 'BigData'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'SAS', (select primary_skill_id from cranium.primary_skill where name = 'BigData'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Azkaban', (select primary_skill_id from cranium.primary_skill where name = 'BigData'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Celery', (select primary_skill_id from cranium.primary_skill where name = 'BigData'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Jenkins', (select primary_skill_id from cranium.primary_skill where name = 'CI_CD'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'VSTS', (select primary_skill_id from cranium.primary_skill where name = 'CI_CD'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Powershell', (select primary_skill_id from cranium.primary_skill where name = 'CI_CD'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Terraform', (select primary_skill_id from cranium.primary_skill where name = 'CI_CD'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Code Deploy', (select primary_skill_id from cranium.primary_skill where name = 'CI_CD'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Chef', (select primary_skill_id from cranium.primary_skill where name = 'CI_CD'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Puppet', (select primary_skill_id from cranium.primary_skill where name = 'CI_CD'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Git', (select primary_skill_id from cranium.primary_skill where name = 'CI_CD'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Drupal', (select primary_skill_id from cranium.primary_skill where name = 'CMS_ECommerce'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Magento', (select primary_skill_id from cranium.primary_skill where name = 'CMS_ECommerce'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Kentico', (select primary_skill_id from cranium.primary_skill where name = 'CMS_ECommerce'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Alfresco', (select primary_skill_id from cranium.primary_skill where name = 'CMS_ECommerce'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Adobe', (select primary_skill_id from cranium.primary_skill where name = 'CMS_ECommerce'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Joomla', (select primary_skill_id from cranium.primary_skill where name = 'CMS_ECommerce'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'R', (select primary_skill_id from cranium.primary_skill where name = 'Data_Science'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'SAS', (select primary_skill_id from cranium.primary_skill where name = 'Data_Science'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Matlab', (select primary_skill_id from cranium.primary_skill where name = 'Data_Science'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Weka', (select primary_skill_id from cranium.primary_skill where name = 'Data_Science'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Oracle', (select primary_skill_id from cranium.primary_skill where name = 'DBA'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'MySQL', (select primary_skill_id from cranium.primary_skill where name = 'DBA'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'SQL Server', (select primary_skill_id from cranium.primary_skill where name = 'DBA'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Postgres', (select primary_skill_id from cranium.primary_skill where name = 'DBA'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Spring', (select primary_skill_id from cranium.primary_skill where name = 'Java'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Hibernate', (select primary_skill_id from cranium.primary_skill where name = 'Java'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Drools', (select primary_skill_id from cranium.primary_skill where name = 'Java'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Activiti', (select primary_skill_id from cranium.primary_skill where name = 'Java'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'JBPM', (select primary_skill_id from cranium.primary_skill where name = 'Java'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Apache Camel', (select primary_skill_id from cranium.primary_skill where name = 'Java'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'AngularJS', (select primary_skill_id from cranium.primary_skill where name = 'JavaScript'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'ReactJS', (select primary_skill_id from cranium.primary_skill where name = 'JavaScript'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Nodejs', (select primary_skill_id from cranium.primary_skill where name = 'JavaScript'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Jquery', (select primary_skill_id from cranium.primary_skill where name = 'JavaScript'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Typescript', (select primary_skill_id from cranium.primary_skill where name = 'JavaScript'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'ActiveMQ', (select primary_skill_id from cranium.primary_skill where name = 'MessageBroker'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'RabbitMQ', (select primary_skill_id from cranium.primary_skill where name = 'MessageBroker'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Kafka', (select primary_skill_id from cranium.primary_skill where name = 'MessageBroker'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'TensorFlow', (select primary_skill_id from cranium.primary_skill where name = 'ML/AI'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Azure ML', (select primary_skill_id from cranium.primary_skill where name = 'ML/AI'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Amazon AI', (select primary_skill_id from cranium.primary_skill where name = 'ML/AI'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'IBM Watson', (select primary_skill_id from cranium.primary_skill where name = 'ML/AI'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Spark Mlib', (select primary_skill_id from cranium.primary_skill where name = 'ML/AI'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'CNTK', (select primary_skill_id from cranium.primary_skill where name = 'ML/AI'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Objective C', (select primary_skill_id from cranium.primary_skill where name = 'Mobile_Development'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Xamarin', (select primary_skill_id from cranium.primary_skill where name = 'Mobile_Development'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Appcelerator', (select primary_skill_id from cranium.primary_skill where name = 'Mobile_Development'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'C#', (select primary_skill_id from cranium.primary_skill where name = 'NET'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'ASP.Net Core', (select primary_skill_id from cranium.primary_skill where name = 'NET'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), '.Net Full Framework', (select primary_skill_id from cranium.primary_skill where name = 'NET'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'ASP.Net Web API', (select primary_skill_id from cranium.primary_skill where name = 'NET'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'ASP.Net MVC', (select primary_skill_id from cranium.primary_skill where name = 'NET'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'WCF', (select primary_skill_id from cranium.primary_skill where name = 'NET'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'WPF', (select primary_skill_id from cranium.primary_skill where name = 'NET'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Entity Framework', (select primary_skill_id from cranium.primary_skill where name = 'NET'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Identity Server', (select primary_skill_id from cranium.primary_skill where name = 'NET'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Cassandra', (select primary_skill_id from cranium.primary_skill where name = 'NoSQL_Databases'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'MongoDB', (select primary_skill_id from cranium.primary_skill where name = 'NoSQL_Databases'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'CouchDB', (select primary_skill_id from cranium.primary_skill where name = 'NoSQL_Databases'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Zend', (select primary_skill_id from cranium.primary_skill where name = 'PHP'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Symfony', (select primary_skill_id from cranium.primary_skill where name = 'PHP'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Aura', (select primary_skill_id from cranium.primary_skill where name = 'PHP'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Kohana', (select primary_skill_id from cranium.primary_skill where name = 'PHP'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Django', (select primary_skill_id from cranium.primary_skill where name = 'Python'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Flask', (select primary_skill_id from cranium.primary_skill where name = 'Python'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Turbo Gears', (select primary_skill_id from cranium.primary_skill where name = 'Python'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'web2py', (select primary_skill_id from cranium.primary_skill where name = 'Python'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Pyramid', (select primary_skill_id from cranium.primary_skill where name = 'Python'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'ROR', (select primary_skill_id from cranium.primary_skill where name = 'Ruby'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Elastic Search', (select primary_skill_id from cranium.primary_skill where name = 'Search_Engine'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Lucene', (select primary_skill_id from cranium.primary_skill where name = 'Search_Engine'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Solr', (select primary_skill_id from cranium.primary_skill where name = 'Search_Engine'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Android  -Java', (select primary_skill_id from cranium.primary_skill where name = 'Mobile_Development'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Android - Kotlin', (select primary_skill_id from cranium.primary_skill where name = 'Mobile_Development'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'iOS – Swift', (select primary_skill_id from cranium.primary_skill where name = 'Mobile_Development'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'iOS – ObjectiveC', (select primary_skill_id from cranium.primary_skill where name = 'Mobile_Development'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'React Native', (select primary_skill_id from cranium.primary_skill where name = 'Mobile_Development'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Ionic Framework', (select primary_skill_id from cranium.primary_skill where name = 'Mobile_Development'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Cordova/Phonegap', (select primary_skill_id from cranium.primary_skill where name = 'Mobile_Development'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Manual Testing', (select primary_skill_id from cranium.primary_skill where name = 'QA'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Automation Testing', (select primary_skill_id from cranium.primary_skill where name = 'QA'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Chef', (select primary_skill_id from cranium.primary_skill where name = 'DevOps Tools'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Jenkins', (select primary_skill_id from cranium.primary_skill where name = 'DevOps Tools'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Openshift', (select primary_skill_id from cranium.primary_skill where name = 'DevOps Tools'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Functional Requirements', (select primary_skill_id from cranium.primary_skill where name = 'Business Analysis'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Business Requirements', (select primary_skill_id from cranium.primary_skill where name = 'Business Analysis'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'API Manager', (select primary_skill_id from cranium.primary_skill where name = 'Mulesoft'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Mule Runtime', (select primary_skill_id from cranium.primary_skill where name = 'Mulesoft'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Cloudhub', (select primary_skill_id from cranium.primary_skill where name = 'Mulesoft'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Oracle Enterprise Service Bus (OSB)', (select primary_skill_id from cranium.primary_skill where name = 'Oracle-SOA'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'BPEL Process Manager', (select primary_skill_id from cranium.primary_skill where name = 'Oracle-SOA'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Oracle Data Integrator (ODI)', (select primary_skill_id from cranium.primary_skill where name = 'Oracle-SOA'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Oracle Business Activity Monitoring (Oracle BAM)', (select primary_skill_id from cranium.primary_skill where name = 'Oracle-SOA'), now(), null, 1, 1, '1');
INSERT INTO cranium.secondary_skill(secondary_skill_id, name, primary_skill_id, created_on, modified_on, created_by, modified_by, is_active) VALUES (nextval('cranium.secondary_skill_id_sequence'), 'Scripting', (select primary_skill_id from cranium.primary_skill where name = 'Python'), now(), null, 1, 1, '1');
