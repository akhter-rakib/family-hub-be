-- V5: Seed data - Units and default categories
-- Using INSERT IGNORE to make migration idempotent (safe for production)

-- Weight units (base: gram)
INSERT IGNORE INTO units (id, name, abbreviation, base_unit, conversion_factor, unit_type) VALUES
(UUID(), 'Kilogram', 'kg', 'g', 1000.000000, 'WEIGHT'),
(UUID(), 'Gram', 'g', 'g', 1.000000, 'WEIGHT'),
(UUID(), 'Pound', 'lb', 'g', 453.592000, 'WEIGHT');

-- Volume units (base: milliliter)
INSERT IGNORE INTO units (id, name, abbreviation, base_unit, conversion_factor, unit_type) VALUES
(UUID(), 'Liter', 'L', 'ml', 1000.000000, 'VOLUME'),
(UUID(), 'Milliliter', 'ml', 'ml', 1.000000, 'VOLUME');

-- Count units
INSERT IGNORE INTO units (id, name, abbreviation, base_unit, conversion_factor, unit_type) VALUES
(UUID(), 'Piece', 'pcs', 'pcs', 1.000000, 'COUNT'),
(UUID(), 'Dozen', 'dz', 'pcs', 12.000000, 'COUNT'),
(UUID(), 'Pack', 'pack', 'pack', 1.000000, 'COUNT'),
(UUID(), 'Box', 'box', 'box', 1.000000, 'COUNT');

-- Global categories (family_id IS NULL = global)
-- Using procedure to avoid duplicates since categories don't have unique name constraint
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Meat & Fish', '🥩', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Meat & Fish' AND family_id IS NULL);
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Vegetables', '🥬', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Vegetables' AND family_id IS NULL);
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Fruits', '🍎', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Fruits' AND family_id IS NULL);
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Dairy', '🥛', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Dairy' AND family_id IS NULL);
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Grains & Rice', '🌾', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Grains & Rice' AND family_id IS NULL);
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Spices', '🌶️', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Spices' AND family_id IS NULL);
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Snacks', '🍪', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Snacks' AND family_id IS NULL);
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Beverages', '🥤', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Beverages' AND family_id IS NULL);
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Cleaning', '🧹', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Cleaning' AND family_id IS NULL);
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Personal Care', '🧴', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Personal Care' AND family_id IS NULL);
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Baby', '👶', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Baby' AND family_id IS NULL);
INSERT INTO categories (id, name, icon, family_id)
SELECT UUID(), 'Other', '📦', NULL FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Other' AND family_id IS NULL);

-- Global items (common household items) - using NOT EXISTS to prevent duplicates
INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Chicken', (SELECT id FROM categories WHERE name = 'Meat & Fish' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Chicken' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Fish', (SELECT id FROM categories WHERE name = 'Meat & Fish' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Fish' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Beef', (SELECT id FROM categories WHERE name = 'Meat & Fish' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Beef' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Egg', (SELECT id FROM categories WHERE name = 'Dairy' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'dz' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Egg' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Milk', (SELECT id FROM categories WHERE name = 'Dairy' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'L' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Milk' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Rice', (SELECT id FROM categories WHERE name = 'Grains & Rice' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Rice' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Onion', (SELECT id FROM categories WHERE name = 'Vegetables' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Onion' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Potato', (SELECT id FROM categories WHERE name = 'Vegetables' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Potato' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Tomato', (SELECT id FROM categories WHERE name = 'Vegetables' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Tomato' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Oil', (SELECT id FROM categories WHERE name = 'Grains & Rice' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'L' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Oil' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Sugar', (SELECT id FROM categories WHERE name = 'Grains & Rice' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Sugar' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Salt', (SELECT id FROM categories WHERE name = 'Spices' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Salt' AND family_id IS NULL);

INSERT INTO items (id, name, category_id, default_unit_id, family_id, active)
SELECT UUID(), 'Water Bottle', (SELECT id FROM categories WHERE name = 'Beverages' AND family_id IS NULL LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'L' LIMIT 1), NULL, TRUE
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM items WHERE name = 'Water Bottle' AND family_id IS NULL);
