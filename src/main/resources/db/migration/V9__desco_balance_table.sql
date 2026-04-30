-- V9: DESCO prepaid meter balance tracking

CREATE TABLE desco_balance (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    account_no VARCHAR(20) NOT NULL,
    meter_no VARCHAR(20) NOT NULL,
    balance DECIMAL(12,2) NOT NULL,
    current_month_consumption DECIMAL(12,2) NOT NULL,
    reading_time TIMESTAMP NOT NULL,
    fetched_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_desco_fetched (fetched_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
