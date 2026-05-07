--------------------------------------------------------------------------------
--- DS1_PostgreSQL_JPA_SparkSQL_Views_from_REST.sql
--- Updated for combined JDBC + JPA PostgreSQL Service
--------------------------------------------------------------------------------

-- Vizualizare date brute din serviciul REST (Test Conexiune)
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/cards/CardJpaView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/cards/MerchantJpaView');

--------------------------------------------------------------------------------
--- JPA Data Source Access Model -----------------------------------------------
--------------------------------------------------------------------------------

-- 1. Get Data Source JSON Schema for Cards
-- Se obține un exemplu de JSON pentru a genera structura SQL
SELECT schema_of_json('[{"cardId":1,"clientId":101,"cardBrand":"VISA","cardType":"CREDIT","expires":"2027-12-31","acctOpenDate":"2022-01-01","numCardsIssued":1}]');

-- 2. Create Remote View for Cards
-- Definirea manuală a schemei (ARRAY<STRUCT<...>>) previne erorile de sintaxă
CREATE OR REPLACE VIEW cards_jpa_view AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<cardId: BIGINT, clientId: BIGINT, cardBrand: STRING, cardType: STRING, expires: STRING, acctOpenDate: STRING, numCardsIssued: BIGINT>>'
    ) AS array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/cards/CardJpaView'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View Cards
SELECT * FROM cards_jpa_view;

--------------------------------------------------------------------------------

-- 1. Get Data Source JSON Schema for Merchants
-- Necesar pentru a asigura maparea corectă a tipurilor de date
SELECT schema_of_json('[{"merchantId":10,"merchantCity":"New York","merchantState":"NY","zip":"10001"}]');

-- 2. Create Remote View for Merchants
-- Această metodă este robustă împotriva caracterelor speciale din JSON
CREATE OR REPLACE VIEW merchants_jpa_view AS
WITH json_view AS (
    SELECT from_json(
        json_raw.data,
        'ARRAY<STRUCT<merchantId: BIGINT, merchantCity: STRING, merchantState: STRING, zip: STRING>>'
    ) AS array
    FROM (
        SELECT java_method(
            'org.spark.service.rest.QueryRESTDataService',
            'getRESTDataDocument',
            'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/cards/MerchantJpaView'
        ) AS data
    ) json_raw
)
SELECT v.*
FROM json_view
LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View Merchants
SELECT * FROM merchants_jpa_view;