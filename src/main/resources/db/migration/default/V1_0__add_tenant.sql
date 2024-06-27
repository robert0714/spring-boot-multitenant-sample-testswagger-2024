
CREATE TABLE IF NOT EXISTS tenant (
    "id" varchar NOT NULL
        CONSTRAINT tenant_pk
            PRIMARY KEY,
    "name" varchar NOT NULL
        CONSTRAINT tenant_name_uc
            UNIQUE,
    "schema" varchar NOT NULL
        CONSTRAINT tenant_schema_uc
            UNIQUE,
    "issuer" varchar NOT NULL
        CONSTRAINT tenant_issuer_uc
            UNIQUE
);
INSERT INTO tenant
VALUES ('beans', 'beans', 'BEANS', 'http://localhost:8089/realms/beans'),
       ('dukes', 'dukes', 'DUKES', 'http://localhost:8089/realms/dukes');

