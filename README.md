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
  "balance": 0
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
  "accountId": "c6b100a5-2f7e-41a7-8b1b-6998c7f32d4c",
  "targetAccountId": null,
  "amount": 1000,
  "type": "DEPOSIT",
  "balanceBefore": 0.00,
  "balanceAfter": 1005.00,
  "createDateTime": 1611023784.262992600
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
  "accountId": "c6b100a5-2f7e-41a7-8b1b-6998c7f32d4c",
  "targetAccountId": null,
  "amount": 800,
  "type": "TAKEOUT",
  "balanceBefore": 1005.00,
  "balanceAfter": 197.00,
  "createDateTime": 1611023803.577546600
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
  "accountId": "f09976af-8e4a-42b6-9082-8bfa358d023b",
  "targetAccountId": "c6b100a5-2f7e-41a7-8b1b-6998c7f32d4c",
  "amount": 100,
  "type": "TRANSFER",
  "balanceBefore": 1005.00,
  "balanceAfter": 905.00,
  "createDateTime": 1611023836.251815100
}
```

**`GET -> /accounts/find?document={$DocumentoCPF}`**

Return an account with some infos and the balance.

**Return:**
```json
{
  "id": "f09976af-8e4a-42b6-9082-8bfa358d023b",
  "name": "Gustavo",
  "document": "42392545073",
  "balance": 905.00
}
```
