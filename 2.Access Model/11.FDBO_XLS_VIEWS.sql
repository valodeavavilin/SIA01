
SELECT directory_name, directory_path
FROM all_directories
WHERE directory_name = 'EXT_FILE_DS';

SELECT *
FROM all_tab_privs
WHERE table_name = 'EXT_FILE_DS'
  AND grantee = 'FDBO';
  
  --dupa ce am rulat install.sql
  SELECT object_name, object_type, status
FROM user_objects
WHERE object_name IN (
    'XUTL_CDF',
    'XUTL_OFFCRYPTO',
    'EXCELTYPES',
    'XUTL_XLS',
    'XUTL_XLSB',
    'XUTL_FLATFILE',
    'EXCELTABLE',
    'EXCELTABLEIMPL',
    'EXCELTABLESHEETLIST',
    'EXCELTABLECELL',
    'EXCELTABLECELLLIST'
)
ORDER BY object_type, object_name;

select * from V_XLS_CUSTOMERS

SELECT *
FROM TABLE(
  ExcelTable.getSheets(
    ExcelTable.getFile('EXT_FILE_DS', 'crm.xlsx')
  )
);

SELECT object_name, procedure_name
FROM user_procedures
WHERE object_name = 'EXCELTABLE'
ORDER BY procedure_name;



SELECT 
    package_name,
    object_name,
    argument_name,
    position,
    data_type,
    in_out
FROM user_arguments
WHERE package_name = 'EXCELTABLE'
  AND object_name = 'GETROWS'
ORDER BY overload, sequence;



SELECT ExcelTable.getFile('EXT_FILE_DS','crm.xlsx')
FROM dual;

SELECT *
FROM TABLE(
  ExcelTable.getSheets(
    ExcelTable.getFile('EXT_FILE_DS', 'crm.xlsx')
  )
);



CREATE OR REPLACE VIEW V_XLS_CUSTOMERS AS
SELECT *
FROM TABLE(
  ExcelTable.getRows(
    ExcelTable.getFile('EXT_FILE_DS', 'crm.xlsx'),
    'customers',
    q'[
      "CLIENT_ID"      NUMBER         column 'A',
      "CURRENT_AGE"    NUMBER         column 'B',
      "RETIREMENT_AGE" NUMBER         column 'C',
      "BIRTH_YEAR"     NUMBER         column 'D',
      "BIRTH_MONTH"    NUMBER         column 'E',
      "GENDER"         VARCHAR2(20)   column 'F',
      "ADDRESS"        VARCHAR2(200)  column 'G',
      "LATITUDE"       NUMBER         column 'H',
      "LONGITUDE"      NUMBER         column 'I'
    ]',
    'A2:I10000',
    1,
    NULL
  )
);
select * from V_XLS_CUSTOMERS

SELECT object_name, object_type
FROM user_objects
WHERE object_type LIKE '%JAVA%';

CREATE OR REPLACE VIEW V_XLS_CUSTOMER_FINANCE_RAW AS
SELECT *
FROM TABLE(
  ExcelTable.getRows(
    ExcelTable.getFile('EXT_FILE_DS', 'crm.xlsx'),
    'customer_finance',
    q'[
      "CLIENT_ID"          VARCHAR2(50)   column 'A',
      "PER_CAPITA_INCOME"  VARCHAR2(50)   column 'B',
      "YEARLY_INCOME"      VARCHAR2(50)   column 'C',
      "TOTAL_DEBT"         VARCHAR2(50)   column 'D',
      "CREDIT_SCORE"       VARCHAR2(50)   column 'E',
      "NUM_CREDIT_CARDS"   VARCHAR2(50)   column 'F'
    ]',
    'A2:F10000',
    1,
    NULL
  )
);


CREATE OR REPLACE VIEW V_XLS_CUSTOMER_FINANCE AS
SELECT
    TO_NUMBER(CLIENT_ID) AS CLIENT_ID,
    TO_NUMBER(REPLACE(PER_CAPITA_INCOME, '$', '')) AS PER_CAPITA_INCOME,
    TO_NUMBER(REPLACE(YEARLY_INCOME, '$', '')) AS YEARLY_INCOME,
    TO_NUMBER(REPLACE(TOTAL_DEBT, '$', '')) AS TOTAL_DEBT,
    TO_NUMBER(CREDIT_SCORE) AS CREDIT_SCORE,
    TO_NUMBER(NUM_CREDIT_CARDS) AS NUM_CREDIT_CARDS
FROM V_XLS_CUSTOMER_FINANCE_RAW;


SELECT * FROM V_XLS_CUSTOMER_FINANCE;



SELECT COUNT(*) FROM V_XLS_CUSTOMER_FINANCE;


select * from V_XLS_CUSTOMERS C JOIN V_XLS_CUSTOMER_FINANCE CF ON C.CLIENT_ID=CF.CLIENT_ID;


