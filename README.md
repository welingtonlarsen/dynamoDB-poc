<div align="center">

![](https://img.shields.io/badge/Status-Done-brightgreen)
</div>

<div align="center">

# DynamoDB - POC
This is a CRUD using DynamoDB to understand more about this technology.

![](https://img.shields.io/badge/Autor-Welington%20Larsen-brightgreen)
![](https://img.shields.io/badge/Language-Kotlin-brightgreen)
![](https://img.shields.io/badge/Framework-Micronaut-brightgreen)
![](https://img.shields.io/badge/Database-DynamoDB-brightgreen)

</div> 

## Technologies
- Kotlin 1.4.30
- Micronaut 2.3.2
    - micronaut-kotlin-runtime
    - micronaut-test-kotest
    - micronaut-aws-sdk-v2
- DynamoDB
    - software-amazon-awssdk-dynamodb
- Docker
- Localstack
- Jdk 8
- Aws

## Execution
- Scripts
  ### Configure your aws cli:
      - region: sa-east-1
      - accessKeyId: test-key
      - secretKey: test-secret
  ### Start the Localstack:
      - Command: ```localstack start```
  ### Run docker-compose
    - 1° command: ``` cd dynamoDb-poc```
    - 2° command: ```docker-compose -f docker-compose.yml up```
  ### Run the application
    - 1° command: ``` ./gradlew clean build```
    - 2° command: ```./gradlew run```
    