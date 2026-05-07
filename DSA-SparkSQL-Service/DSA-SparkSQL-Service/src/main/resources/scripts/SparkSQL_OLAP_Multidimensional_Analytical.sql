--------------------------------------------------------------------------------
--- SparkSQL_OLAP.sql
--- Integration, Dimensional, Fact and Analytical Views
--- Adapted for Java4DI / DSA SparkSQL Architecture
--- Sources already exposed in SparkSQL as REST remote views:
---   PostgreSQL JDBC/JPA, XLSX, MongoDB
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 0. SOURCE VIEWS -------------------------------------------------------------
--------------------------------------------------------------------------------

-- Existing SparkSQL source views:
--
-- PostgreSQL JDBC:
--   card_limits_view
--   card_security_view
--   transactions_view
--
-- PostgreSQL JPA:
--   cards_jpa_view
--   merchants_jpa_view
--
-- XLSX:
--   CUSTOMER_VIEW
--   CUSTOMER_FINANCE_VIEW
--
-- MongoDB:
--   transactions_mongo_view
--   customer_risk_mongo_view
--   customer_risk_cards_mongo_view
--   transactions_customer_risk_mongo_view


--------------------------------------------------------------------------------
--- 1. INTEGRATION VIEWS --------------------------------------------------------
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 1.1 Customer Integration View
--- Sources:
---   CUSTOMER_VIEW
---   CUSTOMER_FINANCE_VIEW
---   customer_risk_mongo_view
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW INT_CUSTOMER_PROFILE_V AS
SELECT
    CAST(c.clientId AS BIGINT)                  AS client_id,
    CAST(c.currentAge AS INT)                   AS current_age,
    CAST(c.retirementAge AS INT)                AS retirement_age,
    CAST(c.birthYear AS INT)                    AS birth_year,
    CAST(c.birthMonth AS INT)                   AS birth_month,
    CAST(c.gender AS STRING)                    AS gender,
    CAST(c.address AS STRING)                   AS address,
    CAST(c.latitude AS DOUBLE)                  AS latitude,
    CAST(c.longitude AS DOUBLE)                 AS longitude,

    CAST(f.perCapitaIncome AS DOUBLE)           AS per_capita_income,
    CAST(f.yearlyIncome AS DOUBLE)              AS yearly_income,
    CAST(f.totalDebt AS DOUBLE)                 AS total_debt,
    CAST(f.creditScore AS INT)                  AS credit_score,
    CAST(f.numCreditCards AS INT)               AS num_credit_cards,

    CAST(r.tx_count AS BIGINT)                  AS tx_count,
    CAST(r.total_amount AS DOUBLE)              AS risk_total_amount,
    CAST(r.top_mcc AS BIGINT)                   AS top_mcc
FROM CUSTOMER_VIEW c
LEFT JOIN CUSTOMER_FINANCE_VIEW f
    ON CAST(c.clientId AS BIGINT) = CAST(f.clientId AS BIGINT)
LEFT JOIN customer_risk_mongo_view r
    ON CAST(c.clientId AS BIGINT) = CAST(r.client_id AS BIGINT);

SELECT * FROM INT_CUSTOMER_PROFILE_V LIMIT 20;


--------------------------------------------------------------------------------
--- 1.2 Card Integration View
--- Sources:
---   cards_jpa_view
---   card_limits_view
---   card_security_view
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW INT_CARD_PROFILE_V AS
SELECT
    CAST(c.cardId AS BIGINT)                    AS card_id,
    CAST(c.clientId AS BIGINT)                  AS client_id,
    CAST(c.cardBrand AS STRING)                 AS card_brand,
    CAST(c.cardType AS STRING)                  AS card_type,
    CAST(c.expires AS STRING)                   AS expires,
    CAST(c.acctOpenDate AS STRING)              AS acct_open_date,
    CAST(c.numCardsIssued AS BIGINT)            AS num_cards_issued,

    CAST(l.creditLimit AS DOUBLE)               AS credit_limit,
    CAST(s.hasChip AS BOOLEAN)                  AS has_chip,
    CAST(s.yearPinLastChanged AS INT)           AS year_pin_last_changed,
    CAST(s.cardOnDarkWeb AS BOOLEAN)            AS card_on_dark_web
FROM cards_jpa_view c
LEFT JOIN card_limits_view l
    ON CAST(c.cardId AS BIGINT) = CAST(l.cardId AS BIGINT)
LEFT JOIN card_security_view s
    ON CAST(c.cardId AS BIGINT) = CAST(s.cardId AS BIGINT);

SELECT * FROM INT_CARD_PROFILE_V LIMIT 20;


--------------------------------------------------------------------------------
--- 1.3 Transaction Base Integration View
--- Sources:
---   transactions_view          - PostgreSQL JDBC source
---   transactions_mongo_view    - MongoDB source
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW INT_TRANSACTIONS_BASE_V AS

SELECT
    CAST(txnId AS BIGINT) AS txn_id,
    CAST(txnDate AS STRING) AS txn_raw_date,
    TO_TIMESTAMP(CAST(txnDate AS STRING)) AS txn_timestamp,
    TO_DATE(TO_TIMESTAMP(CAST(txnDate AS STRING))) AS txn_date,

    CAST(clientId AS BIGINT) AS client_id,
    CAST(cardId AS BIGINT) AS card_id,
    CAST(amount AS DOUBLE) AS amount,
    CAST(useChip AS STRING) AS transaction_channel,
    CAST(merchantId AS BIGINT) AS merchant_id,
    CAST(mcc AS BIGINT) AS mcc,
    CAST(errors AS STRING) AS errors,

    'POSTGRES' AS source_system

FROM transactions_view

UNION ALL

SELECT
    CAST(txn_id AS BIGINT) AS txn_id,
    CAST(txn_timestamp AS STRING) AS txn_raw_date,
    TO_TIMESTAMP(CAST(txn_timestamp AS STRING)) AS txn_timestamp,
    TO_DATE(TO_TIMESTAMP(CAST(txn_timestamp AS STRING))) AS txn_date,

    CAST(client_id AS BIGINT) AS client_id,
    CAST(card_id AS BIGINT) AS card_id,
    CAST(amount AS DOUBLE) AS amount,
    CAST(channel AS STRING) AS transaction_channel,
    CAST(merchant_id AS BIGINT) AS merchant_id,
    CAST(mcc AS BIGINT) AS mcc,
    CAST(errors AS STRING) AS errors,

    'MONGO' AS source_system

FROM transactions_mongo_view;

SELECT * FROM INT_TRANSACTIONS_BASE_V LIMIT 20;


--------------------------------------------------------------------------------
--- 1.4 Source Check: PostgreSQL vs MongoDB Transactions
--------------------------------------------------------------------------------

SELECT
    source_system,
    COUNT(*) AS total_rows,
    COUNT(DISTINCT txn_id) AS distinct_transactions
FROM INT_TRANSACTIONS_BASE_V
GROUP BY source_system
ORDER BY source_system;

SELECT COUNT(*) AS transactions_count, 'POSTGRES' AS source_system
FROM transactions_view
UNION ALL
SELECT COUNT(*) AS transactions_count, 'MONGO' AS source_system
FROM transactions_mongo_view;


--------------------------------------------------------------------------------
--- 2. DIMENSIONAL VIEWS --------------------------------------------------------
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 2.1 Client Dimension
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW DIM_CLIENT_V AS
SELECT
    client_id,
    gender,
    current_age,
    retirement_age,
    birth_year,
    birth_month,
    address,
    latitude,
    longitude,
    per_capita_income,
    yearly_income,
    total_debt,
    credit_score,
    num_credit_cards,
    tx_count,
    risk_total_amount,
    top_mcc,

    CASE
        WHEN current_age IS NULL THEN 'UNKNOWN_AGE'
        WHEN current_age < 30 THEN 'UNDER_30'
        WHEN current_age BETWEEN 30 AND 49 THEN '30_49'
        ELSE '50_PLUS'
    END AS age_group,

    CASE
        WHEN credit_score IS NULL THEN 'UNKNOWN_SCORE'
        WHEN credit_score < 580 THEN 'POOR'
        WHEN credit_score < 670 THEN 'FAIR'
        WHEN credit_score < 740 THEN 'GOOD'
        WHEN credit_score < 800 THEN 'VERY_GOOD'
        ELSE 'EXCELLENT'
    END AS credit_score_group,

    CASE
        WHEN yearly_income IS NULL THEN 'UNKNOWN_INCOME'
        WHEN yearly_income < 30000 THEN 'LOW_INCOME'
        WHEN yearly_income < 70000 THEN 'MEDIUM_INCOME'
        ELSE 'HIGH_INCOME'
    END AS income_group,

    CASE
        WHEN total_debt IS NULL THEN 'UNKNOWN_DEBT'
        WHEN total_debt < 5000 THEN 'LOW_DEBT'
        WHEN total_debt < 20000 THEN 'MEDIUM_DEBT'
        ELSE 'HIGH_DEBT'
    END AS debt_group
FROM INT_CUSTOMER_PROFILE_V;

SELECT * FROM DIM_CLIENT_V LIMIT 20;


--------------------------------------------------------------------------------
--- 2.2 Card Dimension
--------------------------------------------------------------------------------

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
    year_pin_last_changed,

    CASE
        WHEN credit_limit IS NULL THEN 'UNKNOWN_LIMIT'
        WHEN credit_limit < 5000 THEN 'LOW_LIMIT'
        WHEN credit_limit < 15000 THEN 'MEDIUM_LIMIT'
        ELSE 'HIGH_LIMIT'
    END AS credit_limit_group,

    CASE
        WHEN has_chip = true THEN 'CHIP_ENABLED'
        WHEN has_chip = false THEN 'NO_CHIP'
        ELSE 'UNKNOWN_CHIP_STATUS'
    END AS chip_group,

    CASE
        WHEN card_on_dark_web = true THEN 'RISK_EXPOSED'
        WHEN card_on_dark_web = false THEN 'NO_EXPOSURE'
        ELSE 'UNKNOWN_RISK_STATUS'
    END AS darkweb_group
FROM INT_CARD_PROFILE_V;

SELECT * FROM DIM_CARD_V LIMIT 20;


--------------------------------------------------------------------------------
--- 2.3 Time Dimension
--- This is a proper time dimension with distinct transaction dates.
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW DIM_TIME_V AS
SELECT DISTINCT
    txn_date,
    YEAR(txn_date) AS txn_year,
    MONTH(txn_date) AS txn_month,
    DAY(txn_date) AS txn_day,
    QUARTER(txn_date) AS txn_quarter,
    DATE_FORMAT(txn_date, 'yyyy-MM') AS year_month,
    DATE_FORMAT(txn_date, 'MMMM') AS month_name,
    DATE_FORMAT(txn_date, 'EEEE') AS weekday_name
FROM INT_TRANSACTIONS_BASE_V
WHERE txn_date IS NOT NULL;

SELECT * FROM DIM_TIME_V ORDER BY txn_date LIMIT 50;


--------------------------------------------------------------------------------
--- 2.3.1 Check: Distinct Days per Month
--------------------------------------------------------------------------------

SELECT
    txn_year,
    txn_month,
    COUNT(*) AS distinct_days
FROM DIM_TIME_V
GROUP BY txn_year, txn_month
ORDER BY txn_year, txn_month;


--------------------------------------------------------------------------------
--- 2.3.2 Check: Transactions per Month
--------------------------------------------------------------------------------

SELECT
    YEAR(txn_date) AS txn_year,
    MONTH(txn_date) AS txn_month,
    COUNT(*) AS total_transactions
FROM INT_TRANSACTIONS_BASE_V
WHERE txn_date IS NOT NULL
GROUP BY YEAR(txn_date), MONTH(txn_date)
ORDER BY txn_year, txn_month;


--------------------------------------------------------------------------------
--- 2.4 Merchant Geography Dimension
--- It combines merchants from JPA and merchant data embedded in MongoDB transactions.
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW DIM_MERCHANT_GEO_V AS
WITH merchant_union AS (
    SELECT
        CAST(merchantId AS BIGINT) AS merchant_id,
        CAST(merchantCity AS STRING) AS merchant_city,
        CAST(merchantState AS STRING) AS merchant_state,
        CAST(zip AS STRING) AS zip
    FROM merchants_jpa_view

    UNION ALL

    SELECT
        CAST(merchant_id AS BIGINT) AS merchant_id,
        CAST(merchant_city AS STRING) AS merchant_city,
        CAST(merchant_state AS STRING) AS merchant_state,
        CAST(merchant_zip AS STRING) AS zip
    FROM transactions_mongo_view
),
merchant_dedup AS (
    SELECT
        merchant_id,
        MAX(merchant_city) AS merchant_city,
        MAX(merchant_state) AS merchant_state,
        MAX(zip) AS zip
    FROM merchant_union
    WHERE merchant_id IS NOT NULL
    GROUP BY merchant_id
)
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

    CONCAT(
        CASE
            WHEN merchant_state IS NULL OR TRIM(merchant_state) = '' THEN 'UNKNOWN_STATE'
            ELSE UPPER(TRIM(merchant_state))
        END,
        ' > ',
        CASE
            WHEN merchant_city IS NULL OR TRIM(merchant_city) = '' THEN 'UNKNOWN_CITY'
            ELSE INITCAP(TRIM(merchant_city))
        END
    ) AS geo_hierarchy
FROM merchant_dedup;

SELECT * FROM DIM_MERCHANT_GEO_V LIMIT 20;


--------------------------------------------------------------------------------
--- 3. FACT VIEWS ---------------------------------------------------------------
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 3.1 Base Fact View
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW FACT_TRANSACTIONS_V AS
SELECT
    txn_id,
    txn_timestamp,
    txn_date,
    client_id,
    card_id,
    merchant_id,
    amount,
    mcc,

    transaction_channel AS use_chip,
    transaction_channel AS channel,

    errors,
    source_system
FROM INT_TRANSACTIONS_BASE_V;

SELECT * FROM FACT_TRANSACTIONS_V LIMIT 20;


--------------------------------------------------------------------------------
--- 3.2 Enriched Fact View
--- This is useful for dashboards and analytical views.
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW FACT_TRANSACTIONS_ENRICHED_V AS
SELECT
    f.txn_id,
    f.txn_timestamp,
    f.txn_date,
    f.client_id,
    f.card_id,
    f.merchant_id,
    f.amount,
    f.mcc,
    f.use_chip,
    f.channel,
    f.errors,
    f.source_system,

    c.gender,
    c.age_group,
    c.credit_score_group,
    c.income_group,
    c.debt_group,

    d.card_brand,
    d.card_type,
    d.credit_limit_group,
    d.chip_group,
    d.darkweb_group,

    g.state_group,
    g.city_group,
    g.geo_hierarchy,

    t.txn_year,
    t.txn_month,
    t.txn_day,
    t.txn_quarter,
    t.year_month,
    t.month_name,
    t.weekday_name
FROM FACT_TRANSACTIONS_V f
LEFT JOIN DIM_CLIENT_V c
    ON f.client_id = c.client_id
LEFT JOIN DIM_CARD_V d
    ON f.card_id = d.card_id
LEFT JOIN DIM_MERCHANT_GEO_V g
    ON f.merchant_id = g.merchant_id
LEFT JOIN DIM_TIME_V t
    ON f.txn_date = t.txn_date;

SELECT * FROM FACT_TRANSACTIONS_ENRICHED_V LIMIT 20;


--------------------------------------------------------------------------------
--- 4. ANALYTICAL OLAP VIEWS ----------------------------------------------------
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 4.1 Calendar hierarchy ROLLUP: year -> month -> day
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 4.1 Calendar hierarchy ROLLUP: year -> month -> day
--- Ordered-friendly version with sorting columns
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW OLAP_VIEW_TXN_CALENDAR_V AS
WITH calendar_rollup AS (
    SELECT
        t.txn_year,
        t.txn_month,
        t.txn_day,

        GROUPING(t.txn_year) AS g_year,
        GROUPING(t.txn_month) AS g_month,
        GROUPING(t.txn_day) AS g_day,

        SUM(COALESCE(f.amount, 0)) AS total_amount,
        COUNT(f.txn_id) AS txn_count
    FROM DIM_TIME_V t
    LEFT JOIN FACT_TRANSACTIONS_V f
        ON t.txn_date = f.txn_date
    GROUP BY ROLLUP(t.txn_year, t.txn_month, t.txn_day)
),
calendar_formatted AS (
    SELECT
        CASE
            WHEN g_year = 1 THEN '{TOTAL_GENERAL}'
            ELSE CAST(txn_year AS STRING)
        END AS txn_year,

        CASE
            WHEN g_year = 1 THEN ' '
            WHEN g_month = 1 THEN CONCAT('subtotal year ', CAST(txn_year AS STRING))
            ELSE CAST(txn_month AS STRING)
        END AS txn_month,

        CASE
            WHEN g_year = 1 THEN ' '
            WHEN g_month = 1 THEN ' '
            WHEN g_day = 1 THEN CONCAT('subtotal month ', CAST(txn_month AS STRING))
            ELSE CAST(txn_day AS STRING)
        END AS txn_day,

        total_amount,
        txn_count,

        CASE
            WHEN g_year = 1 THEN 9999
            ELSE txn_year
        END AS sort_year,

        CASE
            WHEN g_year = 1 THEN 99
            WHEN g_month = 1 THEN 13
            ELSE txn_month
        END AS sort_month,

        CASE
            WHEN g_year = 1 THEN 99
            WHEN g_month = 1 THEN 99
            WHEN g_day = 1 THEN 32
            ELSE txn_day
        END AS sort_day,

        CASE
            WHEN g_year = 1 THEN 3
            WHEN g_month = 1 THEN 2
            WHEN g_day = 1 THEN 1
            ELSE 0
        END AS rollup_level
    FROM calendar_rollup
)
SELECT
    txn_year,
    txn_month,
    txn_day,
    total_amount,
    txn_count
FROM calendar_formatted
ORDER BY
    sort_year,
    sort_month,
    sort_day,
    rollup_level;

SELECT * FROM OLAP_VIEW_TXN_CALENDAR_V;


--------------------------------------------------------------------------------
--- 4.2 Merchant geography ROLLUP: state -> city
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW OLAP_VIEW_TXN_MERCHANT_GEO_V AS
WITH geo_rollup AS (
    SELECT
        g.state_group,
        g.city_group,

        GROUPING(g.state_group) AS g_state,
        GROUPING(g.city_group) AS g_city,

        SUM(COALESCE(f.amount, 0)) AS total_amount,
        COUNT(f.txn_id) AS txn_count
    FROM DIM_MERCHANT_GEO_V g
    LEFT JOIN FACT_TRANSACTIONS_V f
        ON g.merchant_id = f.merchant_id
    GROUP BY ROLLUP(g.state_group, g.city_group)
),
geo_formatted AS (
    SELECT
        CASE
            WHEN g_state = 1 THEN '{TOTAL_GENERAL}'
            ELSE state_group
        END AS state_group,

        CASE
            WHEN g_state = 1 THEN ' '
            WHEN g_city = 1 THEN CONCAT('subtotal state ', state_group)
            ELSE city_group
        END AS city_group,

        total_amount,
        txn_count,

        CASE
            WHEN g_state = 1 THEN 999999
            ELSE 0
        END AS sort_total,

        COALESCE(state_group, 'ZZZZZZ') AS sort_state,

        CASE
            WHEN g_state = 1 THEN 99
            WHEN g_city = 1 THEN 1
            ELSE 0
        END AS rollup_level,

        COALESCE(city_group, 'ZZZZZZ') AS sort_city
    FROM geo_rollup
)
SELECT
    state_group,
    city_group,
    total_amount,
    txn_count
FROM geo_formatted
ORDER BY
    sort_total,
    sort_state,
    rollup_level,
    sort_city;

SELECT * FROM OLAP_VIEW_TXN_MERCHANT_GEO_V;


--------------------------------------------------------------------------------
--- 4.3 Credit score and age ROLLUP
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW OLAP_VIEW_TXN_CREDIT_AGE_V AS
WITH credit_age_rollup AS (
    SELECT
        c.credit_score_group,
        c.age_group,

        GROUPING(c.credit_score_group) AS g_score,
        GROUPING(c.age_group) AS g_age,

        SUM(COALESCE(f.amount, 0)) AS total_amount,
        COUNT(f.txn_id) AS txn_count
    FROM DIM_CLIENT_V c
    LEFT JOIN FACT_TRANSACTIONS_V f
        ON c.client_id = f.client_id
    GROUP BY ROLLUP(c.credit_score_group, c.age_group)
),
credit_age_formatted AS (
    SELECT
        CASE
            WHEN g_score = 1 THEN '{TOTAL_GENERAL}'
            ELSE credit_score_group
        END AS credit_score_group,

        CASE
            WHEN g_score = 1 THEN ' '
            WHEN g_age = 1 THEN CONCAT('subtotal score ', credit_score_group)
            ELSE age_group
        END AS age_group,

        total_amount,
        txn_count,

        CASE
            WHEN g_score = 1 THEN 999
            WHEN credit_score_group = 'POOR' THEN 1
            WHEN credit_score_group = 'FAIR' THEN 2
            WHEN credit_score_group = 'GOOD' THEN 3
            WHEN credit_score_group = 'VERY_GOOD' THEN 4
            WHEN credit_score_group = 'EXCELLENT' THEN 5
            WHEN credit_score_group = 'UNKNOWN_SCORE' THEN 6
            ELSE 7
        END AS sort_score,

        CASE
            WHEN g_score = 1 THEN 99
            WHEN g_age = 1 THEN 1
            ELSE 0
        END AS rollup_level,

        CASE
            WHEN age_group = 'UNDER_30' THEN 1
            WHEN age_group = '30_49' THEN 2
            WHEN age_group = '50_PLUS' THEN 3
            WHEN age_group = 'UNKNOWN_AGE' THEN 4
            ELSE 5
        END AS sort_age
    FROM credit_age_rollup
)
SELECT
    credit_score_group,
    age_group,
    total_amount,
    txn_count
FROM credit_age_formatted
ORDER BY
    sort_score,
    rollup_level,
    sort_age;

SELECT * FROM OLAP_VIEW_TXN_CREDIT_AGE_V;


--------------------------------------------------------------------------------
--- 4.4 Card brand and card type ROLLUP
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW OLAP_VIEW_TXN_CARD_BRAND_TYPE_V AS
WITH card_rollup AS (
    SELECT
        c.card_brand,
        c.card_type,

        GROUPING(c.card_brand) AS g_brand,
        GROUPING(c.card_type) AS g_type,

        SUM(COALESCE(f.amount, 0)) AS total_amount,
        COUNT(f.txn_id) AS txn_count
    FROM DIM_CARD_V c
    LEFT JOIN FACT_TRANSACTIONS_V f
        ON c.card_id = f.card_id
    GROUP BY ROLLUP(c.card_brand, c.card_type)
),
card_formatted AS (
    SELECT
        CASE
            WHEN g_brand = 1 THEN '{TOTAL_GENERAL}'
            ELSE COALESCE(card_brand, 'UNKNOWN_BRAND')
        END AS card_brand,

        CASE
            WHEN g_brand = 1 THEN ' '
            WHEN g_type = 1 THEN CONCAT('subtotal brand ', COALESCE(card_brand, 'UNKNOWN_BRAND'))
            ELSE COALESCE(card_type, 'UNKNOWN_TYPE')
        END AS card_type,

        total_amount,
        txn_count,

        CASE
            WHEN g_brand = 1 THEN 999999
            ELSE 0
        END AS sort_total,

        COALESCE(card_brand, 'ZZZZZZ') AS sort_brand,

        CASE
            WHEN g_brand = 1 THEN 99
            WHEN g_type = 1 THEN 1
            ELSE 0
        END AS rollup_level,

        COALESCE(card_type, 'ZZZZZZ') AS sort_type
    FROM card_rollup
)
SELECT
    card_brand,
    card_type,
    total_amount,
    txn_count
FROM card_formatted
ORDER BY
    sort_total,
    sort_brand,
    rollup_level,
    sort_type;

SELECT * FROM OLAP_VIEW_TXN_CARD_BRAND_TYPE_V;


--------------------------------------------------------------------------------
--- 4.5 Source system and channel ROLLUP
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW OLAP_VIEW_TXN_SOURCE_CHANNEL_V AS
WITH source_rollup AS (
    SELECT
        source_system,
        channel,

        GROUPING(source_system) AS g_source,
        GROUPING(channel) AS g_channel,

        SUM(COALESCE(amount, 0)) AS total_amount,
        COUNT(txn_id) AS txn_count
    FROM FACT_TRANSACTIONS_V
    GROUP BY ROLLUP(source_system, channel)
),
source_formatted AS (
    SELECT
        CASE
            WHEN g_source = 1 THEN '{TOTAL_GENERAL}'
            ELSE source_system
        END AS source_system,

        CASE
            WHEN g_source = 1 THEN ' '
            WHEN g_channel = 1 THEN CONCAT('subtotal source ', source_system)
            ELSE COALESCE(channel, 'NO_CHANNEL')
        END AS channel,

        total_amount,
        txn_count,

        CASE
            WHEN g_source = 1 THEN 999
            WHEN source_system = 'POSTGRES' THEN 1
            WHEN source_system = 'MONGO' THEN 2
            ELSE 3
        END AS sort_source,

        CASE
            WHEN g_source = 1 THEN 99
            WHEN g_channel = 1 THEN 1
            ELSE 0
        END AS rollup_level,

        COALESCE(channel, 'ZZZZZZ') AS sort_channel
    FROM source_rollup
)
SELECT
    source_system,
    channel,
    total_amount,
    txn_count
FROM source_formatted
ORDER BY
    sort_source,
    rollup_level,
    sort_channel;

SELECT * FROM OLAP_VIEW_TXN_SOURCE_CHANNEL_V;


--------------------------------------------------------------------------------
--- 4.6 State and card brand CUBE
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW OLAP_VIEW_TXN_STATE_BRAND_CUBE_V AS
WITH state_brand_cube AS (
    SELECT
        g.state_group,
        c.card_brand,

        GROUPING(g.state_group) AS g_state,
        GROUPING(c.card_brand) AS g_brand,

        SUM(COALESCE(f.amount, 0)) AS total_amount,
        COUNT(f.txn_id) AS txn_count
    FROM FACT_TRANSACTIONS_V f
    LEFT JOIN DIM_MERCHANT_GEO_V g
        ON f.merchant_id = g.merchant_id
    LEFT JOIN DIM_CARD_V c
        ON f.card_id = c.card_id
    GROUP BY CUBE(g.state_group, c.card_brand)
),
state_brand_formatted AS (
    SELECT
        CASE
            WHEN g_state = 1 THEN '{ALL_STATES}'
            ELSE COALESCE(state_group, 'UNKNOWN_STATE')
        END AS state_group,

        CASE
            WHEN g_brand = 1 THEN '{ALL_BRANDS}'
            ELSE COALESCE(card_brand, 'UNKNOWN_BRAND')
        END AS card_brand,

        total_amount,
        txn_count,

        CASE
            WHEN g_state = 1 AND g_brand = 1 THEN 3
            WHEN g_state = 1 AND g_brand = 0 THEN 2
            WHEN g_state = 0 AND g_brand = 1 THEN 1
            ELSE 0
        END AS cube_level,

        COALESCE(state_group, 'ZZZZZZ') AS sort_state,
        COALESCE(card_brand, 'ZZZZZZ') AS sort_brand
    FROM state_brand_cube
)
SELECT
    state_group,
    card_brand,
    total_amount,
    txn_count
FROM state_brand_formatted
ORDER BY
    cube_level,
    sort_state,
    sort_brand;

SELECT * FROM OLAP_VIEW_TXN_STATE_BRAND_CUBE_V;


--------------------------------------------------------------------------------
--- 4.7 Income and debt ROLLUP
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW OLAP_VIEW_TXN_INCOME_DEBT_V AS
WITH income_debt_rollup AS (
    SELECT
        c.income_group,
        c.debt_group,

        GROUPING(c.income_group) AS g_income,
        GROUPING(c.debt_group) AS g_debt,

        SUM(COALESCE(f.amount, 0)) AS total_amount,
        COUNT(f.txn_id) AS txn_count
    FROM DIM_CLIENT_V c
    LEFT JOIN FACT_TRANSACTIONS_V f
        ON c.client_id = f.client_id
    GROUP BY ROLLUP(c.income_group, c.debt_group)
),
income_debt_formatted AS (
    SELECT
        CASE
            WHEN g_income = 1 THEN '{TOTAL_GENERAL}'
            ELSE income_group
        END AS income_group,

        CASE
            WHEN g_income = 1 THEN ' '
            WHEN g_debt = 1 THEN CONCAT('subtotal income ', income_group)
            ELSE debt_group
        END AS debt_group,

        total_amount,
        txn_count,

        CASE
            WHEN g_income = 1 THEN 999
            WHEN income_group = 'LOW_INCOME' THEN 1
            WHEN income_group = 'MEDIUM_INCOME' THEN 2
            WHEN income_group = 'HIGH_INCOME' THEN 3
            WHEN income_group = 'UNKNOWN_INCOME' THEN 4
            ELSE 5
        END AS sort_income,

        CASE
            WHEN g_income = 1 THEN 99
            WHEN g_debt = 1 THEN 1
            ELSE 0
        END AS rollup_level,

        CASE
            WHEN debt_group = 'LOW_DEBT' THEN 1
            WHEN debt_group = 'MEDIUM_DEBT' THEN 2
            WHEN debt_group = 'HIGH_DEBT' THEN 3
            WHEN debt_group = 'UNKNOWN_DEBT' THEN 4
            ELSE 5
        END AS sort_debt
    FROM income_debt_rollup
)
SELECT
    income_group,
    debt_group,
    total_amount,
    txn_count
FROM income_debt_formatted
ORDER BY
    sort_income,
    rollup_level,
    sort_debt;

SELECT * FROM OLAP_VIEW_TXN_INCOME_DEBT_V;


--------------------------------------------------------------------------------
--- 4.8 Card security CUBE: chip status and dark web status
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V AS
WITH security_cube AS (
    SELECT
        c.chip_group,
        c.darkweb_group,

        GROUPING(c.chip_group) AS g_chip,
        GROUPING(c.darkweb_group) AS g_darkweb,

        SUM(COALESCE(f.amount, 0)) AS total_amount,
        COUNT(f.txn_id) AS txn_count,
        ROUND(AVG(COALESCE(f.amount, 0)), 2) AS avg_amount
    FROM FACT_TRANSACTIONS_V f
    LEFT JOIN DIM_CARD_V c
        ON f.card_id = c.card_id
    GROUP BY CUBE(c.chip_group, c.darkweb_group)
),
security_formatted AS (
    SELECT
        CASE
            WHEN g_chip = 1 THEN '{ALL_CHIP_STATUS}'
            ELSE COALESCE(chip_group, 'UNKNOWN_CHIP_STATUS')
        END AS chip_group,

        CASE
            WHEN g_darkweb = 1 THEN '{ALL_RISK_STATUS}'
            ELSE COALESCE(darkweb_group, 'UNKNOWN_RISK_STATUS')
        END AS darkweb_group,

        total_amount,
        txn_count,
        avg_amount,

        CASE
            WHEN g_chip = 1 AND g_darkweb = 1 THEN 3
            WHEN g_chip = 1 AND g_darkweb = 0 THEN 2
            WHEN g_chip = 0 AND g_darkweb = 1 THEN 1
            ELSE 0
        END AS cube_level,

        CASE
            WHEN chip_group = 'CHIP_ENABLED' THEN 1
            WHEN chip_group = 'NO_CHIP' THEN 2
            WHEN chip_group = 'UNKNOWN_CHIP_STATUS' THEN 3
            ELSE 4
        END AS sort_chip,

        CASE
            WHEN darkweb_group = 'NO_EXPOSURE' THEN 1
            WHEN darkweb_group = 'RISK_EXPOSED' THEN 2
            WHEN darkweb_group = 'UNKNOWN_RISK_STATUS' THEN 3
            ELSE 4
        END AS sort_darkweb
    FROM security_cube
)
SELECT
    chip_group,
    darkweb_group,
    total_amount,
    txn_count,
    avg_amount
FROM security_formatted
ORDER BY
    cube_level,
    sort_chip,
    sort_darkweb;

SELECT * FROM OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V;


--------------------------------------------------------------------------------
--- 4.9 GROUPING SETS: year, state and source system
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V AS
WITH year_state_source_gsets AS (
    SELECT
        t.txn_year,
        g.state_group,
        f.source_system,

        GROUPING(t.txn_year) AS g_year,
        GROUPING(g.state_group) AS g_state,
        GROUPING(f.source_system) AS g_source,

        SUM(COALESCE(f.amount, 0)) AS total_amount,
        COUNT(f.txn_id) AS txn_count
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
),
year_state_source_formatted AS (
    SELECT
        CASE
            WHEN g_year = 1 THEN '{TOTAL_GENERAL}'
            ELSE CAST(txn_year AS STRING)
        END AS txn_year,

        CASE
            WHEN g_year = 1 THEN ' '
            WHEN g_state = 1 THEN CONCAT('subtotal year ', CAST(txn_year AS STRING))
            ELSE COALESCE(state_group, 'UNKNOWN_STATE')
        END AS state_group,

        CASE
            WHEN g_year = 1 THEN ' '
            WHEN g_state = 1 THEN ' '
            WHEN g_source = 1 THEN CONCAT('subtotal state ', COALESCE(state_group, 'UNKNOWN_STATE'))
            ELSE COALESCE(source_system, 'UNKNOWN_SOURCE')
        END AS source_system,

        total_amount,
        txn_count,

        CASE
            WHEN g_year = 1 THEN 9999
            ELSE txn_year
        END AS sort_year,

        CASE
            WHEN g_year = 1 THEN 99
            WHEN g_state = 1 THEN 1
            ELSE 0
        END AS rollup_level,

        COALESCE(state_group, 'ZZZZZZ') AS sort_state,

        CASE
            WHEN source_system = 'POSTGRES' THEN 1
            WHEN source_system = 'MONGO' THEN 2
            ELSE 3
        END AS sort_source
    FROM year_state_source_gsets
)
SELECT
    txn_year,
    state_group,
    source_system,
    total_amount,
    txn_count
FROM year_state_source_formatted
ORDER BY
    sort_year,
    rollup_level,
    sort_state,
    sort_source;

SELECT * FROM OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V;


--------------------------------------------------------------------------------
--- 4.10 GROUPING SETS: month, income group and MCC
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V AS
WITH month_income_mcc_gsets AS (
    SELECT
        t.txn_month,
        c.income_group,
        f.mcc,

        GROUPING(t.txn_month) AS g_month,
        GROUPING(c.income_group) AS g_income,
        GROUPING(f.mcc) AS g_mcc,

        SUM(COALESCE(f.amount, 0)) AS total_amount,
        COUNT(f.txn_id) AS txn_count,
        ROUND(AVG(COALESCE(f.amount, 0)), 2) AS avg_amount
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
),
month_income_mcc_formatted AS (
    SELECT
        CASE
            WHEN g_month = 1 THEN '{TOTAL_GENERAL}'
            ELSE CAST(txn_month AS STRING)
        END AS txn_month,

        CASE
            WHEN g_month = 1 THEN ' '
            WHEN g_income = 1 THEN CONCAT('subtotal month ', CAST(txn_month AS STRING))
            ELSE COALESCE(income_group, 'UNKNOWN_INCOME')
        END AS income_group,

        CASE
            WHEN g_month = 1 THEN ' '
            WHEN g_income = 1 THEN ' '
            WHEN g_mcc = 1 THEN CONCAT('subtotal income ', COALESCE(income_group, 'UNKNOWN_INCOME'))
            ELSE CAST(mcc AS STRING)
        END AS mcc,

        total_amount,
        txn_count,
        avg_amount,

        CASE
            WHEN g_month = 1 THEN 99
            ELSE txn_month
        END AS sort_month,

        CASE
            WHEN g_month = 1 THEN 99
            WHEN g_income = 1 THEN 1
            ELSE 0
        END AS rollup_level,

        CASE
            WHEN income_group = 'LOW_INCOME' THEN 1
            WHEN income_group = 'MEDIUM_INCOME' THEN 2
            WHEN income_group = 'HIGH_INCOME' THEN 3
            WHEN income_group = 'UNKNOWN_INCOME' THEN 4
            ELSE 5
        END AS sort_income,

        COALESCE(mcc, 999999) AS sort_mcc
    FROM month_income_mcc_gsets
)
SELECT
    txn_month,
    income_group,
    mcc,
    total_amount,
    txn_count,
    avg_amount
FROM month_income_mcc_formatted
ORDER BY
    sort_month,
    rollup_level,
    sort_income,
    sort_mcc;

SELECT * FROM OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V;


--------------------------------------------------------------------------------
--- 5. WINDOW ANALYTICAL VIEWS --------------------------------------------------
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 5.1 Running total by client
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW WV_TXN_RUNNING_TOTAL_CLIENT_V AS
SELECT
    f.client_id,
    f.txn_timestamp,
    f.txn_id,
    f.amount,
    SUM(f.amount) OVER (
        PARTITION BY f.client_id
        ORDER BY f.txn_timestamp, f.txn_id
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS running_total_client
FROM FACT_TRANSACTIONS_V f
ORDER BY
    f.client_id,
    f.txn_timestamp,
    f.txn_id;

SELECT * FROM WV_TXN_RUNNING_TOTAL_CLIENT_V LIMIT 50;


--------------------------------------------------------------------------------
--- 5.2 Client average and transaction deviation from client average
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW WV_TXN_CLIENT_AVG_DIFF_V AS
SELECT
    f.client_id,
    f.txn_id,
    f.txn_timestamp,
    f.amount,
    ROUND(
        AVG(f.amount) OVER (PARTITION BY f.client_id),
        2
    ) AS avg_amount_per_client,
    ROUND(
        f.amount - AVG(f.amount) OVER (PARTITION BY f.client_id),
        2
    ) AS diff_from_client_avg
FROM FACT_TRANSACTIONS_V f
ORDER BY
    f.client_id,
    f.txn_timestamp,
    f.txn_id;

SELECT * FROM WV_TXN_CLIENT_AVG_DIFF_V LIMIT 50;


--------------------------------------------------------------------------------
--- 5.3 Card ranking by total amount
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW WV_CARD_TOTAL_RANK_V AS
SELECT
    x.card_id,
    x.total_amount,
    RANK() OVER (ORDER BY x.total_amount DESC) AS rank_card,
    DENSE_RANK() OVER (ORDER BY x.total_amount DESC) AS dense_rank_card,
    ROW_NUMBER() OVER (ORDER BY x.total_amount DESC) AS row_number_card
FROM (
    SELECT
        f.card_id,
        SUM(f.amount) AS total_amount
    FROM FACT_TRANSACTIONS_V f
    GROUP BY f.card_id
) x
ORDER BY
    rank_card,
    x.card_id;

SELECT * FROM WV_CARD_TOTAL_RANK_V LIMIT 50;


--------------------------------------------------------------------------------
--- 5.4 Transaction share in monthly total and monthly running total
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW WV_TXN_MONTH_SHARE_RUNNING_V AS
SELECT
    t.txn_year,
    t.txn_month,
    f.txn_timestamp,
    f.txn_id,
    f.client_id,
    f.amount,

    SUM(f.amount) OVER (
        PARTITION BY t.txn_year, t.txn_month
    ) AS total_month_amount,

    ROUND(
        CASE
            WHEN SUM(f.amount) OVER (PARTITION BY t.txn_year, t.txn_month) = 0 THEN 0
            ELSE 100 * f.amount / SUM(f.amount) OVER (PARTITION BY t.txn_year, t.txn_month)
        END,
        2
    ) AS pct_of_month_total,

    SUM(f.amount) OVER (
        PARTITION BY t.txn_year, t.txn_month
        ORDER BY f.txn_timestamp, f.txn_id
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS running_total_month
FROM FACT_TRANSACTIONS_V f
JOIN DIM_TIME_V t
    ON f.txn_date = t.txn_date
ORDER BY
    t.txn_year,
    t.txn_month,
    f.txn_timestamp,
    f.txn_id;

SELECT * FROM WV_TXN_MONTH_SHARE_RUNNING_V LIMIT 50;


--------------------------------------------------------------------------------
--- 5.5 First / last transaction value by client and top transactions per client
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW WV_TXN_CLIENT_FIRST_LAST_TOP_V AS
SELECT
    f.client_id,
    f.txn_timestamp,
    f.txn_id,
    f.amount,

    FIRST_VALUE(f.amount) OVER (
        PARTITION BY f.client_id
        ORDER BY f.txn_timestamp, f.txn_id
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) AS first_amount_client,

    LAST_VALUE(f.amount) OVER (
        PARTITION BY f.client_id
        ORDER BY f.txn_timestamp, f.txn_id
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
    ) AS last_amount_client,

    ROW_NUMBER() OVER (
        PARTITION BY f.client_id
        ORDER BY f.amount DESC, f.txn_timestamp, f.txn_id
    ) AS top_txn_rank_per_client
FROM FACT_TRANSACTIONS_V f
ORDER BY
    f.client_id,
    top_txn_rank_per_client,
    f.txn_timestamp,
    f.txn_id;

SELECT * FROM WV_TXN_CLIENT_FIRST_LAST_TOP_V LIMIT 50;


--------------------------------------------------------------------------------
--- 5.6 Merchant ranking within each state
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW WV_MERCHANT_STATE_RANK_V AS
SELECT
    x.state_group,
    x.city_group,
    x.merchant_id,
    x.total_amount,
    x.txn_count,

    RANK() OVER (
        PARTITION BY x.state_group
        ORDER BY x.total_amount DESC
    ) AS rank_in_state,

    DENSE_RANK() OVER (
        PARTITION BY x.state_group
        ORDER BY x.total_amount DESC
    ) AS dense_rank_in_state,

    ROW_NUMBER() OVER (
        PARTITION BY x.state_group
        ORDER BY x.total_amount DESC, x.merchant_id
    ) AS row_number_in_state
FROM (
    SELECT
        g.state_group,
        g.city_group,
        f.merchant_id,
        SUM(f.amount) AS total_amount,
        COUNT(f.txn_id) AS txn_count
    FROM FACT_TRANSACTIONS_V f
    JOIN DIM_MERCHANT_GEO_V g
        ON f.merchant_id = g.merchant_id
    GROUP BY
        g.state_group,
        g.city_group,
        f.merchant_id
) x
ORDER BY
    x.state_group,
    rank_in_state,
    x.merchant_id;

SELECT * FROM WV_MERCHANT_STATE_RANK_V LIMIT 50;


--------------------------------------------------------------------------------
--- 5.7 Credit score monthly performance
--- Final ordered version
--------------------------------------------------------------------------------

CREATE OR REPLACE VIEW WV_CREDIT_MONTH_PERFORMANCE_V AS
SELECT
    x.txn_year,
    x.txn_month,
    x.credit_score_group,
    x.total_amount,
    x.txn_count,

    ROUND(
        AVG(x.total_amount) OVER (
            PARTITION BY x.credit_score_group
        ),
        2
    ) AS avg_total_per_score_group,

    SUM(x.total_amount) OVER (
        PARTITION BY x.txn_year, x.txn_month
    ) AS total_month_amount,

    ROUND(
        CASE
            WHEN SUM(x.total_amount) OVER (PARTITION BY x.txn_year, x.txn_month) = 0 THEN 0
            ELSE 100 * x.total_amount / SUM(x.total_amount) OVER (PARTITION BY x.txn_year, x.txn_month)
        END,
        2
    ) AS pct_of_month_total
FROM (
    SELECT
        t.txn_year,
        t.txn_month,
        c.credit_score_group,
        SUM(f.amount) AS total_amount,
        COUNT(f.txn_id) AS txn_count
    FROM FACT_TRANSACTIONS_V f
    JOIN DIM_CLIENT_V c
        ON f.client_id = c.client_id
    JOIN DIM_TIME_V t
        ON f.txn_date = t.txn_date
    GROUP BY
        t.txn_year,
        t.txn_month,
        c.credit_score_group
) x
ORDER BY
    x.txn_year,
    x.txn_month,
    CASE
        WHEN x.credit_score_group = 'POOR' THEN 1
        WHEN x.credit_score_group = 'FAIR' THEN 2
        WHEN x.credit_score_group = 'GOOD' THEN 3
        WHEN x.credit_score_group = 'VERY_GOOD' THEN 4
        WHEN x.credit_score_group = 'EXCELLENT' THEN 5
        WHEN x.credit_score_group = 'UNKNOWN_SCORE' THEN 6
        ELSE 7
    END;

SELECT * FROM WV_CREDIT_MONTH_PERFORMANCE_V LIMIT 50;


--------------------------------------------------------------------------------
--- 6. FINAL VALIDATION -------------------------------------------------
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 6.1 View existence - Integration, Facts, Dimensions
--------------------------------------------------------------------------------

SELECT COUNT(*) AS cnt FROM INT_CUSTOMER_PROFILE_V;
SELECT COUNT(*) AS cnt FROM INT_CARD_PROFILE_V;
SELECT COUNT(*) AS cnt FROM INT_TRANSACTIONS_BASE_V;
SELECT COUNT(*) AS cnt FROM DIM_CLIENT_V;
SELECT COUNT(*) AS cnt FROM DIM_CARD_V;
SELECT COUNT(*) AS cnt FROM DIM_TIME_V;
SELECT COUNT(*) AS cnt FROM DIM_MERCHANT_GEO_V;
SELECT COUNT(*) AS cnt FROM FACT_TRANSACTIONS_V;
SELECT COUNT(*) AS cnt FROM FACT_TRANSACTIONS_ENRICHED_V;


--------------------------------------------------------------------------------
--- 6.2 Transactions by source
--------------------------------------------------------------------------------

SELECT
    source_system,
    COUNT(*) AS total_rows,
    COUNT(DISTINCT txn_id) AS distinct_transactions
FROM INT_TRANSACTIONS_BASE_V
GROUP BY source_system
ORDER BY source_system;


--------------------------------------------------------------------------------
--- 6.3 Distinct days per month
--------------------------------------------------------------------------------

SELECT
    txn_year,
    txn_month,
    COUNT(*) AS distinct_days
FROM DIM_TIME_V
GROUP BY txn_year, txn_month
ORDER BY txn_year, txn_month;


--------------------------------------------------------------------------------
--- 6.4 Transactions per month
--------------------------------------------------------------------------------

SELECT
    YEAR(txn_date) AS txn_year,
    MONTH(txn_date) AS txn_month,
    COUNT(*) AS total_transactions
FROM INT_TRANSACTIONS_BASE_V
WHERE txn_date IS NOT NULL
GROUP BY YEAR(txn_date), MONTH(txn_date)
ORDER BY txn_year, txn_month;


--------------------------------------------------------------------------------
--- 6.5 Transactions per month and source system
--------------------------------------------------------------------------------

SELECT
    YEAR(txn_date) AS txn_year,
    MONTH(txn_date) AS txn_month,
    source_system,
    COUNT(*) AS total_transactions
FROM INT_TRANSACTIONS_BASE_V
WHERE txn_date IS NOT NULL
GROUP BY YEAR(txn_date), MONTH(txn_date), source_system
ORDER BY txn_year, txn_month, source_system;


--------------------------------------------------------------------------------
--- 7. COMPLETE VIEW LIST -------------------------------------------------------
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- 7.1 Source / Remote SparkSQL Views
--------------------------------------------------------------------------------

-- card_limits_view
-- card_security_view
-- transactions_view
-- cards_jpa_view
-- merchants_jpa_view
-- CUSTOMER_VIEW
-- CUSTOMER_FINANCE_VIEW
-- transactions_mongo_view
-- customer_risk_mongo_view
-- customer_risk_cards_mongo_view
-- transactions_customer_risk_mongo_view


--------------------------------------------------------------------------------
--- 7.2 Integration Views
--------------------------------------------------------------------------------

-- INT_CUSTOMER_PROFILE_V
-- INT_CARD_PROFILE_V
-- INT_TRANSACTIONS_BASE_V


--------------------------------------------------------------------------------
--- 7.3 Dimensional Views
--------------------------------------------------------------------------------

-- DIM_CLIENT_V
-- DIM_CARD_V
-- DIM_TIME_V
-- DIM_MERCHANT_GEO_V


--------------------------------------------------------------------------------
--- 7.4 Fact Views
--------------------------------------------------------------------------------

-- FACT_TRANSACTIONS_V
-- FACT_TRANSACTIONS_ENRICHED_V


--------------------------------------------------------------------------------
--- 7.5 OLAP Analytical Views
--------------------------------------------------------------------------------

-- OLAP_VIEW_TXN_CALENDAR_V
-- OLAP_VIEW_TXN_MERCHANT_GEO_V
-- OLAP_VIEW_TXN_CREDIT_AGE_V
-- OLAP_VIEW_TXN_CARD_BRAND_TYPE_V
-- OLAP_VIEW_TXN_SOURCE_CHANNEL_V
-- OLAP_VIEW_TXN_STATE_BRAND_CUBE_V
-- OLAP_VIEW_TXN_INCOME_DEBT_V
-- OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V
-- OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V
-- OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V


--------------------------------------------------------------------------------
--- 7.6 Window Analytical Views
--------------------------------------------------------------------------------

-- WV_TXN_RUNNING_TOTAL_CLIENT_V
-- WV_TXN_CLIENT_AVG_DIFF_V
-- WV_CARD_TOTAL_RANK_V
-- WV_TXN_MONTH_SHARE_RUNNING_V
-- WV_TXN_CLIENT_FIRST_LAST_TOP_V
-- WV_MERCHANT_STATE_RANK_V
-- WV_CREDIT_MONTH_PERFORMANCE_V


--------------------------------------------------------------------------------
--- 8. SPARKSQL REST ENDPOINT TBD ------------------------------------------
--------------------------------------------------------------------------------

-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_CALENDAR_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_MERCHANT_GEO_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_CREDIT_AGE_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_CARD_BRAND_TYPE_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_SOURCE_CHANNEL_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_STATE_BRAND_CUBE_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_INCOME_DEBT_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_CARD_SECURITY_CUBE_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_YEAR_STATE_SOURCE_GSETS_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/OLAP_VIEW_TXN_MONTH_INCOME_MCC_GSETS_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_TXN_RUNNING_TOTAL_CLIENT_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_TXN_CLIENT_AVG_DIFF_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_CARD_TOTAL_RANK_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_TXN_MONTH_SHARE_RUNNING_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_TXN_CLIENT_FIRST_LAST_TOP_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_MERCHANT_STATE_RANK_V
-- http://localhost:9990/DSA-SparkSQL-Service/rest/view/WV_CREDIT_MONTH_PERFORMANCE_V