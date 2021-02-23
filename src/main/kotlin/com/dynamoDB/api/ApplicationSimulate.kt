package com.dynamoDB.api

import com.dynamoDB.integration.dynamodb.SynchronousDynamoDbClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.* // ktlint-disable no-wildcard-imports
import javax.inject.Inject

fun main() {
}

class Metodos() {
    @Inject
    private lateinit var dynamoDbClient: DynamoDbClient



    fun createTable(tableName: String, key: String): String {
        val dbWaiter = dynamoDbClient.waiter()
        val request = CreateTableRequest.builder()
            .attributeDefinitions(
                AttributeDefinition.builder()
                    .attributeName(key)
                    .attributeType(ScalarAttributeType.S)
                    .build()
            )
            .keySchema(
                KeySchemaElement.builder()
                    .attributeName(key)
                    .keyType(KeyType.HASH)
                    .build()
            )
            .provisionedThroughput(
                ProvisionedThroughput.builder()
                    .readCapacityUnits(10)
                    .writeCapacityUnits(10)
                    .build()
            )
            .tableName(tableName)
            .build()

        val newTable: String
        return try {
            val response = dynamoDbClient.createTable(request)
            val tableRequest = DescribeTableRequest.builder()
                .tableName(tableName)
                .build()

            // Wait until the Amazon DynamoDB table is created
            val waiterResponse = dbWaiter.waitUntilTableExists(tableRequest)
            waiterResponse.matched().response().ifPresent { println(it) }
            newTable = response.tableDescription().tableName()
            newTable
        } catch (e: DynamoDbException) {
            System.err.println(e)
            ""
        }
    }
}
