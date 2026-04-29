-- V10: DESCO config per family
CREATE TABLE desco_config (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    family_id VARCHAR(36) NOT NULL,
    account_no VARCHAR(20) NOT NULL,
    meter_no VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_family (family_id),
    FOREIGN KEY (family_id) REFERENCES families(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Add family_id to desco_balance
ALTER TABLE desco_balance ADD COLUMN family_id VARCHAR(36) AFTER id;
ALTER TABLE desco_balance ADD CONSTRAINT fk_desco_balance_family FOREIGN KEY (family_id) REFERENCES families(id);
CREATE INDEX idx_desco_balance_family ON desco_balance(family_id, fetched_at DESC);
