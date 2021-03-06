package com.dynamoDB.integration.dynamodb

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.* // ktlint-disable no-wildcard-imports
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SynchronousDynamoDbClient(@Inject private val dynamoDbClient: DynamoDbClient) {

    fun insertDynamoDbItem(tableName: String, itemValues: Map<String, AttributeValue>): PutItemResponse? {
        val request = PutItemRequest.builder()
            .tableName(tableName)
            .item(itemValues)
            .build()

        return try {
            dynamoDbClient.putItem(request)
        } catch (e: ResourceNotFoundException) {
            print(e.stackTrace)
            null
        } catch (e: DynamoDbException) {
            print(e.stackTrace)
            null
        }
    }

    fun getDynamoDbItem(tableName: String, keyToGet: HashMap<String, AttributeValue>): GetItemResponse? {
        val request = GetItemRequest.builder()
            .tableName(tableName)
            .key(keyToGet)
            .build()

        return try {
            dynamoDbClient.getItem(request)
        } catch (e: DynamoDbException) {
            print(e.stackTrace)
            null
        }
    }

    fun updateDynamoDbItem(tableName: String, key: HashMap<String, AttributeValue>, column: String, newValue: String): UpdateItemResponse? {
        val updateValue = hashMapOf<String, AttributeValueUpdate>()
        updateValue[column] = AttributeValueUpdate.builder()
            .value(
                AttributeValue
                    .builder()
                    .s(newValue)
                    .build()
            )
            .action(AttributeAction.PUT)
            .build()

        val request = UpdateItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .attributeUpdates(updateValue)
            .build()

        return try {
            dynamoDbClient.updateItem(request)
        } catch (e: ResourceNotFoundException) {
            print(e.stackTrace)
            null
        } catch (e: DynamoDbException) {
            print(e.stackTrace)
            null
        }
    }

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

    fun deleteTable(tableName: String): DeleteTableResponse? {
        val request = DeleteTableRequest.builder()
            .tableName(tableName)
            .build()

        return try {
            dynamoDbClient.deleteTable(request)
        } catch (e: DynamoDbException) {
            System.err.println(e.message)
            null
        }
    }
}
