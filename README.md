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

### Technical approaches assumptions

- Behaviour Driven Development
- Package granulation
- Rich Domain Model

##Setup

mvn clean install 
mvn spring-boot:run

### REST API
<b>http://localhost:8080/swagger-ui.html</b>

Whole rental process is based on temporary <b>RentalBox</b> which lasts with http session.
Steps placed chronological:
- Add film with declared number of days to rental box 
- Get details of rental box
- Remove film from rental box
- Confirm rental
- Return film

Each film must be returned independently

Besides that:
- Get films
- Get film details
- Create customer
- Get customer details  

Examples of usage are placed in <b>examples</b> folder


REST API intentionally without authentication 

