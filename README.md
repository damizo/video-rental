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

### Setup

mvn clean install -Ptest
mvn spring-boot:run -Dspring.profiles.active=test

## Profiles
<b>test</b>
 - run integration and acceptance tests during build
 - initialize inventory with few films and customer in database

### REST API
<b>http://localhost:8080/swagger-ui.html</b>

Whole rental process is based on temporary <b>RentalBox</b> which lasts among client http session.
Find description below how to reproduce rental process, step by step:

- Add film to rental box 
- Confirm rental order
- Return film
- Fetch details about rental order

Each film must be returned independently from order

Besides that API offers few other endpoints:
- Add film
- Get films
- Get film details
- Create customer
- Get customer details  

Examples of usage are placed in <b>examples</b> folder

REST API is intentionally:
- without authentication/authorization 
- without pagination

### Why...

Is there HttpSession param in few endpoints?

<b> I wanted to bound temporary rental box with client http session, as it works in online shops with carts. 
Box has no business value, that's why I don't save it database. It becomes important business value when client 
confirms his rental draft.</b>

Is there 'customerId' param required in <b>POST @ /api/rental</b>, someone might pass id of any client in database

<b> I agree, I wanted to simplify it. Authentication would resolve it, because I would get customerId from customer which
 is currently logged in.
Additionally, with authentication some endpoints (e.g. create new customer, add new film) should be strictly protected 
with corresponding permissions</b>


