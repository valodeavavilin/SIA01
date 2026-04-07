SHOW CON_NAME;

CREATE OR REPLACE VIEW FDBO.CARDS_V AS
SELECT
    "card_id",
    "client_id",
    "card_brand",
    "card_type",
    "expires",
    "acct_open_date",
    "num_cards_issued"
FROM "cards"@PG;

CREATE OR REPLACE VIEW FDBO.TRANSACTIONS_V AS
SELECT
  "txn_id"      AS txn_id,
  "date"        AS txn_date,
  "client_id"   AS client_id,
  "card_id"     AS card_id,
  "amount"      AS amount,
  "use_chip"    AS use_chip,
  "merchant_id" AS merchant_id,
  "mcc"         AS mcc,
  "errors"      AS errors
FROM "transactions_clean"@PG;

CREATE OR REPLACE VIEW FDBO.MERCHANTS_V AS
SELECT
    "merchant_id",
    "merchant_city",
    "merchant_state",
    "zip"
FROM "merchants"@PG;

CREATE OR REPLACE VIEW FDBO.CARD_LIMITS_V AS
SELECT
    "card_id",
    "credit_limit"
FROM "card_limits"@PG;

CREATE OR REPLACE VIEW FDBO.CARD_SECURITY_V AS
SELECT
    "card_id",
    "has_chip",
    "year_pin_last_changed",
    "card_on_dark_web"
FROM "card_security"@PG;

SELECT * FROM FDBO.CARDS_V;
SELECT * FROM FDBO.TRANSACTIONS_V;
select * FROM FDBO.MERCHANTS_V;
select * FROM FDBO.CARD_LIMITS_V;
select * FROM FDBO.CARD_SECURITY_V;


----------------- DUPA CREAREA TABELLEOR
CREATE OR REPLACE VIEW FDBO.CUSTOMERS_V AS
SELECT
  client_id, current_age, retirement_age, birth_year, birth_month,
  gender, address, latitude, longitude
FROM CRM.CUSTOMERS;

CREATE OR REPLACE VIEW FDBO.CUSTOMER_FINANCE_V AS
SELECT
  client_id, per_capita_income, yearly_income, total_debt,
  credit_score, num_credit_cards
FROM CRM.CUSTOMER_FINANCE;

DESC FDBO.TRANSACTIONS_V;
---- FEDERATED QUERY
SELECT
  c.client_id,
  c.gender,
  f.credit_score,
  COUNT(t.txn_id) AS txn_count,
  SUM(t.amount)   AS total_amount
FROM FDBO.CUSTOMERS_V c
JOIN FDBO.CUSTOMER_FINANCE_V f
  ON f.client_id = c.client_id
JOIN FDBO.TRANSACTIONS_V t
  ON t.client_id = c.client_id
GROUP BY c.client_id, c.gender, f.credit_score
ORDER BY total_amount DESC;

SELECT t.client_id
FROM FDBO.TRANSACTIONS_V t
FETCH FIRST 1 ROWS ONLY;

select count(*) from FDBO.TRANSACTIONS_V
SELECT
  c.client_id,
  c.gender,
  f.credit_score,
  COUNT(t.txn_id) AS txn_count,
  SUM(t.amount)   AS total_amount
FROM FDBO.CUSTOMERS_V c
JOIN FDBO.CUSTOMER_FINANCE_V f
  ON f.client_id = c.client_id
JOIN (
  SELECT txn_id, client_id, amount
  FROM FDBO.TRANSACTIONS_V
  WHERE ROWNUM <= 50000
) t
  ON t.client_id = c.client_id
GROUP BY c.client_id, c.gender, f.credit_score
ORDER BY total_amount DESC
FETCH FIRST 50 ROWS ONLY;