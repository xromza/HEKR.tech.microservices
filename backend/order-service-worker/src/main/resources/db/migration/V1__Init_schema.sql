CREATE TABLE orders (
    id text PRIMARY KEY,
    user_id bigint NOT NULL,
    warehouse_id bigint,
    price numeric(12,2) NOT NULL,
    address text NOT NULL,
    status varchar(32) NOT NULL,
    payment_method varchar(32) NOT NULL DEFAULT 'CASH',
    comment TEXT,
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_warehouse_id ON orders(warehouse_id);

CREATE TABLE order_items (
    order_id text NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    variant_id bigint NOT NULL,
    quantity bigint NOT NULL,
    price_at_purchase numeric(12,2) NOT NULL,
    total_price numeric(12,2) NOT NULL,
    PRIMARY KEY (order_id, variant_id)
);

CREATE TABLE order_status_history (
    id text PRIMARY KEY,
    order_id text NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    new_status varchar(32) NOT NULL,
    changed_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    changed_by bigint NOT NULL,
    comment TEXT NOT NULL
);
