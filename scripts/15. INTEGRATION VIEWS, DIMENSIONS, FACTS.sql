################### INTEGRATION VIEWS ######################################3
--integration view Customer module
CREATE OR REPLACE VIEW INT_CUSTOMER_PROFILE_V AS
SELECT
    c.client_id,
    c.current_age,
    c.retirement_age,
    c.birth_year,
    c.birth_month,
    c.gender,
    c.address,
    c.latitude,
    c.longitude,
    f.per_capita_income,
    f.yearly_income,
    f.total_debt,
    f.credit_score,
    f.num_credit_cards,
    r.tx_count,
    r.total_amount,
    r.top_mcc
FROM V_XLS_CUSTOMERS c
LEFT JOIN V_XLS_CUSTOMER_FINANCE f
    ON c.client_id = f.client_id
LEFT JOIN V_MONGO_CUSTOMER_RISK r
    ON c.client_id = r.client_id;

select * from INT_CUSTOMER_PROFILE_V;
    
--intergration view Cards module

CREATE OR REPLACE VIEW INT_CARD_PROFILE_V AS
SELECT
    c.card_id,
    c.client_id,
    c.card_brand,
    c.card_type,
    c.expires,
    c.acct_open_date,
    c.num_cards_issued,
    l.credit_limit,
    s.has_chip,
    s.year_pin_last_changed,
    s.card_on_dark_web
FROM CARDS_V c
LEFT JOIN CARD_LIMITS_V l
    ON c.card_id = l.card_id
LEFT JOIN CARD_SECURITY_V s
    ON c.card_id = s.card_id;
    
select * from INT_CARD_PROFILE_V;

--Integration view Transactions (Postgres - 10.000 records & Mongo - 100 records)
CREATE OR REPLACE VIEW INT_TRANSACTIONS_BASE_V AS
SELECT
    CAST(txn_id AS NUMBER(20))              AS txn_id,
    CAST(txn_date AS DATE)                  AS txn_date,
    CAST(client_id AS NUMBER(20))           AS client_id,
    CAST(card_id AS NUMBER(20))             AS card_id,
    CAST(amount AS NUMBER(18,2))            AS amount,
    CAST(use_chip AS VARCHAR2(50))          AS use_chip,
    CAST(NULL AS VARCHAR2(50))              AS channel,
    CAST(merchant_id AS NUMBER(20))         AS merchant_id,
    CAST(mcc AS NUMBER(10))                 AS mcc,
    CAST(errors AS VARCHAR2(200))           AS errors,
    CAST('POSTGRES' AS VARCHAR2(20))        AS source_system
FROM TRANSACTIONS_V

UNION ALL

SELECT
    CAST(txn_id AS NUMBER(20))              AS txn_id,
    CAST(TO_TIMESTAMP(ts, 'YYYY-MM-DD HH24:MI:SS') AS DATE) AS txn_date,
    CAST(client_id AS NUMBER(20))           AS client_id,
    CAST(card_id AS NUMBER(20))             AS card_id,
    CAST(amount AS NUMBER(18,2))            AS amount,
    CAST(NULL AS VARCHAR2(50))              AS use_chip,
    CAST(channel AS VARCHAR2(50))           AS channel,
    CAST(merchant_id AS NUMBER(20))         AS merchant_id,
    CAST(mcc AS NUMBER(10))                 AS mcc,
    CAST(NULL AS VARCHAR2(200))             AS errors,
    CAST('MONGO' AS VARCHAR2(20))           AS source_system
FROM V_MONGO_TRANSACTIONS;

select * from INT_TRANSACTIONS_BASE_V;

--checks - nr of transactions
SELECT * FROM INT_TRANSACTIONS_V;
SELECT COUNT(*) AS TRANZACTII,'POSTGRES'  AS SURSA FROM TRANSACTIONS_V
UNION ALL
SELECT COUNT(*) AS TRANZACTII,'MONGO' AS SURSA  FROM V_MONGO_TRANSACTIONS 


--verificam tranzactiile din mongo sa nu fie identice cu cele din postgres 
SELECT 
    p.txn_id,
    p.client_id,
    p.card_id,
    p.txn_date,
    p.amount
FROM TRANSACTIONS_V p
JOIN V_MONGO_TRANSACTIONS m
    ON p.txn_id = m.txn_id; --ok
    

###################### DIMENSIONS ###############################
--1. CLIENT DATA, CREDIT SCORE AND INCOME GROUPS
CREATE OR REPLACE VIEW DIM_CLIENT_V AS
SELECT
    client_id,
    gender,
    current_age,
    retirement_age,
    birth_year,
    birth_month,
    per_capita_income,
    yearly_income,
    total_debt,
    credit_score,
    num_credit_cards,
    tx_count,
    total_amount,
    top_mcc,
    CASE
        WHEN current_age < 30 THEN 'UNDER_30'
        WHEN current_age BETWEEN 30 AND 49 THEN '30_49'
        ELSE '50_PLUS'
    END AS age_group,
    CASE
        WHEN credit_score < 580 THEN 'POOR'
        WHEN credit_score < 670 THEN 'FAIR'
        WHEN credit_score < 740 THEN 'GOOD'
        WHEN credit_score < 800 THEN 'VERY_GOOD'
        ELSE 'EXCELLENT'
    END AS credit_score_group,
    CASE
        WHEN yearly_income < 30000 THEN 'LOW_INCOME'
        WHEN yearly_income < 70000 THEN 'MEDIUM_INCOME'
        ELSE 'HIGH_INCOME'
    END AS income_group,
    CASE
        WHEN total_debt < 5000 THEN 'LOW_DEBT'
        WHEN total_debt < 20000 THEN 'MEDIUM_DEBT'
        ELSE 'HIGH_DEBT'
    END AS debt_group
FROM INT_CUSTOMER_PROFILE_V;

select * from DIM_CLIENT_V;
--2. CARDS, ALSO ADDING GROUPS
CREATE OR REPLACE VIEW DIM_CARD_V AS
SELECT
    card_id,
    client_id,
    card_brand,
    card_type,
    credit_limit,
    has_chip,
    card_on_dark_web,
    acct_open_date,
    expires,
    num_cards_issued,
    CASE
        WHEN credit_limit < 5000 THEN 'LOW_LIMIT'
        WHEN credit_limit < 15000 THEN 'MEDIUM_LIMIT'
        ELSE 'HIGH_LIMIT'
    END AS credit_limit_group,
    CASE
        WHEN has_chip = 'YES' THEN 'CHIP_ENABLED'
        ELSE 'NO_CHIP'
    END AS chip_group,
    CASE
        WHEN card_on_dark_web = 'YES' THEN 'RISK_EXPOSED'
        ELSE 'NO_EXPOSURE'
    END AS darkweb_group
FROM INT_CARD_PROFILE_V;

SELECT * FROM DIM_CARD_V;

--3. DATE TIME VIEW 
CREATE OR REPLACE VIEW DIM_TIME_V AS
SELECT DISTINCT
    txn_date,
    EXTRACT(YEAR FROM txn_date)  AS txn_year,
    EXTRACT(MONTH FROM txn_date) AS txn_month,
    EXTRACT(DAY FROM txn_date)   AS txn_day,
    TO_CHAR(txn_date, 'YYYY-MM') AS year_month,
    TO_CHAR(txn_date, 'MON')     AS month_name,
    TO_CHAR(txn_date, 'DY')      AS weekday_name
FROM INT_TRANSACTIONS_BASE_V
WHERE txn_date IS NOT NULL;

SELECT * FROM DIM_TIME_V;

--4. MERCHANTS
CREATE OR REPLACE VIEW DIM_MERCHANT_GEO_V AS
SELECT
    merchant_id,
    merchant_state,
    merchant_city,
    zip,
    CASE
        WHEN merchant_state IS NULL OR TRIM(merchant_state) = '' THEN 'UNKNOWN_STATE'
        ELSE UPPER(TRIM(merchant_state))
    END AS state_group,
    CASE
        WHEN merchant_city IS NULL OR TRIM(merchant_city) = '' THEN 'UNKNOWN_CITY'
        ELSE INITCAP(TRIM(merchant_city))
    END AS city_group,
    CASE
        WHEN merchant_state IS NULL OR TRIM(merchant_state) = '' THEN 'UNKNOWN_STATE'
        ELSE UPPER(TRIM(merchant_state))
    END || ' > ' ||
    CASE
        WHEN merchant_city IS NULL OR TRIM(merchant_city) = '' THEN 'UNKNOWN_CITY'
        ELSE INITCAP(TRIM(merchant_city))
    END AS geo_hierarchy
FROM MERCHANTS_V;

SELECT * FROM DIM_MERCHANT_GEO_V;

############## FACT VIEW ###########################
CREATE OR REPLACE VIEW FACT_TRANSACTIONS_V AS
SELECT
    txn_id,
    txn_date,
    client_id,
    card_id,
    merchant_id,
    amount,
    mcc,
    use_chip,
    channel,
    errors,
    source_system
FROM INT_TRANSACTIONS_BASE_V;

SELECT * FROM FACT_TRANSACTIONS_V;

################### Analytical Views #############################

--Să se analizeze valoarea totală a tranzacțiilor pe structură calendaristică ierarhică, la nivel de an, lună și zi, incluzând subtotaluri pe lună, pe an și total general.

CREATE OR REPLACE VIEW OLAP_VIEW_TXN_CALENDAR_V AS
SELECT
    CASE
        WHEN GROUPING(t.txn_year) = 1 THEN '{TOTAL_GENERAL}'
        ELSE TO_CHAR(t.txn_year)
    END AS txn_year,
    CASE
        WHEN GROUPING(t.txn_year) = 1 THEN ' '
        WHEN GROUPING(t.txn_month) = 1 THEN 'subtotal year ' || TO_CHAR(t.txn_year)
        ELSE TO_CHAR(t.txn_month)
    END AS txn_month,
    CASE
        WHEN GROUPING(t.txn_year) = 1 THEN ' '
        WHEN GROUPING(t.txn_month) = 1 THEN ' '
        WHEN GROUPING(t.txn_day) = 1 THEN 'subtotal month ' || TO_CHAR(t.txn_month)
        ELSE TO_CHAR(t.txn_day)
    END AS txn_day,
    SUM(NVL(f.amount, 0)) AS total_amount
FROM DIM_TIME_V t
LEFT JOIN FACT_TRANSACTIONS_V f
    ON t.txn_date = f.txn_date
GROUP BY ROLLUP(t.txn_year, t.txn_month, t.txn_day)
ORDER BY t.txn_year, t.txn_month, t.txn_day;

select * from OLAP_VIEW_TXN_CALENDAR_V;

--Să se analizeze suma totală a tranzacțiilor în funcție de zona geografică a comerciantului, la nivel de stat și oraș, cu subtotal pe stat și total general.
CREATE OR REPLACE VIEW OLAP_VIEW_TXN_MERCHANT_GEO_V AS
SELECT
    CASE
        WHEN GROUPING(g.state_group) = 1 THEN '{TOTAL_GENERAL}'
        ELSE g.state_group
    END AS state_group,
    CASE
        WHEN GROUPING(g.state_group) = 1 THEN ' '
        WHEN GROUPING(g.city_group) = 1 THEN 'subtotal state ' || g.state_group
        ELSE g.city_group
    END AS city_group,
    SUM(NVL(f.amount, 0)) AS total_amount
FROM DIM_MERCHANT_GEO_V g
LEFT JOIN FACT_TRANSACTIONS_V f
    ON g.merchant_id = f.merchant_id
GROUP BY ROLLUP(g.state_group, g.city_group)
ORDER BY g.state_group, g.city_group;

select * from OLAP_VIEW_TXN_MERCHANT_GEO_V;

--Să se analizeze valoarea totală a tranzacțiilor în funcție de grupa de scor de credit și grupa de vârstă a clienților, cu subtotaluri pe fiecare categorie de scor de credit.
CREATE OR REPLACE VIEW OLAP_VIEW_TXN_CREDIT_AGE_V AS
SELECT
    CASE
        WHEN GROUPING(c.credit_score_group) = 1 THEN '{TOTAL_GENERAL}'
        ELSE c.credit_score_group
    END AS credit_score_group,
    CASE
        WHEN GROUPING(c.credit_score_group) = 1 THEN ' '
        WHEN GROUPING(c.age_group) = 1 THEN 'subtotal score ' || c.credit_score_group
        ELSE c.age_group
    END AS age_group,
    SUM(NVL(f.amount, 0)) AS total_amount
FROM DIM_CLIENT_V c
LEFT JOIN FACT_TRANSACTIONS_V f
    ON c.client_id = f.client_id
GROUP BY ROLLUP(c.credit_score_group, c.age_group)
ORDER BY c.credit_score_group, c.age_group;

select * from OLAP_VIEW_TXN_CREDIT_AGE_V;

---Să se analizeze totalul tranzacțiilor pe baza caracteristicilor cardului, la nivel de brand de card și tip de card, cu subtotal pe brand și total general.
CREATE OR REPLACE VIEW OLAP_VIEW_TXN_CARD_BRAND_TYPE_V AS
SELECT
    CASE
        WHEN GROUPING(c.card_brand) = 1 THEN '{TOTAL_GENERAL}'
        ELSE c.card_brand
    END AS card_brand,
    CASE
        WHEN GROUPING(c.card_brand) = 1 THEN ' '
        WHEN GROUPING(c.card_type) = 1 THEN 'subtotal brand ' || c.card_brand
        ELSE c.card_type
    END AS card_type,
    SUM(NVL(f.amount, 0)) AS total_amount
FROM DIM_CARD_V c
LEFT JOIN FACT_TRANSACTIONS_V f
    ON c.card_id = f.card_id
GROUP BY ROLLUP(c.card_brand, c.card_type)
ORDER BY c.card_brand, c.card_type;

select * from OLAP_VIEW_TXN_CARD_BRAND_TYPE_V;

---Să se determine valoarea totală și numărul tranzacțiilor în funcție de sistemul sursă și canalul de proveniență, incluzând subtotaluri pe sursă și total general.
CREATE OR REPLACE VIEW OLAP_VIEW_TXN_SOURCE_CHANNEL_V AS
SELECT
    CASE
        WHEN GROUPING(source_system) = 1 THEN '{TOTAL_GENERAL}'
        ELSE source_system
    END AS source_system,
    CASE
        WHEN GROUPING(source_system) = 1 THEN ' '
        WHEN GROUPING(channel) = 1 THEN 'subtotal source ' || source_system
        ELSE NVL(channel, 'NO_CHANNEL')
    END AS channel,
    SUM(NVL(amount, 0)) AS total_amount,
    COUNT(*) AS txn_count
FROM FACT_TRANSACTIONS_V
GROUP BY ROLLUP(source_system, channel)
ORDER BY source_system, channel;

select * from OLAP_VIEW_TXN_SOURCE_CHANNEL_V;

---Să se realizeze o analiză multidimensională de tip CUBE asupra tranzacțiilor după statul comerciantului și brandul cardului, astfel încât să fie obținute toate combinațiile posibile de agregare, inclusiv totalurile generale.
CREATE OR REPLACE VIEW OLAP_VIEW_TXN_STATE_BRAND_CUBE_V AS
SELECT
    CASE
        WHEN GROUPING(g.state_group) = 1 THEN '{ALL_STATES}'
        ELSE g.state_group
    END AS state_group,
    CASE
        WHEN GROUPING(c.card_brand) = 1 THEN '{ALL_BRANDS}'
        ELSE c.card_brand
    END AS card_brand,
    SUM(NVL(f.amount, 0)) AS total_amount,
    COUNT(*) AS txn_count
FROM FACT_TRANSACTIONS_V f
LEFT JOIN DIM_MERCHANT_GEO_V g
    ON f.merchant_id = g.merchant_id
LEFT JOIN DIM_CARD_V c
    ON f.card_id = c.card_id
GROUP BY CUBE(g.state_group, c.card_brand)
ORDER BY g.state_group, c.card_brand;

select * from OLAP_VIEW_TXN_STATE_BRAND_CUBE_V;

---Să se analizeze valoarea totală și numărul tranzacțiilor în funcție de grupa de venit și grupa de datorie a clientului, cu subtotaluri pe fiecare categorie de venit.
CREATE OR REPLACE VIEW OLAP_VIEW_TXN_INCOME_DEBT_V AS
SELECT
    CASE
        WHEN GROUPING(c.income_group) = 1 THEN '{TOTAL_GENERAL}'
        ELSE c.income_group
    END AS income_group,
    CASE
        WHEN GROUPING(c.income_group) = 1 THEN ' '
        WHEN GROUPING(c.debt_group) = 1 THEN 'subtotal income ' || c.income_group
        ELSE c.debt_group
    END AS debt_group,
    SUM(NVL(f.amount, 0)) AS total_amount,
    COUNT(*) AS txn_count
FROM DIM_CLIENT_V c
LEFT JOIN FACT_TRANSACTIONS_V f
    ON c.client_id = f.client_id
GROUP BY ROLLUP(c.income_group, c.debt_group)
ORDER BY c.income_group, c.debt_group;

select * from OLAP_VIEW_TXN_INCOME_DEBT_V;

--Să se realizeze o analiză de tip CUBE asupra tranzacțiilor după statusul cipului cardului și statusul de risc dark web, calculând suma totală, numărul tranzacțiilor și valoarea medie.
CREATE OR REPLACE VIEW OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V AS
SELECT
    CASE
        WHEN GROUPING(c.chip_group) = 1 THEN '{ALL_CHIP_STATUS}'
        ELSE c.chip_group
    END AS chip_group,
    CASE
        WHEN GROUPING(c.darkweb_group) = 1 THEN '{ALL_RISK_STATUS}'
        ELSE c.darkweb_group
    END AS darkweb_group,
    SUM(NVL(f.amount, 0)) AS total_amount,
    COUNT(*) AS txn_count,
    ROUND(AVG(NVL(f.amount, 0)), 2) AS avg_amount
FROM FACT_TRANSACTIONS_V f
LEFT JOIN DIM_CARD_V c
    ON f.card_id = c.card_id
GROUP BY CUBE(c.chip_group, c.darkweb_group)
ORDER BY c.chip_group, c.darkweb_group;

select * from OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V;

--Să se analizeze tranzacțiile prin GROUPING SETS la nivel de an, stat și sistem sursă, pentru a obține separat agregări pe an, pe an-stat, pe an-stat-sursă și total general.
CREATE OR REPLACE VIEW OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V AS
SELECT
    CASE
        WHEN GROUPING(t.txn_year) = 1 THEN '{TOTAL_GENERAL}'
        ELSE TO_CHAR(t.txn_year)
    END AS txn_year,
    CASE
        WHEN GROUPING(t.txn_year) = 1 THEN ' '
        WHEN GROUPING(g.state_group) = 1 THEN 'subtotal year ' || TO_CHAR(t.txn_year)
        ELSE g.state_group
    END AS state_group,
    CASE
        WHEN GROUPING(t.txn_year) = 1 THEN ' '
        WHEN GROUPING(g.state_group) = 1 THEN ' '
        WHEN GROUPING(f.source_system) = 1 THEN 'subtotal state ' || g.state_group
        ELSE f.source_system
    END AS source_system,
    SUM(NVL(f.amount, 0)) AS total_amount,
    COUNT(*) AS txn_count
FROM FACT_TRANSACTIONS_V f
LEFT JOIN DIM_TIME_V t
    ON f.txn_date = t.txn_date
LEFT JOIN DIM_MERCHANT_GEO_V g
    ON f.merchant_id = g.merchant_id
GROUP BY GROUPING SETS (
    (t.txn_year),
    (t.txn_year, g.state_group),
    (t.txn_year, g.state_group, f.source_system),
    ()
)
ORDER BY 1, 2, 3;

select * from OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V;

--Să se analizeze tranzacțiile prin GROUPING SETS la nivel de lună, grupă de venit și cod MCC, calculând suma totală, numărul de tranzacții și valoarea medie, împreună cu subtotaluri și total general.
CREATE OR REPLACE VIEW OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V AS
SELECT
    CASE
        WHEN GROUPING(t.txn_month) = 1 THEN '{TOTAL_GENERAL}'
        ELSE TO_CHAR(t.txn_month)
    END AS txn_month,
    CASE
        WHEN GROUPING(t.txn_month) = 1 THEN ' '
        WHEN GROUPING(c.income_group) = 1 THEN 'subtotal month ' || TO_CHAR(t.txn_month)
        ELSE c.income_group
    END AS income_group,
    CASE
        WHEN GROUPING(t.txn_month) = 1 THEN ' '
        WHEN GROUPING(c.income_group) = 1 THEN ' '
        WHEN GROUPING(f.mcc) = 1 THEN 'subtotal income ' || c.income_group
        ELSE TO_CHAR(f.mcc)
    END AS mcc,
    SUM(NVL(f.amount, 0)) AS total_amount,
    COUNT(*) AS txn_count,
    ROUND(AVG(NVL(f.amount, 0)), 2) AS avg_amount
FROM FACT_TRANSACTIONS_V f
LEFT JOIN DIM_TIME_V t
    ON f.txn_date = t.txn_date
LEFT JOIN DIM_CLIENT_V c
    ON f.client_id = c.client_id
GROUP BY GROUPING SETS (
    (t.txn_month),
    (t.txn_month, c.income_group),
    (t.txn_month, c.income_group, f.mcc),
    ()
)
ORDER BY 1, 2, 3;

select * from OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V;