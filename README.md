
# Spring Boot multi-tenant sample app

This sample app uses the [Spring Boot starter library from Quantics](https://github.com/quantics-io/multitenant-oauth2-spring-boot-starter) 
which contains all the configuration options that are required to build a multi-tenant Spring Boot application.

There are two working examples in this repo which can be activated via Spring profiles:

| Spring Profile                                         | Tenant resolution mode            |
|--------------------------------------------------------|-----------------------------------|
| [default](src/main/resources/application.properties)   | Header (using custom HTTP header) |
| [prod](src/main/resources/application-prod.properties) | JWT (using OAuth2 issuer claim)   |


The code in this repository accompanies the following blog posts:
- [How to build a multi-tenant SaaS solution: A Spring Boot sample app](https://jomatt.io/how-to-build-a-multi-tenant-saas-solution-sample-app/)
- [How to make your multi-tenant Spring app production-ready](https://jomatt.io/how-to-make-your-multi-tenant-spring-app-production-ready/)

## test
```bash
java -jar target/spring-boot-multitenant-sample-0.1.0.jar
```

## prod
```bash
java -jar target/spring-boot-multitenant-sample-0.1.0.jar --spring.profiles.active=prod
```
* dukes tenant: Log in with `isabelle/password`. 
* beans tenant: Log in with `bjorn/password`.  





## Unit test
```bash
mvn clean test -Dtest="io.jomatt.multitenant.sample.header.MultiTenantHeaderApplicationTests"
mvn clean test -Dtest="io.jomatt.multitenant.sample.jwt.MultiTenantJwtApplicationTests"
```