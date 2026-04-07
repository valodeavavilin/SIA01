----------------------------------------------------------------------------------
--- DS3_XML_SparkSQL_Views.sql
----------------------------------------------------------------------------------
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8092/DSA-DOC-XMLService/rest/locations/DepartamentCityView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8092/DSA-DOC-XMLService/rest/locations/DepartamentView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8092/DSA-DOC-XMLService/rest/locations/CityView');
----------------------------------------------------------------------------------
-- 1. Create JSON View
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'DEPARTAMENTS_JSON_VIEW',
               'http://localhost:8092/DSA-DOC-XMLService/rest/locations/DepartamentView');

SELECT * FROM DEPARTAMENTS_JSON_VIEW;

-- 2. Create Remote View
-- DROP VIEW departaments_view;
CREATE OR REPLACE VIEW departaments_view AS
select v.idDepartament, v.departamentName, v.departamentCode, v.countryName
FROM DEPARTAMENTS_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;
-- 3. Test Remote View
select * FROM departaments_view;

----------------------------------------------------------------------------------
-- 1. Create JSON View
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'CITIES_JSON_VIEW',
               'http://localhost:8092/DSA-DOC-XMLService/rest/locations/CityView');

SELECT * FROM CITIES_JSON_VIEW;

-- 2. Create Remote View
-- DROP VIEW cities_view;
CREATE OR REPLACE VIEW cities_view AS
select v.*
FROM CITIES_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;
-- 3. Test Remote View
select * FROM cities_view;

----------------------------------------------------------------------------------
-- DROP VIEW departaments_cities_view;
CREATE OR REPLACE VIEW departaments_cities_view AS
select v.idDepartament, explode(v.cities.idCity) as idCity
FROM DEPARTAMENTS_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;
-- 3. Test Remote View
select * FROM departaments_cities_view;
-- DROP VIEW departaments_cities_view_all;
CREATE OR REPLACE VIEW departaments_cities_view_all AS
select v.idDepartament, v.departamentName, v.departamentCode, v.countryName, inline(v.cities)
FROM DEPARTAMENTS_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;
-- 3. Test Remote View
select * FROM departaments_cities_view_all;





