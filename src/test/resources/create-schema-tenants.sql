CREATE TABLE IF NOT EXISTS tenant (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    schema VARCHAR(255) NOT NULL,
    issuer VARCHAR(255) NOT NULL 
);
CREATE TABLE IF NOT EXISTS  usr ("name" varchar PRIMARY KEY); 
 
CREATE SCHEMA IF NOT EXISTS tenant1;
CREATE SCHEMA IF NOT EXISTS tenant2; 