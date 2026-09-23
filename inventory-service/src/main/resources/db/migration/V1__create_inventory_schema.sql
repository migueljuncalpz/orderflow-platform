CREATE TABLE inventory (
                           product_id VARCHAR(100) PRIMARY KEY,
                           available_quantity INTEGER NOT NULL,
                           reserved_quantity INTEGER NOT NULL DEFAULT 0,
                           version BIGINT NOT NULL DEFAULT 0,

                           CONSTRAINT chk_inventory_available
                               CHECK (available_quantity >= 0),

                           CONSTRAINT chk_inventory_reserved
                               CHECK (reserved_quantity >= 0)
);

CREATE TABLE stock_movement (
                                movement_id UUID PRIMARY KEY,
                                product_id VARCHAR(100) NOT NULL,
                                movement_type VARCHAR(30) NOT NULL,
                                quantity INTEGER NOT NULL,
                                movement_status VARCHAR(30) NOT NULL,
                                created_at TIMESTAMP WITH TIME ZONE NOT NULL,

                                CONSTRAINT chk_stock_movement_quantity
                                    CHECK (quantity > 0)
);

CREATE TABLE processed_event (
                                 event_id UUID PRIMARY KEY,
                                 processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_stock_movement_product_created
    ON stock_movement(product_id, created_at);

CREATE INDEX idx_stock_movement_status
    ON stock_movement(movement_status);