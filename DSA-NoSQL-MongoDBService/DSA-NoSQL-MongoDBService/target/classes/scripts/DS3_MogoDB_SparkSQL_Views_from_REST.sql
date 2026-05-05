----------------------------------------------------------------------------------
--- DS3_MongoDB_SparkSQL_Views_from_REST.sql
--- DATASET: MongoDB REST -> SparkSQL JSON Views
----------------------------------------------------------------------------------


----------------------------------------------------------------------------------
-- 1. Create JSON View: Mongo Transactions
----------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'TRANSACTIONS_MONGO_JSON_VIEW',
    'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/mongodb/MongoTransactionView'
);

SELECT *
FROM TRANSACTIONS_MONGO_JSON_VIEW;


-- 2. Create Remote View: transactions_mongo_view
-- DROP VIEW transactions_mongo_view;

CREATE OR REPLACE VIEW transactions_mongo_view AS
SELECT
    v.txn_id              AS txn_id,
    v.ts                  AS txn_timestamp,
    v.client_id           AS client_id,
    v.card_id             AS card_id,
    v.amount              AS amount,
    v.channel             AS channel,
    v.mcc                 AS mcc,
    v.errors              AS errors,
    v.merchant.id         AS merchant_id,
    v.merchant.city       AS merchant_city,
    v.merchant.state      AS merchant_state,
    v.merchant.zip        AS merchant_zip
FROM TRANSACTIONS_MONGO_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;


-- 3. Test Remote View
SELECT *
FROM transactions_mongo_view;



----------------------------------------------------------------------------------
-- 2. Create JSON View: Customer Risk
----------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'CUSTOMER_RISK_MONGO_JSON_VIEW',
    'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/mongodb/CustomerRiskView'
);

SELECT *
FROM CUSTOMER_RISK_MONGO_JSON_VIEW;


-- 2. Create Remote View: customer_risk_mongo_view
-- DROP VIEW customer_risk_mongo_view;

CREATE OR REPLACE VIEW customer_risk_mongo_view AS
SELECT
    v.client_id          AS client_id,
    v.credit_score      AS credit_score,
    v.yearly_income     AS yearly_income,
    v.total_debt        AS total_debt,
    v.num_credit_cards  AS num_credit_cards,
    v.tx_count          AS tx_count,
    v.total_amount      AS total_amount,
    v.top_mcc           AS top_mcc,
    v.cards             AS cards
FROM CUSTOMER_RISK_MONGO_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;


-- 3. Test Remote View
SELECT *
FROM customer_risk_mongo_view;



----------------------------------------------------------------------------------
-- 3. Create Remote View: Customer Risk Cards Flattened
----------------------------------------------------------------------------------

-- DROP VIEW customer_risk_cards_mongo_view;

CREATE OR REPLACE VIEW customer_risk_cards_mongo_view AS
SELECT
    r.client_id          AS client_id,
    card.card_id         AS card_id,
    card.card_brand      AS card_brand,
    card.card_type       AS card_type,
    card.has_chip        AS has_chip,
    card.credit_limit    AS credit_limit
FROM customer_risk_mongo_view AS r
LATERAL VIEW explode(r.cards) AS card;


-- Test Remote View
SELECT *
FROM customer_risk_cards_mongo_view;



----------------------------------------------------------------------------------
-- 4. Create Remote View: Transactions + Customer Risk
----------------------------------------------------------------------------------

-- DROP VIEW transactions_customer_risk_mongo_view;

CREATE OR REPLACE VIEW transactions_customer_risk_mongo_view AS
SELECT
    t.txn_id,
    t.txn_timestamp,
    t.client_id,
    t.card_id,
    t.amount,
    t.channel,
    t.mcc,
    t.errors,
    t.merchant_id,
    t.merchant_city,
    t.merchant_state,
    t.merchant_zip,
    r.credit_score,
    r.yearly_income,
    r.total_debt,
    r.num_credit_cards,
    r.tx_count,
    r.total_amount AS customer_total_amount,
    r.top_mcc
FROM transactions_mongo_view AS t
LEFT JOIN customer_risk_mongo_view AS r
    ON t.client_id = r.client_id;


-- Test Integration View
SELECT *
FROM transactions_customer_risk_mongo_view;