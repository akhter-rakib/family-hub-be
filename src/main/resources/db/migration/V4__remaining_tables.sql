-- V4: Bills, Gas, Inventory, Notifications, Budgets

CREATE TABLE bills (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    family_id VARCHAR(36) NOT NULL,
    title VARCHAR(100) NOT NULL,
    bill_type VARCHAR(30) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    due_date DATE NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    paid_date DATE,
    recurring BOOLEAN NOT NULL DEFAULT FALSE,
    recurrence_interval VARCHAR(20),
    note VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (family_id) REFERENCES families(id),
    INDEX idx_bills_family (family_id),
    INDEX idx_bills_due (family_id, paid, due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE gas_usage_logs (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    family_id VARCHAR(36) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    days_used INT,
    cost DECIMAL(12,2),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    purchase_id VARCHAR(36),
    note VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (family_id) REFERENCES families(id),
    FOREIGN KEY (purchase_id) REFERENCES purchase_records(id),
    INDEX idx_gas_family (family_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE inventory_items (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    family_id VARCHAR(36) NOT NULL,
    item_id VARCHAR(36) NOT NULL,
    quantity DECIMAL(10,3) NOT NULL,
    unit_id VARCHAR(36) NOT NULL,
    low_stock_threshold DECIMAL(10,3),
    expiry_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_inv_family_item (family_id, item_id),
    FOREIGN KEY (family_id) REFERENCES families(id),
    FOREIGN KEY (item_id) REFERENCES items(id),
    FOREIGN KEY (unit_id) REFERENCES units(id),
    INDEX idx_inv_family (family_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE budgets (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    family_id VARCHAR(36) NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_budget_family_month (family_id, year, month),
    FOREIGN KEY (family_id) REFERENCES families(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE notifications (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    family_id VARCHAR(36),
    type VARCHAR(50) NOT NULL,
    message VARCHAR(500) NOT NULL,
    read_status BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_notif_user (user_id, read_status),
    INDEX idx_notif_created (user_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
