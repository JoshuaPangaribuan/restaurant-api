CREATE TABLE restaurant_tables (
    table_id VARCHAR(16) NOT NULL PRIMARY KEY,
    restaurant_id VARCHAR(16) NOT NULL,
    qr_path VARCHAR(1000) NOT NULL,
    occupied BOOLEAN NOT NULL DEFAULT FALSE,
    section TEXT NOT NULL,
    floor_level INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant_account(restaurant_id)
);