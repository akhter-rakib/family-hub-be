-- V7: Add Gas category and Gas Cylinder item

INSERT INTO categories (id, name, icon, family_id) VALUES
(UUID(), 'Gas', '⛽', NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active) VALUES
(UUID(), 'Gas Cylinder', (SELECT id FROM categories WHERE name = 'Gas' LIMIT 1),
 (SELECT id FROM units WHERE abbreviation = 'pcs' LIMIT 1), NULL, TRUE);
