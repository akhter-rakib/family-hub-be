-- V7: Add Gas category and Gas Cylinder item
-- Using NOT EXISTS to make migration idempotent (safe for production)

INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Gas', '⛽', NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Gas' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Gas Cylinder',
       (SELECT id FROM categories WHERE name = 'Gas' AND family_id IS NULL LIMIT 1),
       (SELECT id FROM units WHERE abbreviation = 'pcs' LIMIT 1),
       NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Gas Cylinder' AND family_id IS NULL);
