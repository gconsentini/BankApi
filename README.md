# BankApi
A Simple Bank Api that allows users to Deposit, takeout and transfer money =)

## Stack

- Java 11
- Springboot
- H2
- Swagger
- Junit5
- Docker, Docker-compose
- Kotlin

## Requirements
- Docker and docker-compose

or

* Java 11
* Kotlin
* Spring Boot
* Docker and Docker-compose

## How to run the app locally

Through Docker, with docker-compose

```docker-compose up --build --force-recreate```

Running locally

```shell
chmod 777 ./gradlew
./gradlew bootRun
```

### Environments

* Locally: http://localhost:8080

* Docker: http://localhost:8089

## Swagger Documentation

* http://localhost:8080/swagger-ui/
* http://localhost:8089/swagger-ui/

## API alguns exemplos

**`POST -> /accounts/create`**

Creates an account

**Body json:**
```json
{
  "name": "Gustavo Consentini",
  "document": "88504758028"
}
```
**Return:**
```json
{
  "name": "Gustavo Consentini",
  "document": "88504758028",
  "balance": 0.0
}
```

**`POST -> /transactions/deposit`**

Makes a deposit on an account

**Body json:**
```json
{
  "documentAccount": "88504758028",
  "amount": "10000"
}
```

**Return:**
```json
{
  "name": "Gustavo Consentini",
  "document": "88504758028"
}
```


**`POST -> /transactions/takeout`**

Withdraw some value from an account

**Body json:**
```json
{
  "documentAccount": "88504758028",
  "amount": "10000"
}
```

**Return:**
```json
{
  "name": "Gustavo Consentini",
  "document": "88504758028"
}
```



**`POST -> /transactions/transfer`**

Makes a transfer of a value between two accounts

**Body json:**
```json
{
  "documentAccount": "88504758028",
  "toDocumentAccount": "84089253004",
  "amount": "100"
}
```

**Return:**
```json
{
  "name": "Gustavo Consentini",
  "document": "88504758028"
}
```

**`GET -> /accounts/find?document={$DocumentoCPF}`**

Return an account with some infos and the balance.

**Return:**
```json
{
  "books": [
    {
      "description": "string",
      "id": 0,
      "isbn": "string",
      "language": "string",
      "title": "string"
    }
  ],
  "numberBooks": 0
}
```
