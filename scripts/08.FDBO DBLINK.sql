DROP DATABASE LINK PG;

CREATE DATABASE LINK PG
   CONNECT TO "payments" IDENTIFIED BY "payments"
   USING '(DESCRIPTION =
     (ADDRESS = (PROTOCOL = TCP)(HOST = localhost)(PORT = 1521))
     (CONNECT_DATA =
       (SID = PG)
     )
     (HS = OK)
   )';
   
   SELECT * FROM user_db_links;
   
SELECT db_link, username, host
FROM user_db_links
WHERE db_link = 'PG';

SELECT COUNT(*) FROM "cards"@PG;
SELECT * FROM "cards"@PG;