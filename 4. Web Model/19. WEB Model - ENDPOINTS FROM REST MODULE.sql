http://localhost:8080/ords/fdbo/olap/calendar
http://localhost:8080/ords/fdbo/olap/merchant-geo
http://localhost:8080/ords/fdbo/olap/credit-age
http://localhost:8080/ords/fdbo/olap/card-brand-type
http://localhost:8080/ords/fdbo/olap/source-channel
http://localhost:8080/ords/fdbo/olap/state-brand-cube
http://localhost:8080/ords/fdbo/olap/income-debt
http://localhost:8080/ords/fdbo/olap/card-security-cube
http://localhost:8080/ords/fdbo/olap/year-state-source-gsets
http://localhost:8080/ords/fdbo/olap/month-income-mcc-gsets

http://localhost:8080/ords/fdbo/olap/running-total-client
http://localhost:8080/ords/fdbo/olap/client-avg-diff
http://localhost:8080/ords/fdbo/olap/card-total-rank
http://localhost:8080/ords/fdbo/olap/month-share-running
http://localhost:8080/ords/fdbo/olap/client-first-last-top
http://localhost:8080/ords/fdbo/olap/merchant-state-rank
http://localhost:8080/ords/fdbo/olap/credit-month-performance

SELECT name, uri_prefix
FROM user_ords_modules
ORDER BY name;

http://localhost:8080/ords/fdbo/olap/fact-transactions