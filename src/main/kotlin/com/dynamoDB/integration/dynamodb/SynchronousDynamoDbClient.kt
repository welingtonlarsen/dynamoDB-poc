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

    fun getDynamoDbItem(tableName: String, key: HashMap<String, AttributeValue>): GetItemResponse? {
        val request = GetItemRequest.builder()
            .tableName(tableName)
            .key(key)
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

    fun deleteDynamoDbItem(tableName: String, key: HashMap<String, AttributeValue>): DeleteItemResponse? {
        val request = DeleteItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build()

        return try {
            dynamoDbClient.deleteItem(request)
        } catch (e: DynamoDbException) {
            print(e.stackTrace)
            null
        }
    }
}
