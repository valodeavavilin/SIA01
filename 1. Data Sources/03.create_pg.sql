DROP TABLE IF EXISTS payments.transactions_clean;
DROP TABLE IF EXISTS payments.card_security;
DROP TABLE IF EXISTS payments.card_limits;
DROP TABLE IF EXISTS payments.merchants;
DROP TABLE IF EXISTS payments.cards;

CREATE TABLE payments.cards (
    card_id INTEGER PRIMARY KEY,
    client_id INTEGER NOT NULL,
    card_brand VARCHAR(20),
    card_type VARCHAR(30),
    expires DATE,
    acct_open_date DATE,
    num_cards_issued INTEGER
);

CREATE TABLE payments.merchants (
    merchant_id INTEGER PRIMARY KEY,
    merchant_city VARCHAR(100),
    merchant_state VARCHAR(50),
    zip VARCHAR(50)
);

CREATE TABLE payments.card_limits (
    card_id INTEGER PRIMARY KEY,
    credit_limit NUMERIC(12,2),
    CONSTRAINT fk_limits_card
        FOREIGN KEY (card_id)
        REFERENCES payments.cards(card_id)
);

CREATE TABLE payments.card_security (
    card_id INTEGER PRIMARY KEY,
    has_chip BOOLEAN,
    year_pin_last_changed INTEGER,
    card_on_dark_web BOOLEAN,
    CONSTRAINT fk_security_card
        FOREIGN KEY (card_id)
        REFERENCES payments.cards(card_id)
);

CREATE TABLE payments.transactions_clean (
    txn_id BIGINT PRIMARY KEY,
    date TIMESTAMP,
    client_id INTEGER,
    card_id INTEGER,
    amount NUMERIC(12,2),
    use_chip VARCHAR(30),
    merchant_id INTEGER,
    mcc INTEGER,
    errors VARCHAR(255),
    CONSTRAINT fk_tx_card
        FOREIGN KEY (card_id)
        REFERENCES payments.cards(card_id),
    CONSTRAINT fk_tx_merchant
        FOREIGN KEY (merchant_id)
        REFERENCES payments.merchants(merchant_id)
);