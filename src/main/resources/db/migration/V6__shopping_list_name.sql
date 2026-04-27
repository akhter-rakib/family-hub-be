-- V6: Add list_name column to shopping_requests for grouping items by named list or date
ALTER TABLE shopping_requests ADD COLUMN list_name VARCHAR(100) NULL AFTER note;
