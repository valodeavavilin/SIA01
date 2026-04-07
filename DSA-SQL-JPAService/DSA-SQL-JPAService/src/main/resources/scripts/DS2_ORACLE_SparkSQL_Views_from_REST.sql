--------------------------------------------------------------------------------
--- DS2_PostgreSQL_JPA_SparkSQL_Views_from_REST.sql
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

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'CARD_JPA_JSON_VIEW',
               'http://localhost:8091/DSA_SQL_JPAService/rest/cards/CardJpaView');

SELECT * FROM CARD_JPA_JSON_VIEW;

CREATE OR REPLACE VIEW cards_jpa_view AS
select v.*
FROM CARD_JPA_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM cards_jpa_view;

--------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'MERCHANT_JPA_JSON_VIEW',
               'http://localhost:8091/DSA_SQL_JPAService/rest/cards/MerchantJpaView');

SELECT * FROM MERCHANT_JPA_JSON_VIEW;

CREATE OR REPLACE VIEW merchants_jpa_view AS
select v.*
FROM MERCHANT_JPA_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM merchants_jpa_view;

--------------------------------------------------------------------------------

-- With AUTHENTICATION examples
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'CARD_JPA_JSON_VIEW_AUTH',
               'http://developer:iis@localhost:8091/DSA_SQL_JPAService/rest/cards/CardJpaView');

SELECT * FROM CARD_JPA_JSON_VIEW_AUTH;

CREATE OR REPLACE VIEW cards_jpa_view_auth AS
select v.*
FROM CARD_JPA_JSON_VIEW_AUTH as json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM cards_jpa_view_auth;