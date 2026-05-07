--------------------------------------------------------------------------------
--- DS1_PostgreSQL_SparkSQL_Views.sql
--- Updated for combined JDBC + JPA PostgreSQL Service
--------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/CardLimitsView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/CardSecurityView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/TransactionsView');

--------------------------------------------------------------------------------
--- JDBC Data Source Access Model ----------------------------------------------
--------------------------------------------------------------------------------

-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/CardLimitsView');

SELECT schema_of_json('[{"cardId":1,"creditLimit":5000.00}]');

-- 2. Create Remote View
CREATE OR REPLACE VIEW card_limits_view AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<cardId: BIGINT, creditLimit: DOUBLE>>'
    ) AS array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/CardLimitsView'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM card_limits_view;

--------------------------------------------------------------------------------

-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/CardSecurityView');

SELECT schema_of_json('[{"cardId":1,"hasChip":true,"yearPinLastChanged":2023,"cardOnDarkWeb":false}]');

-- 2. Create Remote View
CREATE OR REPLACE VIEW card_security_view AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<cardId: BIGINT, hasChip: BOOLEAN, yearPinLastChanged: BIGINT, cardOnDarkWeb: BOOLEAN>>'
    ) AS array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/CardSecurityView'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM card_security_view;

--------------------------------------------------------------------------------

-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/TransactionsView');

SELECT schema_of_json('[{"txnId":1,"txnDate":"2024-01-01 10:00:00.0","clientId":101,"cardId":1,"amount":120.50,"useChip":"YES","merchantId":10,"mcc":5411,"errors":null}]');

-- 2. Create Remote View
CREATE OR REPLACE VIEW transactions_view AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<amount: DOUBLE, cardId: BIGINT, clientId: BIGINT, errors: STRING, mcc: BIGINT, merchantId: BIGINT, txnDate: STRING, txnId: BIGINT, useChip: STRING>>'
    ) AS array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/TransactionsView'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM transactions_view;