CREATE TABLE cake_table.addresses
(
    address_id    INT AUTO_INCREMENT     NOT NULL,
    user_id       INT                    NOT NULL,
    name          VARCHAR(50)            NOT NULL,
    receiver_name VARCHAR(50)            NOT NULL,
    phone         VARCHAR(20)            NOT NULL,
    address       VARCHAR(200)           NOT NULL,
    latitude      DECIMAL(10, 8)         NULL,
    longitude     DECIMAL(11, 8)         NULL,
    is_default    BIT(1)   DEFAULT 0     NULL,
    created_at    datetime DEFAULT NOW() NULL,
    CONSTRAINT pk_addresses PRIMARY KEY (address_id)
);

ALTER TABLE cake_table.addresses
    ADD CONSTRAINT FK_ADDRESSES_ON_USER FOREIGN KEY (user_id) REFERENCES cake_table.users (user_id) ON DELETE CASCADE;