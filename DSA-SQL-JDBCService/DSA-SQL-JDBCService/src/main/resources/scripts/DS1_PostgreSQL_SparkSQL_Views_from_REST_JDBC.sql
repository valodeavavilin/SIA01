--------------------------------------------------------------------------------
--- DS1_PostgreSQL_SparkSQL_Views_from_REST.sql
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

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'CARD_LIMITS_JSON_VIEW',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/CardLimitsView');

SELECT * FROM CARD_LIMITS_JSON_VIEW;

CREATE OR REPLACE VIEW card_limits_view AS
select v.*
FROM CARD_LIMITS_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM card_limits_view;

--------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'CARD_SECURITY_JSON_VIEW',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/CardSecurityView');

SELECT * FROM CARD_SECURITY_JSON_VIEW;

CREATE OR REPLACE VIEW card_security_view AS
select v.*
FROM CARD_SECURITY_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM card_security_view;

--------------------------------------------------------------------------------

SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'TRANSACTIONS_JSON_VIEW',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/payments/TransactionsView');

SELECT * FROM TRANSACTIONS_JSON_VIEW;

CREATE OR REPLACE VIEW transactions_view AS
select v.*
FROM TRANSACTIONS_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM transactions_view;

--------------------------------------------------------------------------------

-- With AUTHENTICATION examples
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'CARD_LIMITS_JSON_VIEW_AUTH',
               'http://developer:iis@localhost:8090/DSA-SQL-JDBCService/rest/payments/CardLimitsView');

SELECT * FROM CARD_LIMITS_JSON_VIEW_AUTH;

CREATE OR REPLACE VIEW card_limits_view_auth AS
select v.*
FROM CARD_LIMITS_JSON_VIEW_AUTH as json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM card_limits_view_auth;