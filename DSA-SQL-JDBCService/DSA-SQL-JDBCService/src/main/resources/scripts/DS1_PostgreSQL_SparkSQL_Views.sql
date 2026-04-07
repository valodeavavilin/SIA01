--------------------------------------------------------------------------------
--- DS1_PostgreSQL_SparkSQL_Views.sql
--------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/CardLimitsView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/CardSecurityView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/TransactionsView');

--------------------------------------------------------------------------------
--- JDBC Data Source Access Model ----------------------------------------------
--------------------------------------------------------------------------------

-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/CardLimitsView');

SELECT schema_of_json('[{"cardId":1,"creditLimit":5000.00}]');

-- 2. Create Remote View
CREATE OR REPLACE VIEW card_limits_view AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
                     'ARRAY<STRUCT<cardId: BIGINT, creditLimit: DOUBLE>>') array
    FROM (SELECT java_method(
        'org.spark.service.rest.QueryRESTDataService',
        'getRESTDataDocument',
        'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/CardLimitsView'
    ) as data) json_raw
)
select v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM card_limits_view;

--------------------------------------------------------------------------------

-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/CardSecurityView');

SELECT schema_of_json('[{"cardId":1,"hasChip":true,"yearPinLastChanged":2023,"cardOnDarkWeb":false}]');

-- 2. Create Remote View
CREATE OR REPLACE VIEW card_security_view AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
                     'ARRAY<STRUCT<cardId: BIGINT, hasChip: BOOLEAN, yearPinLastChanged: BIGINT, cardOnDarkWeb: BOOLEAN>>') array
    FROM (SELECT java_method(
        'org.spark.service.rest.QueryRESTDataService',
        'getRESTDataDocument',
        'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/CardSecurityView'
    ) as data) json_raw
)
select v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM card_security_view;

--------------------------------------------------------------------------------

-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/TransactionsView');

SELECT schema_of_json('[{"txnId":1,"txnDate":"2024-01-01 10:00:00.0","clientId":101,"cardId":1,"amount":120.50,"useChip":"YES","merchantId":10,"mcc":5411,"errors":null}]');

-- 2. Create Remote View
CREATE OR REPLACE VIEW transactions_view AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
                     'ARRAY<STRUCT<txnId: BIGINT, txnDate: STRING, clientId: BIGINT, cardId: BIGINT, amount: DOUBLE, useChip: STRING, merchantId: BIGINT, mcc: BIGINT, errors: STRING>>') array
    FROM (SELECT java_method(
        'org.spark.service.rest.QueryRESTDataService',
        'getRESTDataDocument',
        'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/TransactionsView'
    ) as data) json_raw
)
select v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM transactions_view;