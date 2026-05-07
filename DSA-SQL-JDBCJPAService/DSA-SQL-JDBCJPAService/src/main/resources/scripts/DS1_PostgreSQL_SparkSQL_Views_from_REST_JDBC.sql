--------------------------------------------------------------------------------
--- DS1_PostgreSQL_SparkSQL_Views_from_REST.sql
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

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'CARD_LIMITS_JSON_VIEW',
               'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/CardLimitsView');

SELECT * FROM CARD_LIMITS_JSON_VIEW;

CREATE OR REPLACE VIEW card_limits_view AS
SELECT v.*
FROM CARD_LIMITS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM card_limits_view;

--------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'CARD_SECURITY_JSON_VIEW',
               'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/CardSecurityView');

SELECT * FROM CARD_SECURITY_JSON_VIEW;

CREATE OR REPLACE VIEW card_security_view AS
SELECT v.*
FROM CARD_SECURITY_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM card_security_view;

--------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'TRANSACTIONS_JSON_VIEW',
               'http://localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/TransactionsView');

SELECT * FROM TRANSACTIONS_JSON_VIEW;

CREATE OR REPLACE VIEW transactions_view AS
SELECT v.*
FROM TRANSACTIONS_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM transactions_view;

--------------------------------------------------------------------------------
-- With AUTHENTICATION example
--------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'CARD_LIMITS_JSON_VIEW_AUTH',
               'http://developer:iis@localhost:8090/DSA-SQL-PostgreSQLService/rest/payments/CardLimitsView');

SELECT * FROM CARD_LIMITS_JSON_VIEW_AUTH;

CREATE OR REPLACE VIEW card_limits_view_auth AS
SELECT v.*
FROM CARD_LIMITS_JSON_VIEW_AUTH AS json_view
LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM card_limits_view_auth;