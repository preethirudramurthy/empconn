
insert into cranium.account(account_id, vertical_id, name, created_on, created_by, status, category, is_active, client_website_link, description, start_date)
values(nextval('cranium.account_id_sequence'), (select vertical_id from cranium.vertical where name = 'Tavant'), 'Tavant', now(), 1, 'OPEN', 'Internal',true, 'http://tavant.com', 'Tavant Technologies', '2004-11-21');

