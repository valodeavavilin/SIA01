----------------------------------------------------------------------------------
--- DS2_XLSX_SparkSQL_Views_from_REST.sql
--- XLS source: customers + customer_finance
----------------------------------------------------------------------------------

-- Test REST endpoints
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerFinanceView');

----------------------------------------------------------------------------------
-- CUSTOMER_VIEW
----------------------------------------------------------------------------------

-- 1. Create JSON View
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'CUSTOMER_JSON_VIEW',
               'http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerView');

SELECT * FROM CUSTOMER_JSON_VIEW;

-- 2. Create SQL View
CREATE OR REPLACE VIEW CUSTOMER_VIEW AS
SELECT v.*
FROM CUSTOMER_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM CUSTOMER_VIEW;

----------------------------------------------------------------------------------
-- CUSTOMER_FINANCE_VIEW
----------------------------------------------------------------------------------

-- 1. Create JSON View
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'CUSTOMER_FINANCE_JSON_VIEW',
               'http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerFinanceView');

SELECT * FROM CUSTOMER_FINANCE_JSON_VIEW;

-- 2. Create SQL View
CREATE OR REPLACE VIEW CUSTOMER_FINANCE_VIEW AS
SELECT v.*
FROM CUSTOMER_FINANCE_JSON_VIEW AS json_view
LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM CUSTOMER_FINANCE_VIEW;