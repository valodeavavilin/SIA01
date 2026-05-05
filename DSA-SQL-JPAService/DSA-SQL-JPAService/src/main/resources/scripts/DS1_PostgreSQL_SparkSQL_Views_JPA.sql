--------------------------------------------------------------------------------
--- DS1_PostgreSQL_JPA_SparkSQL_Views.sql
--------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8091/DSA_SQL_JPAService/rest/cards/CardJpaView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8091/DSA_SQL_JPAService/rest/cards/MerchantJpaView');

--------------------------------------------------------------------------------
--- JPA Data Source Access Model -----------------------------------------------
--------------------------------------------------------------------------------

-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8091/DSA_SQL_JPAService/rest/cards/CardJpaView');

SELECT schema_of_json('[{"cardId":1,"clientId":101,"cardBrand":"VISA","cardType":"CREDIT","expires":"2027-12-31","acctOpenDate":"2022-01-01","numCardsIssued":1}]');

-- 2. Create Remote View
CREATE OR REPLACE VIEW cards_jpa_view AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
                     'ARRAY<STRUCT<cardId: BIGINT, clientId: BIGINT, cardBrand: STRING, cardType: STRING, expires: STRING, acctOpenDate: STRING, numCardsIssued: BIGINT>>') array
    FROM (SELECT java_method(
        'org.spark.service.rest.QueryRESTDataService',
        'getRESTDataDocument',
        'http://localhost:8091/DSA_SQL_JPAService/rest/cards/CardJpaView'
    ) as data) json_raw
)
select v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM cards_jpa_view;

--------------------------------------------------------------------------------

-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8091/DSA_SQL_JPAService/rest/cards/MerchantJpaView');

SELECT schema_of_json('[{"merchantId":10,"merchantCity":"New York","merchantState":"NY","zip":"10001"}]');

-- 2. Create Remote View
CREATE OR REPLACE VIEW merchants_jpa_view AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
                     'ARRAY<STRUCT<merchantId: BIGINT, merchantCity: STRING, merchantState: STRING, zip: STRING>>') array
    FROM (SELECT java_method(
        'org.spark.service.rest.QueryRESTDataService',
        'getRESTDataDocument',
        'http://localhost:8091/DSA_SQL_JPAService/rest/cards/MerchantJpaView'
    ) as data) json_raw
)
select v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM merchants_jpa_view;