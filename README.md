# Overview

Vending Machine Api - coding exercise.

Spring Boot 3, Java 17, Maven, H2 in memory DB

Caveats:
- The authentication mechanism is Oauth2, but token has no expiry logic
- In memory DB means all data disappears after the application shuts down
- did not implement deposit and buy endpoint

# Potential Improvements
- could implement a real JWT token authentication
- needs better coverage
- could use docker containers (compose) for a postgres DB
- could use testcontainers for integration tests for testing persistence
- could use flyway for migration files and initialise the data
- could use Spring Security instead of AOP
- could replace the regex for checking multiple of 5 by implementing org.springframework.validation.Validator

# Getting Started

`mvn spring-boot:run`

# Testing

### Register as a Seller

```
curl --location --request POST 'localhost:8080/users' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "seller",
    "password": "1234",
    "role": "SELLER"
}'
```

### Login as a Seller
```
curl --location --request POST 'localhost:8080/auth/login?username=seller&password=1234' \
--header 'Content-Type: application/json' \
--data-raw ''
```

### Logout all (expires all tokens)
```
curl --location --request POST 'localhost:8080/auth/logout/all' \
--header 'Content-Type: application/json' \
--data-raw ''
```

### Fetch all profucts as a Buyer
```
curl --location --request GET 'localhost:8080/products' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer buyerc33091...26c' \
--data-raw ''
```

### Add a product as a Seller
```
curl --location --request POST 'localhost:8080/products' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer seller674bbcf...c1b812f2df' \
--data-raw '{
    "productName": "Mars Bar",
    "cost": 0,
    "amountAvailable" : 0
}'
```


### Update a product as a Seller

```
curl --location --request PUT 'localhost:8080/products/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer seller674bbcf...c1b812f2df' \
--data-raw '{
    "productName": "Mars Bar",
    "cost": 5,
    "amountAvailable" : 0
}'
```


### Delete a product as a Seller

```
curl --location --request DELETE 'localhost:8080/products/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer seller674bbcf...c1b812f2df' \
--data-raw ''

```