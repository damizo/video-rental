

  
# Casumo Recruitment Task    
 ## Introduction    
 The key of resolving this task is to decide on which level you should place hermetization in terms of calculation of     
rentals days and surcharge. I think that the best way to implement solution is to place calculation strategies in enum.     
    
Why I decide on such move:    
- I wanted to avoid additional DI and configuration in IoC container     
- No ifology or switch cases in order to choose proper strategy depended on film type    
- Our domain model becomes rich     
- Very easy parameterization of prices    
- Ready to split calculation on first payment and surcharge     
  
I focused on business problem, usability, scalability (no overengineering) and well written, detailed tests.      

## Technologies    
 - Java 8    
- Spring Boot    
- Lombok    
- H2    
- Spock    
- Swagger    
    
## Technical approaches    
 - Behaviour Driven Development    
- Rich Domain Model and package granulation oriented on domain (borrowed from DDD)    
    
## Setup    
    
	   mvn clean install -Ptest    
	   mvn spring-boot:run -Dspring.profiles.active=test    
 ## Profiles 
<b>test</b>    
	 - runs integration and acceptance tests during build    
    - initializes inventory with few films and customer in database    
    
## REST API     

Please find described endpoints below:
<b>http://localhost:8080/swagger-ui.html</b>    
    
Whole rental process is based on temporary <b>RentalOrderDraft</b> which lasts among http session and is stored in <b>RentalBox</b>. <b>RentalBox</b> is a storage, that contains <b>Rental</b> choices, chosen by <b>Customer</b>. If <b> Customer</b> decides to rent all films from <b>RentalBox</b>, then it becomes <b>RentalOrder</b>, which aggregates all <b>Rental</b> positions.
  
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
    
Postman collection with examples is in <b>examples</b> folder    
    
REST API is intentionally:    
- without authentication/authorization     
- without pagination    
    
## Why...    
 Q: Are you using HttpSession to store films?    
A: <b> I think that connecting temporary rental box with client http session is scalable and causes that customer has more control about what he wants to rent. Box has no specific business value, that's why I don't save it database. It becomes important business value when client confirms his rental draft (all films from rental box).</b>    
    
Q: Is there 'customerId' param required in <b>POST @ /api/rental</b>, someone might pass any id and assign rental to any customer.  
A: <b> I agree, I wanted to simplify it. Authentication/authorization would resolve it, I would get customerId from customer which  is currently logged in.  Additionally, with authentication/authorization some endpoints (e.g. create new customer, add new film) should be strictly protected  with corresponding permissions. I skipped that part.</b>    
    
    
Q: ConcurrentHashMap for tests instead of H2?    
A: <b>H2 is additional weight for Spring Context, I used minimmum configuration in order to make all tests run faster.</b>
