
DELETE FROM cranium.earmark;
UPDATE cranium.employee SET primary_allocation_id = null;
DELETE FROM cranium.allocation;
DELETE FROM  cranium.project_location WHERE project_location_id IN (SELECT pl.project_location_id FROM  cranium.project_location pl INNER JOIN cranium.project p ON p.project_id = pl.project_id WHERE p.name IN ('Central Bench', 'NDBench'));
