ALTER TABLE cranium.employee ADD COLUMN primary_allocation_id bigint;
	
ALTER TABLE cranium.employee ADD CONSTRAINT primary_allocation_fk
FOREIGN KEY (primary_allocation_id)
REFERENCES cranium.allocation (allocation_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;