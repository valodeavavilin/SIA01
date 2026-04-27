----------------------------------------------------------------------------------
--- DS3_MongoDB_SparkSQL_Views.sql
--- DATASET: MongoDB transactions + customer risk
----------------------------------------------------------------------------------


----------------------------------------------------------------------------------
-- 1. MONGO TRANSACTIONS VIEW
----------------------------------------------------------------------------------

-- 1. Get Data Source JSON document
SELECT java_method(
    'org.spark.service.rest.QueryRESTDataService',
    'getRESTDataDocument',
    'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/mongodb/MongoTransactionView'
);

-- 2. Check JSON schema example
SELECT schema_of_json('[
  {
    "txn_id":7475327,
    "ts":"2010-01-01 00:01:00",
    "client_id":1556,
    "card_id":2972,
    "amount":-77.0,
    "channel":"Swipe Transaction",
    "merchant":{
      "id":59935,
      "city":"Beulah",
      "state":"ND",
      "zip":"58523.0"
    },
    "mcc":5499,
    "errors":""
  }
]');

-- 3. Create Remote View
-- DROP VIEW transactions_mongo_view;

CREATE OR REPLACE VIEW transactions_mongo_view AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<amount: DOUBLE, card_id: BIGINT, channel: STRING, client_id: BIGINT, errors: STRING, mcc: BIGINT, merchant: STRUCT<city: STRING, id: BIGINT, state: STRING, zip: STRING>, ts: STRING, txn_id: BIGINT>>'
    ) array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/mongodb/MongoTransactionView'
        ) AS data
    ) json_raw
)
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
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

-- 4. Test Remote View
SELECT *
FROM transactions_mongo_view;



----------------------------------------------------------------------------------
-- 2. CUSTOMER RISK VIEW
----------------------------------------------------------------------------------

-- 1. Get Data Source JSON document
SELECT java_method(
    'org.spark.service.rest.QueryRESTDataService',
    'getRESTDataDocument',
    'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/mongodb/CustomerRiskView'
);

-- 2. Check JSON schema example
SELECT schema_of_json('[
  {
    "client_id":1556,
    "credit_score":720,
    "yearly_income":"$50000",
    "total_debt":"$12000",
    "num_credit_cards":3,
    "tx_count":25.0,
    "total_amount":1500.75,
    "top_mcc":5499.0,
    "cards":[
      {
        "card_id":2972,
        "card_brand":"Visa",
        "card_type":"Debit",
        "has_chip":"YES",
        "credit_limit":"$5000"
      }
    ]
  }
]');

-- 3. Create Remote View
-- DROP VIEW customer_risk_mongo_view;

CREATE OR REPLACE VIEW customer_risk_mongo_view AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<cards: ARRAY<STRUCT<card_brand: STRING, card_id: BIGINT, card_type: STRING, credit_limit: STRING, has_chip: STRING>>, client_id: BIGINT, credit_score: BIGINT, num_credit_cards: BIGINT, top_mcc: DOUBLE, total_amount: DOUBLE, total_debt: STRING, tx_count: DOUBLE, yearly_income: STRING>>'
    ) array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8093/DSA-NoSQL-MongoDBService/rest/mongodb/CustomerRiskView'
        ) AS data
    ) json_raw
)
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
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

-- 4. Test Remote View
SELECT *
FROM customer_risk_mongo_view;



----------------------------------------------------------------------------------
-- 3. CUSTOMER RISK CARDS VIEW - nested cards flattened
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
FROM customer_risk_mongo_view r
LATERAL VIEW explode(r.cards) AS card;

-- Test Remote View
SELECT *
FROM customer_risk_cards_mongo_view;



----------------------------------------------------------------------------------
-- 4. TRANSACTIONS + CUSTOMER RISK INTEGRATION VIEW
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
FROM transactions_mongo_view t
LEFT JOIN customer_risk_mongo_view r
    ON t.client_id = r.client_id;

-- Test Integration View
SELECT *
FROM transactions_customer_risk_mongo_view;