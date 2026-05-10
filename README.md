# IIS / Information Integration Project – SIA_01

Proiectul este organizat în două părți principale:

1. **P1 – FDBO: Oracle Federated Database / Oracle Integration Model**
2. **P2 – DSA Java4DI: Data Service Architecture cu Spring Boot, SparkSQL și Vaadin**

Ambele părți folosesc același studiu de caz și aceleași surse de date externe, dar implementează integrarea prin două arhitecturi diferite:

- în **P1**, integrarea este realizată în Oracle, prin schema FDBO, DB Link, ExcelTable, RESTHeart, ORDS și APEX;
- în **P2**, integrarea este realizată prin arhitectura Java4DI, bazată pe microservicii Spring Boot, SparkSQL, REST services și Vaadin UI.

---

# 1. Studiu de caz

Tema proiectului este integrarea și analiza datelor dintr-un sistem de plăți, folosind surse de date eterogene.

Sursele de date folosite sunt:

```text
DS_1 – Payments System
      PostgreSQL / CSV
      Date tranzacționale despre carduri, limite, securitate, comercianți și tranzacții

DS_2 – CRM Customers
      XLSX
      Date descriptive și financiare despre clienți

DS_3 – Risk / Fraud Analysis
      MongoDB / JSON
      Date documentare despre risc, tranzacții și comportament financiar
```

---

# 2. Surse de date

## 2.1 DS_1 – Payments System / PostgreSQL

Sursa DS_1 este o sursă relațională importată în PostgreSQL.

Structuri principale:

```text
cards
card_limits
card_security
merchants
transactions_clean
```

Conținut:

- `cards` – informații despre carduri: card ID, client ID, brand, tip card, dată expirare, dată deschidere cont;
- `card_limits` – limita de credit pentru fiecare card;
- `card_security` – informații de securitate: cip, anul ultimei modificări PIN, expunere pe dark web;
- `merchants` – comercianți, oraș, stat/regiune, cod poștal;
- `transactions_clean` – tranzacții, valoare, client, card, comerciant, MCC, erori.

Relații principale:

```text
cards.card_id = card_limits.card_id
cards.card_id = card_security.card_id
transactions_clean.card_id = cards.card_id
transactions_clean.merchant_id = merchants.merchant_id
```

---

## 2.2 DS_2 – CRM Customers / XLSX

Sursa DS_2 este un fișier Excel.

Fișier utilizat:

```text
crm.xlsx / ai_reporting_local.xlsx
```

Sheet-uri utilizate:

```text
customers
customer_finance
```

Structuri:

```text
customers
  client_id
  current_age
  retirement_age
  birth_year
  birth_month
  gender
  address
  latitude
  longitude

customer_finance
  client_id
  per_capita_income
  yearly_income
  total_debt
  credit_score
  num_credit_cards
```

Relația principală:

```text
customers.client_id = customer_finance.client_id
```

---

## 2.3 DS_3 – Risk / Fraud Analysis / MongoDB

Sursa DS_3 este o sursă documentară JSON încărcată în MongoDB.

Baza de date MongoDB:

```text
mds
```

Colecții:

```text
customer_risk_docs
transaction_docs
```

Structuri:

```text
customer_risk_docs
  client_id
  credit_score
  yearly_income
  total_debt
  num_credit_cards
  tx_count
  total_amount
  top_mcc
  cards

transaction_docs
  txn_id
  ts
  client_id
  card_id
  amount
  channel
  merchant
    id
    city
    state
    zip
  mcc
  errors
```

Corelări logice:

```text
customer_risk_docs.client_id ↔ transaction_docs.client_id
transaction_docs.card_id ↔ cards.card_id
transaction_docs.merchant.id ↔ merchants.merchant_id
```

---

# 3. P1 – FDBO / Oracle Federated Database

Prima parte a proiectului implementează un sistem federativ de integrare în Oracle, folosind schema **FDBO**.

Arhitectura generală P1:

```text
PostgreSQL / XLSX / MongoDB
        ↓
Oracle Access Layer
        ↓
FDBO Integration Views
        ↓
Dimensional and Fact Views
        ↓
OLAP and Window Analytical Views
        ↓
ORDS REST Services
        ↓
Oracle APEX Web UI
```

---

## 3.1 P1 – Access Model în Oracle

În P1, sursele externe sunt accesate din Oracle prin mecanisme diferite, în funcție de tipul sursei.

### DS_1 PostgreSQL – acces prin Oracle DB Link

Accesul la PostgreSQL se face prin:

```text
Oracle Heterogeneous Services
ODBC driver
Oracle Listener
Database Link în schema FDBO
```

DB Link:

```sql
CREATE DATABASE LINK PG
CONNECT TO "payments" IDENTIFIED BY "payments"
USING '(DESCRIPTION =
  (ADDRESS = (PROTOCOL = TCP)(HOST = localhost)(PORT = 1521))
  (CONNECT_DATA =
    (SID = PG)
  )
  (HS = OK)
)';
```

View-uri Oracle peste structurile PostgreSQL:

```text
CARDS_V
TRANSACTIONS_V
MERCHANTS_V
CARD_LIMITS_V
CARD_SECURITY_V
```

---

### DS_2 Excel – acces prin PL/SQL ExcelTable

Accesul la fișierul Excel se face prin:

```text
Oracle DIRECTORY
PL/SQL ExcelTable
```

Structuri Oracle locale:

```text
V_XLS_CUSTOMERS
V_XLS_CUSTOMER_FINANCE_RAW
V_XLS_CUSTOMER_FINANCE
```

`V_XLS_CUSTOMER_FINANCE` conține valori curățate și convertite numeric pentru:

```text
per_capita_income
yearly_income
total_debt
credit_score
num_credit_cards
```

---

### DS_3 MongoDB – acces prin RESTHeart + UTL_HTTP + JSON_TABLE

Accesul la MongoDB se face indirect:

```text
MongoDB
  ↓
RESTHeart
  ↓
HTTP request din Oracle prin UTL_HTTP
  ↓
JSON_TABLE
  ↓
Oracle views
```

Structuri Oracle locale:

```text
V_MONGO_CUSTOMER_RISK
V_MONGO_TRANSACTIONS
```

Endpointuri RESTHeart utilizate:

```text
http://localhost:8081/mds/customer_risk_docs
http://localhost:8081/mds/transaction_docs
```

---

# 4. P1 – Integration and Analytical Model

Modelul analitic Oracle este construit în schema **FDBO**.

---

## 4.1 Integration Views

Au fost create view-uri de consolidare pentru a integra sursele eterogene.

```text
INT_CUSTOMER_PROFILE_V
INT_CARD_PROFILE_V
INT_TRANSACTIONS_BASE_V
```

### INT_CUSTOMER_PROFILE_V

Integrează:

```text
V_XLS_CUSTOMERS
V_XLS_CUSTOMER_FINANCE
V_MONGO_CUSTOMER_RISK
```

Rol:

```text
Consolidează profilul clientului cu date descriptive, financiare și indicatori de risc.
```

---

### INT_CARD_PROFILE_V

Integrează:

```text
CARDS_V
CARD_LIMITS_V
CARD_SECURITY_V
```

Rol:

```text
Consolidează informațiile despre carduri, limite și securitate.
```

---

### INT_TRANSACTIONS_BASE_V

Integrează:

```text
TRANSACTIONS_V
V_MONGO_TRANSACTIONS
```

Rol:

```text
Unifică tranzacțiile din PostgreSQL și MongoDB într-o structură comună.
```

Include atributul:

```text
source_system = POSTGRES / MONGO
```

---

## 4.2 Dimensional Views

Au fost create 4 view-uri dimensionale:

```text
DIM_CLIENT_V
DIM_CARD_V
DIM_TIME_V
DIM_MERCHANT_GEO_V
```

### DIM_CLIENT_V

Conține date despre clienți și categorii derivate:

```text
age_group
credit_score_group
income_group
debt_group
```

---

### DIM_CARD_V

Conține date despre carduri și categorii derivate:

```text
credit_limit_group
chip_group
darkweb_group
```

---

### DIM_TIME_V

Permite analiza calendaristică:

```text
txn_year
txn_month
txn_day
txn_quarter
year_month
month_name
weekday_name
```

---

### DIM_MERCHANT_GEO_V

Permite analiza geografică:

```text
state_group
city_group
geo_hierarchy
```

---

## 4.3 Fact Views

Au fost create view-uri de fapte pentru analiza tranzacțiilor:

```text
FACT_TRANSACTIONS_V
FACT_TRANSACTIONS_ENRICHED_V
```

### FACT_TRANSACTIONS_V

View de fapte principal, conține tranzacțiile standardizate.

### FACT_TRANSACTIONS_ENRICHED_V

View de fapte extins, îmbogățit cu atribute din dimensiuni:

```text
client attributes
card attributes
merchant geography
time attributes
source_system
```

---

## 4.4 OLAP Analytical Views

Au fost definite view-uri analitice OLAP folosind operații precum:

```text
ROLLUP
CUBE
GROUPING SETS
SUM
COUNT
AVG
GROUPING()
```

View-uri OLAP:

```text
OLAP_VIEW_TXN_CALENDAR_V
OLAP_VIEW_TXN_MERCHANT_GEO_V
OLAP_VIEW_TXN_CREDIT_AGE_V
OLAP_VIEW_TXN_CARD_BRAND_TYPE_V
OLAP_VIEW_TXN_SOURCE_CHANNEL_V
OLAP_VIEW_TXN_STATE_BRAND_CUBE_V
OLAP_VIEW_TXN_INCOME_DEBT_V
OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V
OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V
OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V
```

---

## 4.5 Window Analytical Views

Au fost definite view-uri analitice cu funcții window:

```text
WV_TXN_RUNNING_TOTAL_CLIENT_V
WV_TXN_CLIENT_AVG_DIFF_V
WV_CARD_TOTAL_RANK_V
WV_TXN_MONTH_SHARE_RUNNING_V
WV_TXN_CLIENT_FIRST_LAST_TOP_V
WV_MERCHANT_STATE_RANK_V
WV_CREDIT_MONTH_PERFORMANCE_V
```

Funcții utilizate:

```text
SUM() OVER
AVG() OVER
RANK()
DENSE_RANK()
ROW_NUMBER()
FIRST_VALUE()
LAST_VALUE()
PARTITION BY
ORDER BY
```

---

# 5. P1 – REST Web Model / ORDS

Datele analitice Oracle au fost expuse prin Oracle REST Data Services.

Au fost folosite trei mecanisme:

```text
ORDS AutoREST
ORDS Custom REST Module
REST Enabled SQL
```

---

## 5.1 ORDS AutoREST

Schema FDBO a fost activată pentru REST:

```sql
BEGIN
  ORDS.ENABLE_SCHEMA(
    p_enabled => TRUE,
    p_schema => 'FDBO',
    p_url_mapping_type => 'BASE_PATH',
    p_url_mapping_pattern => 'fdbo',
    p_auto_rest_auth => FALSE
  );
  COMMIT;
END;
/
```

Exemple endpointuri AutoREST:

```text
http://localhost:8080/ords/fdbo/olap_view_txn_calendar_v/
http://localhost:8080/ords/fdbo/olap_view_txn_credit_age_v/
http://localhost:8080/ords/fdbo/olap_view_txn_source_channel_v/
http://localhost:8080/ords/fdbo/wv_card_total_rank_v/
http://localhost:8080/ords/fdbo/wv_month_share_running_v/
```

---

## 5.2 ORDS Custom REST Module

A fost definit un modul REST custom:

```text
fdbo.olap.api
```

Base path:

```text
/olap/
```

Exemple endpointuri custom:

```text
http://localhost:8080/ords/fdbo/olap/calendar
http://localhost:8080/ords/fdbo/olap/merchant-geo
http://localhost:8080/ords/fdbo/olap/credit-age
http://localhost:8080/ords/fdbo/olap/source-channel
http://localhost:8080/ords/fdbo/olap/card-security-cube
http://localhost:8080/ords/fdbo/olap/card-total-rank
http://localhost:8080/ords/fdbo/olap/month-share-running
http://localhost:8080/ords/fdbo/olap/merchant-state-rank
http://localhost:8080/ords/fdbo/olap/fact-transactions
```

---

## 5.3 REST Enabled SQL

A fost folosit și endpointul ORDS pentru execuție SQL prin HTTP:

```text
http://localhost:8080/ords/fdbo/_/sql
```

Exemplu:

```bash
curl -i -X POST --user FDBO:fdbo \
--data "select * from FACT_TRANSACTIONS_V" \
-H "Content-Type: application/sql" \
http://localhost:8080/ords/fdbo/_/sql
```

---

# 6. P1 – Oracle APEX Web UI

A fost creată și o aplicație Oracle APEX pentru vizualizarea datelor integrate și analitice.

Aplicația include:

```text
Dashboard
KPI cards
Interactive Reports
Interactive Grids
Charts
REST/API access
```

Exemple pagini/componente APEX:

```text
Dashboard Home
Data Explorer – Transactions
Client Analysis – Credit Score & Age
Geographic Analysis
Card Analysis
Source & Channel Analysis
Card Ranking Analysis
Merchant State Ranking Analysis
Card Security Cube Analysis
State and Card Brand Cube Analysis
```

Tipuri de componente:

```text
Cards
Reports
Charts
Interactive Reports
Interactive Grids
```

Exemple view-uri folosite în APEX:

```text
FACT_TRANSACTIONS_V
OLAP_VIEW_TXN_CREDIT_AGE_V
OLAP_VIEW_TXN_MERCHANT_GEO_V
OLAP_VIEW_TXN_CARD_BRAND_TYPE_V
OLAP_VIEW_TXN_SOURCE_CHANNEL_V
OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V
OLAP_VIEW_TXN_STATE_BRAND_CUBE_V
WV_CARD_TOTAL_RANK_V
WV_MERCHANT_STATE_RANK_V
WV_TXN_CLIENT_AVG_DIFF_V
WV_CREDIT_MONTH_PERFORMANCE_V
```

A fost definit și un modul REST direct în APEX:

```text
apex.api
/apex/transactions
```

Endpoint:

```text
http://localhost:8080/ords/fdbo/apex/transactions
```

---

# 7. P2 – Data Service Architecture / Java4DI

A doua parte a proiectului implementează aceeași logică de integrare folosind arhitectura **Java4DI**, bazată pe microservicii Spring Boot și SparkSQL.

Arhitectura generală P2:

```text
PostgreSQL / XLSX / MongoDB
        ↓
Spring Boot Access Microservices
        ↓
DSA-SparkSQL-Service
        ↓
SparkSQL Access Views
        ↓
Integration Views
        ↓
Dimensional and Fact Views
        ↓
OLAP / Window / Advanced Analytical Views
        ↓
DSA-WEB-RESTService
        ↓
DSA-WEB-VaadinService
```

---

# 8. P2 – Spring Boot Services

Repository-ul conține următoarele proiecte Spring Boot:

```text
DSA-SQL-PostgreSQLService
DSA-DOC-XLSService
DSA-NoSQL-MongoDBService
DSA-SparkSQL-Service
DSA-WEB-RESTService
DSA-WEB-VaadinService
```

Porturi:

```text
DSA-SQL-PostgreSQLService   8090
DSA-DOC-XLSService          8094
DSA-NoSQL-MongoDBService    8093
DSA-SparkSQL-Service        9990
DSA-WEB-RESTService         8096
DSA-WEB-VaadinService       9080
```

---

# 9. P2 – Access Model

## 9.1 DSA-SQL-PostgreSQLService

Expune datele PostgreSQL prin endpointuri REST.

Base URL:

```text
http://localhost:8090/DSA-SQL-PostgreSQLService/rest
```

Endpointuri principale:

```text
/payments/CardLimitsView
/payments/CardSecurityView
/payments/TransactionsView
/cards/CardJpaView
/cards/MerchantJpaView
```

---

## 9.2 DSA-DOC-XLSService

Expune datele din fișierul XLSX.

Base URL:

```text
http://localhost:8094/DSA-DOC-XLSService/rest
```

Fișier folosit:

```text
ai_reporting_local.xlsx
```

Endpointuri:

```text
/customers/CustomerView
/customers/CustomerFinanceView
```

---

## 9.3 DSA-NoSQL-MongoDBService

Expune datele MongoDB prin endpointuri REST.

Base URL:

```text
http://localhost:8093/DSA-NoSQL-MongoDBService/rest
```

Endpointuri:

```text
/mongodb/ping
/mongodb/MongoTransactionView
/mongodb/CustomerRiskView
```

Pentru MongoDB au fost aplicate limite în builders pentru a evita încărcarea excesivă a datelor în memorie.

---

# 10. P2 – SparkSQL Integration and Analytical Model

`DSA-SparkSQL-Service` este motorul de integrare și analiză.

Configurație principală:

```text
server.port=9990
server.servlet.context-path=/DSA-SparkSQL-Service
jdbc:hive2://localhost:10000/default
```

Endpointuri SparkSQL:

```text
http://localhost:9990/DSA-SparkSQL-Service/rest/ping
http://localhost:9990/DSA-SparkSQL-Service/rest/view/{VIEW_NAME}
http://localhost:9990/DSA-SparkSQL-Service/rest/STRUCT/{VIEW_NAME}
http://localhost:9990/DSA-SparkSQL-Service/_sqlrest/query
```

SparkSQL creează view-uri peste endpointurile Access Model folosind apeluri REST.

Exemplu:

```sql
SELECT java_method(
    'org.spark.service.rest.RESTEnabledSQLService',
    'createJSONViewFromREST',
    'JSON_VIEW_NAME',
    'http://localhost:PORT/.../endpoint'
);
```

Apoi datele JSON sunt transformate în view-uri SparkSQL prin `LATERAL VIEW explode`.

---

## 10.1 SparkSQL Access Views

View-uri Access Model create în SparkSQL:

```text
card_limits_view
card_security_view
transactions_view
cards_jpa_view
merchants_jpa_view
CUSTOMER_VIEW
CUSTOMER_FINANCE_VIEW
transactions_mongo_view
customer_risk_mongo_view
customer_risk_cards_mongo_view
transactions_customer_risk_mongo_view
```

---

## 10.2 SparkSQL Integration Views

View-uri de integrare:

```text
INT_CUSTOMER_PROFILE_V
INT_CARD_PROFILE_V
INT_TRANSACTIONS_BASE_V
```

Acestea reproduc în SparkSQL logica de integrare din P1.

---

## 10.3 SparkSQL Dimensional Views

View-uri dimensionale:

```text
DIM_CLIENT_V
DIM_CARD_V
DIM_TIME_V
DIM_MERCHANT_GEO_V
```

---

## 10.4 SparkSQL Fact Views

View-uri de fapte:

```text
FACT_TRANSACTIONS_V
FACT_TRANSACTIONS_ENRICHED_V
```

În UI a fost folosit în principal:

```text
FACT_TRANSACTIONS_ENRICHED_V
```

deoarece conține tranzacțiile îmbogățite cu atribute din dimensiuni.

---

## 10.5 SparkSQL OLAP Analytical Views

Au fost create 10 view-uri OLAP:

```text
OLAP_VIEW_TXN_CALENDAR_V
OLAP_VIEW_TXN_MERCHANT_GEO_V
OLAP_VIEW_TXN_CREDIT_AGE_V
OLAP_VIEW_TXN_CARD_BRAND_TYPE_V
OLAP_VIEW_TXN_SOURCE_CHANNEL_V
OLAP_VIEW_TXN_STATE_BRAND_CUBE_V
OLAP_VIEW_TXN_INCOME_DEBT_V
OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V
OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V
OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V
```

---

## 10.6 SparkSQL Window Analytical Views

Au fost create 7 view-uri window:

```text
WV_TXN_RUNNING_TOTAL_CLIENT_V
WV_TXN_CLIENT_AVG_DIFF_V
WV_CARD_TOTAL_RANK_V
WV_TXN_MONTH_SHARE_RUNNING_V
WV_TXN_CLIENT_FIRST_LAST_TOP_V
WV_MERCHANT_STATE_RANK_V
WV_CREDIT_MONTH_PERFORMANCE_V
```

---

## 10.7 SparkSQL Advanced Analytical Views

Au fost create 3 view-uri advanced:

```text
ADV_PIVOT_TXN_SOURCE_MONTH_V
ADV_TXN_AMOUNT_DISTRIBUTION_V
ADV_TXN_RISK_STATISTICS_V
```

Acestea implementează:

```text
pivot-style monthly source comparison
statistical amount distribution
risk and correlation statistics
```

---

# 11. P2 – DSA-WEB-RESTService

`DSA-WEB-RESTService` expune view-urile SparkSQL prin endpointuri REST.

Base URL:

```text
http://localhost:8096/DSA-WEB-RESTService/rest/OLAP
```

Tehnologii:

```text
Spring Boot
Spring Data JPA
Hive JDBC
SparkSQL / Hive virtual database
```

Pattern implementat:

```text
SparkSQL View
      ↓
Java @Entity
      ↓
Spring Data JPA Repository
      ↓
REST Controller
      ↓
JSON endpoint
```

---

## 11.1 Endpointuri Dimension

```text
/dimensional/client
/dimensional/card
/dimensional/time
/dimensional/merchant-geo
```

---

## 11.2 Endpointuri Fact

```text
/fact/transactions
/fact/transactions-enriched
```

---

## 11.3 Endpointuri OLAP Analytical

```text
/analytical/txn-calendar
/analytical/merchant-geo
/analytical/credit-age
/analytical/card-brand-type
/analytical/source-channel
/analytical/state-brand-cube
/analytical/income-debt
/analytical/card-security-cube
/analytical/year-state-source-gsets
/analytical/month-income-mcc-gsets
```

---

## 11.4 Endpointuri Window Analytical

```text
/window/running-total-client
/window/client-avg-diff
/window/card-total-rank
/window/month-share-running
/window/client-first-last-top
/window/merchant-state-rank
/window/credit-month-performance
```

---

## 11.5 Endpointuri Advanced Analytical

```text
/advanced/pivot-txn-source-month
/advanced/txn-amount-distribution
/advanced/txn-risk-statistics
```

---

# 12. P2 – DSA-WEB-VaadinService

`DSA-WEB-VaadinService` este interfața Web UI construită cu Vaadin.

URL aplicație:

```text
http://localhost:9080/DSA-WEB-VaadinService
```

Configurație backend în `application.properties`:

```properties
dsa.backend.base-url=http://localhost:8096/DSA-WEB-RESTService/rest/OLAP
```

Vaadin consumă endpointurile din `DSA-WEB-RESTService`, nu accesează direct SparkSQL.

Flux:

```text
Vaadin UI
   ↓
DSA-WEB-RESTService
   ↓
SparkSQL Analytical Views
```

---

## 12.1 Structura UI Vaadin

UI-ul conține:

```text
Dashboard
Dimension Data Grids
Fact Data Grid
OLAP Charts
Window Charts
Advanced Charts
```

În total, interfața Vaadin afișează:

```text
4 Dimension Views
1 Fact View
9 Analytical Chart Views
```

Total:

```text
14 view-uri afișate + pagina Dashboard
```

---

## 12.2 Dimension Views în Vaadin

```text
DIM_CLIENT_V
DIM_CARD_V
DIM_TIME_V
DIM_MERCHANT_GEO_V
```

Acestea sunt afișate ca tabele Vaadin Grid.

---

## 12.3 Fact View în Vaadin

```text
FACT_TRANSACTIONS_ENRICHED_V
```

Acest view este afișat ca tabel de date tranzacționale îmbogățite.

---

## 12.4 Analytical Views în Vaadin

### OLAP Charts

```text
OLAP_VIEW_TXN_SOURCE_CHANNEL_V
OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V
OLAP_VIEW_TXN_CREDIT_AGE_V
```

### Window Charts

```text
WV_TXN_MONTH_SHARE_RUNNING_V
WV_CARD_TOTAL_RANK_V
WV_MERCHANT_STATE_RANK_V
```

### Advanced Charts

```text
ADV_PIVOT_TXN_SOURCE_MONTH_V
ADV_TXN_AMOUNT_DISTRIBUTION_V
ADV_TXN_RISK_STATISTICS_V
```

Fiecare pagină de chart include:

```text
responsive chart
chart data grid
navigation through MainView
```

---

# 13. Ordine de pornire

Pentru ca întregul proiect P2 să funcționeze, serviciile trebuie pornite în următoarea ordine:

```text
1. PostgreSQL
2. MongoDB
3. DSA-SQL-PostgreSQLService
4. DSA-DOC-XLSService
5. DSA-NoSQL-MongoDBService
6. DSA-SparkSQL-Service
7. DSA-WEB-RESTService
8. DSA-WEB-VaadinService
```

Observații:

```text
DSA-SparkSQL-Service depinde de microserviciile Access Model.
DSA-WEB-RESTService depinde de DSA-SparkSQL-Service.
DSA-WEB-VaadinService depinde de DSA-WEB-RESTService.
```

---

# 14. Comparație P1 vs P2

| Componentă | P1 – FDBO / Oracle | P2 – DSA Java4DI |
|---|---|---|
| Integrare | Oracle schema FDBO | SparkSQL |
| Access PostgreSQL | Oracle DB Link | Spring Boot REST Service |
| Access XLSX | ExcelTable în Oracle | Spring Boot XLS Service |
| Access MongoDB | RESTHeart + UTL_HTTP + JSON_TABLE | Spring Boot MongoDB Service |
| Model analitic | Oracle SQL Views | SparkSQL Views |
| REST API | ORDS | Spring Boot REST |
| Web UI | Oracle APEX | Vaadin |
| Stil arhitectural | Federated Database / Oracle-centric | Data Service Architecture / Java4DI |

---

# 15. Sinteză

Acest repository demonstrează două abordări de integrare a datelor eterogene:

```text
P1 – Oracle FDBO
  Integrare federativă în Oracle
  View-uri de access, integrare, dimensiuni, fapte și analitice
  Expunere prin ORDS
  Vizualizare prin APEX

P2 – Java4DI / DSA
  Microservicii Spring Boot pentru access model
  SparkSQL ca motor de integrare și analiză
  REST Web Model prin Spring Boot + Hive JDBC
  Dashboard Vaadin pentru vizualizare
```

Proiectul integrează date din PostgreSQL, XLSX și MongoDB și oferă analiză multidimensională, funcții window, statistici avansate și interfețe REST/Web pentru acces programatic și vizual.
