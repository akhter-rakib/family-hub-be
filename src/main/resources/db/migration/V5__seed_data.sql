-- V5: Seed data - Units and default categories

-- Weight units (base: gram)
INSERT INTO units (id, name, abbreviation, base_unit, conversion_factor, unit_type) VALUES
(UUID(), 'Kilogram', 'kg', 'g', 1000.000000, 'WEIGHT'),
(UUID(), 'Gram', 'g', 'g', 1.000000, 'WEIGHT'),
(UUID(), 'Pound', 'lb', 'g', 453.592000, 'WEIGHT');

-- Volume units (base: milliliter)
INSERT INTO units (id, name, abbreviation, base_unit, conversion_factor, unit_type) VALUES
(UUID(), 'Liter', 'L', 'ml', 1000.000000, 'VOLUME'),
(UUID(), 'Milliliter', 'ml', 'ml', 1.000000, 'VOLUME');

-- Count units
INSERT INTO units (id, name, abbreviation, base_unit, conversion_factor, unit_type) VALUES
(UUID(), 'Piece', 'pcs', 'pcs', 1.000000, 'COUNT'),
(UUID(), 'Dozen', 'dz', 'pcs', 12.000000, 'COUNT'),
(UUID(), 'Pack', 'pack', 'pack', 1.000000, 'COUNT'),
(UUID(), 'Box', 'box', 'box', 1.000000, 'COUNT');

-- Global categories (family_id IS NULL = global)
INSERT INTO categories (id, name, icon, family_id) VALUES
(UUID(), 'Meat & Fish', '🥩', NULL),
(UUID(), 'Vegetables', '🥬', NULL),
(UUID(), 'Fruits', '🍎', NULL),
(UUID(), 'Dairy', '🥛', NULL),
(UUID(), 'Grains & Rice', '🌾', NULL),
(UUID(), 'Spices', '🌶️', NULL),
(UUID(), 'Snacks', '🍪', NULL),
(UUID(), 'Beverages', '🥤', NULL),
(UUID(), 'Cleaning', '🧹', NULL),
(UUID(), 'Personal Care', '🧴', NULL),
(UUID(), 'Baby', '👶', NULL),
(UUID(), 'Other', '📦', NULL);

-- Global items (common household items)
INSERT INTO items (id, name, category_id, default_unit_id, family_id, active) VALUES
(UUID(), 'Chicken', (SELECT id FROM categories WHERE name = 'Meat & Fish' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE),
(UUID(), 'Fish', (SELECT id FROM categories WHERE name = 'Meat & Fish' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE),
(UUID(), 'Beef', (SELECT id FROM categories WHERE name = 'Meat & Fish' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE),
(UUID(), 'Egg', (SELECT id FROM categories WHERE name = 'Dairy' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'dz' LIMIT 1), NULL, TRUE),
(UUID(), 'Milk', (SELECT id FROM categories WHERE name = 'Dairy' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'L' LIMIT 1), NULL, TRUE),
(UUID(), 'Rice', (SELECT id FROM categories WHERE name = 'Grains & Rice' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE),
(UUID(), 'Onion', (SELECT id FROM categories WHERE name = 'Vegetables' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE),
(UUID(), 'Potato', (SELECT id FROM categories WHERE name = 'Vegetables' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE),
(UUID(), 'Tomato', (SELECT id FROM categories WHERE name = 'Vegetables' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE),
(UUID(), 'Oil', (SELECT id FROM categories WHERE name = 'Grains & Rice' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'L' LIMIT 1), NULL, TRUE),
(UUID(), 'Sugar', (SELECT id FROM categories WHERE name = 'Grains & Rice' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE),
(UUID(), 'Salt', (SELECT id FROM categories WHERE name = 'Spices' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'kg' LIMIT 1), NULL, TRUE),
(UUID(), 'Water Bottle', (SELECT id FROM categories WHERE name = 'Beverages' LIMIT 1), (SELECT id FROM units WHERE abbreviation = 'L' LIMIT 1), NULL, TRUE);
