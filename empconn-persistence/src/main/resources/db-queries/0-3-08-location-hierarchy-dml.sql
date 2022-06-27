
update cranium.location set hierarchy=1 where name='Bangalore';
update cranium.location set hierarchy=2 where name='Noida';
update cranium.location set hierarchy=3 where name='Hyderabad';
update cranium.location set hierarchy=4 where name='Kolkatta';
update cranium.location set hierarchy=5 where name not in ('Bangalore', 'Noida','Hyderabad', 'Kolkatta');

	