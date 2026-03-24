DROP TABLE IF EXISTS payments.stg_cards;
DROP TABLE IF EXISTS payments.stg_card_limits;
DROP TABLE IF EXISTS payments.stg_card_security;
DROP TABLE IF EXISTS payments.stg_merchants;
DROP TABLE IF EXISTS payments.stg_transactions;

CREATE TABLE payments.stg_cards (
    card_id TEXT,
    client_id TEXT,
    card_brand TEXT,
    card_type TEXT,
    expires TEXT,
    acct_open_date TEXT,
    num_cards_issued TEXT
);

CREATE TABLE payments.stg_card_limits (
    card_id TEXT,
    credit_limit TEXT
);

CREATE TABLE payments.stg_card_security (
    card_id TEXT,
    has_chip TEXT,
    year_pin_last_changed TEXT,
    card_on_dark_web TEXT
);

CREATE TABLE payments.stg_merchants (
    merchant_id TEXT,
    merchant_city TEXT,
    merchant_state TEXT,
    zip TEXT
);

CREATE TABLE payments.stg_transactions (
    txn_id TEXT,
    date TEXT,
    client_id TEXT,
    card_id TEXT,
    amount TEXT,
    use_chip TEXT,
    merchant_id TEXT,
    mcc TEXT,
    errors TEXT
);