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

## Execution

The application is executed through a Gradle command that start the Micronaut.

- Scripts
  ### Run docker-compose
    - 1° command: ``` cd dynamoDB-study```
    - 2° command: ```docker-compose -f docker-compose.yml up```
  ### Run the application
    - 1° command: ``` ./gradlew clean build```
    - 2° command: ```./gradlew run```

## Utilização
- Aternativas open source do client para testar serviços gRPC
-  [BloomRPC](https://appimage.github.io/BloomRPC/)
-  [Insomnia](https://insomnia.rest/)
    
