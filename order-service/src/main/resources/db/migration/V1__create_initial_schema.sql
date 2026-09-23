CREATE TABLE stock_order (
                             order_id UUID PRIMARY KEY,
                             created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                             order_status VARCHAR(30) NOT NULL
);

CREATE TABLE stock_order_item (
                                  order_id UUID NOT NULL,
                                  product_id VARCHAR(100) NOT NULL,
                                  quantity INTEGER NOT NULL,

                                  CONSTRAINT fk_order_item_order
                                      FOREIGN KEY (order_id)
                                          REFERENCES stock_order(order_id),

                                  CONSTRAINT chk_order_item_quantity
                                      CHECK (quantity > 0)
);

CREATE TABLE processed_event (
                                 event_id UUID PRIMARY KEY,
                                 processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_stock_order_status_created
    ON stock_order(order_status, created_at);

CREATE INDEX idx_order_item_product
    ON stock_order_item(product_id);