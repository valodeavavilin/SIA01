CREATE OR REPLACE VIEW V_MONGO_CUSTOMER_RISK AS
SELECT jt.*
FROM JSON_TABLE(
       get_mongo_json('http://localhost:8081/mds/customer_risk_docs'),
       '$[*]'
       COLUMNS (
         client_id        NUMBER         PATH '$.client_id',
         credit_score     NUMBER         PATH '$.credit_score',
         yearly_income    VARCHAR2(50)   PATH '$.yearly_income',
         total_debt       VARCHAR2(50)   PATH '$.total_debt',
         num_credit_cards NUMBER         PATH '$.num_credit_cards',
         tx_count         NUMBER         PATH '$.tx_count',
         total_amount     NUMBER         PATH '$.total_amount',
         top_mcc          NUMBER         PATH '$.top_mcc'
       )
     ) jt;
-------
SELECT * FROM V_MONGO_CUSTOMER_RISK;
-------------------
CREATE OR REPLACE VIEW V_MONGO_TRANSACTIONS AS
SELECT jt.*
FROM JSON_TABLE(
       get_mongo_json('http://localhost:8081/mds/transaction_docs'),
       '$[*]'
       COLUMNS (
         txn_id       NUMBER        PATH '$.txn_id',
         ts           VARCHAR2(30)  PATH '$.ts',
         client_id    NUMBER        PATH '$.client_id',
         card_id      NUMBER        PATH '$.card_id',
         amount       NUMBER        PATH '$.amount',
         channel      VARCHAR2(50)  PATH '$.channel',
         merchant_id  NUMBER        PATH '$.merchant.id',
         city         VARCHAR2(50)  PATH '$.merchant.city',
         state        VARCHAR2(10)  PATH '$.merchant.state',
         zip          VARCHAR2(10)  PATH '$.merchant.zip',
         mcc          NUMBER        PATH '$.mcc'
       )
     ) jt;
---------------     
SELECT * FROM V_MONGO_TRANSACTIONS;
------------------ INTEROGARE MONGO + XLS 
SELECT c.client_id,
       c.current_age,
       r.credit_score,
       r.total_amount
FROM V_XLS_CUSTOMERS c
JOIN V_MONGO_CUSTOMER_RISK r
ON c.client_id = r.client_id;


-------------  INTEROGARE XLS, MONGO, POSTGRES
SELECT
    x.client_id,
    x.current_age,
    x.gender,
    f.yearly_income,
    f.credit_score AS excel_credit_score,
    m.credit_score AS mongo_credit_score,
    m.total_amount,
    m.tx_count,
    p.retirement_age,
    p.address
FROM V_XLS_CUSTOMERS x
JOIN V_XLS_CUSTOMER_FINANCE f
    ON x.client_id = f.client_id
JOIN V_MONGO_CUSTOMER_RISK m
    ON x.client_id = m.client_id
JOIN CUSTOMERS_V p
    ON x.client_id = p.client_id;