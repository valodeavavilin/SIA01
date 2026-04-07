INSERT INTO payments.cards
SELECT
    CAST(card_id AS INTEGER),
    CAST(client_id AS INTEGER),
    card_brand,
    card_type,
    TO_DATE(expires, 'MM/YYYY'),
    TO_DATE(acct_open_date, 'MM/YYYY'),
    CAST(num_cards_issued AS INTEGER)
FROM payments.stg_cards;

INSERT INTO payments.merchants
SELECT
    CAST(merchant_id AS INTEGER),
    merchant_city,
    merchant_state,
    zip
FROM payments.stg_merchants;

INSERT INTO payments.card_limits
SELECT
    CAST(card_id AS INTEGER),
    CAST(
        REPLACE(REPLACE(credit_limit, '$',''), ',', '')
        AS NUMERIC(12,2)
    )
FROM payments.stg_card_limits;

INSERT INTO payments.card_security
SELECT
    CAST(card_id AS INTEGER),
    CASE WHEN UPPER(has_chip) = 'YES' THEN TRUE ELSE FALSE END,
    CAST(year_pin_last_changed AS INTEGER),
    CASE WHEN UPPER(card_on_dark_web) = 'YES' THEN TRUE ELSE FALSE END
FROM payments.stg_card_security;

INSERT INTO payments.transactions_clean
SELECT
    CAST(txn_id AS BIGINT),
    CAST(date AS TIMESTAMP),
    CAST(client_id AS INTEGER),
    CAST(card_id AS INTEGER),
    CAST(amount AS NUMERIC(12,2)),
    use_chip,
    CAST(merchant_id AS INTEGER),
    CAST(mcc AS INTEGER),
    errors
FROM payments.stg_transactions;
