----------------------------------------------------------------------------------
--- DS2_XLSX_SparkSQL_Views.sql
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

-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerView');

SELECT schema_of_json('[
  {
    "clientId":825,
    "currentAge":53,
    "retirementAge":66,
    "birthYear":1971,
    "birthMonth":10,
    "gender":"Female",
    "address":"123 Main Street",
    "latitude":41.12345,
    "longitude":-87.54321
  }
]');

-- 2. Create Remote View
CREATE OR REPLACE VIEW CUSTOMER_VIEW AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
                     'ARRAY<STRUCT<address: STRING, birthMonth: BIGINT, birthYear: BIGINT, clientId: BIGINT, currentAge: BIGINT, gender: STRING, latitude: DOUBLE, longitude: DOUBLE, retirementAge: BIGINT>>') array
    FROM (SELECT java_method(
        'org.spark.service.rest.QueryRESTDataService',
        'getRESTDataDocument',
        'http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerView'
    ) as data) json_raw
)
SELECT v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM CUSTOMER_VIEW;

----------------------------------------------------------------------------------
-- CUSTOMER_FINANCE_VIEW
----------------------------------------------------------------------------------

-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerFinanceView');

SELECT schema_of_json('[
  {
    "clientId":825,
    "perCapitaIncome":29278.0,
    "yearlyIncome":59696.0,
    "totalDebt":127613.0,
    "creditScore":787,
    "numCreditCards":5
  }
]');

-- 2. Create Remote View
CREATE OR REPLACE VIEW CUSTOMER_FINANCE_VIEW AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
                     'ARRAY<STRUCT<clientId: BIGINT, creditScore: BIGINT, numCreditCards: BIGINT, perCapitaIncome: DOUBLE, totalDebt: DOUBLE, yearlyIncome: DOUBLE>>') array
    FROM (SELECT java_method(
        'org.spark.service.rest.QueryRESTDataService',
        'getRESTDataDocument',
        'http://localhost:8094/DSA-DOC-XLSService/rest/customers/CustomerFinanceView'
    ) as data) json_raw
)
SELECT v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
SELECT * FROM CUSTOMER_FINANCE_VIEW;