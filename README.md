1. Download the latest postgreSQL from 
    # https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

2. Go through the installation process. If you don't want to set anything inside the project configuration, set the password to "admin".
You don't have to install any additional software, so you can skip it in the installation process by clicking "cancel".

3. Open a tool for DB administration (In my case DBeaver). Configure a default connection:
    - host: localhost
    - port: 5432
    - database: postgres (you can leave it empty)
    - user: postgres
    - password: admin

4. When connected, create script and execute this query:
    #  create database mss_local_db with encoding 'UTF-8'; 

5. Connect to mss_local_db (create new connection):
    - host: localhost
    - port: 5432
    - database: mss_local_db
    - user: postgres
    - password: admin

6. Execute queries from these files: 
    - PostgreSQL_DB_Schema_Creation.sql
    - PostgreSQL_Data_Insertion.sql
    which you can find in "sql" folder.

7. DB created. Now to run the application, execute the command in cmd, in the project main directory (backend-mss):
    # gradlew bootRun 