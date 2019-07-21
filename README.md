# Casumo Recruitment Task

### Introduction

The key of resolving this task is to decide on which level you should place hermetization in terms of calculation of 
rentals days and surcharge. I think that the best way to implement such solution is to place calculation strategies in enum. 

Why I decide on such move:
- I wanted to avoid additional DI and configuration in IoC container 
- No ifs or switch cases in order to choose proper strategy depended on film type
- Our domain model becomes rich 
- Easy parameterization of prices
- Ready for split calculation on first payment and surcharge


### Technologies

- Java 8
- Spring Boot
- Lombok
- H2
- Spock
- Swagger

### Technical approaches

- Behaviour Driven Development
- Rich Domain Model and package granulation oriented on domain (borrowed from DDD)

*conscious lack of authentication for simplificity and lack of unit tests for mappers

### Setup

mvn clean install -Ptest
mvn spring-boot:run -Dspring.profiles.active=test

## Profiles
<b>test</b>
 - run integration and acceptance tests during build
 - initialize few films and customer in database

### REST API
<b>http://localhost:8080/swagger-ui.html</b>

Whole rental process is based on temporary <b>RentalBox</b> which lasts with http session.
Steps placed chronological:

- Add film with declared number of days to rental box 
- Confirm rental
- Return film
- Fetch details about rental order

Each film must be returned independently from order

Besides that there is few other endpoints:
- Get films
- Get film details
- Create customer
- Get customer details  

Examples of usage are placed in <b>examples</b> folder


REST API intentionally without authentication 

