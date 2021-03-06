package com.dynamoDB

import com.dynamoDB.integration.dynamodb.SynchronousDynamoDbClient
import com.github.javafaker.Faker
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.* // ktlint-disable no-wildcard-imports
import java.text.SimpleDateFormat
import java.util.* // ktlint-disable no-wildcard-imports
import javax.inject.Inject
import kotlin.collections.HashMap

@MicronautTest
class SynchronousDynamoDbClientIntegrationTest(
    @Inject
    private val dynamoDbClient: DynamoDbClient,
    @Inject
    private val synchronousDynamoDbClient: SynchronousDynamoDbClient,
    @Inject
    private val fakeDataGenerator: Faker,
) : BehaviorSpec() {
    override fun beforeSpec(spec: Spec) {
        // Creating table User
        val createTableRequest = getCreateTableRequest()
        dynamoDbClient.createTable(createTableRequest)
        super.beforeSpec(spec)
    }

    private fun getCreateTableRequest() = CreateTableRequest.builder()
        .attributeDefinitions(
            AttributeDefinition.builder()
                .attributeName("PK_USER_ID")
                .attributeType(ScalarAttributeType.S)
                .build(),
            AttributeDefinition.builder()
                .attributeName("SK_CREATION_DATE")
                .attributeType(ScalarAttributeType.S)
                .build()
        )
        .keySchema(
            KeySchemaElement.builder()
                .attributeName("PK_USER_ID")
                .keyType(KeyType.HASH)
                .build(),
            KeySchemaElement.builder()
                .attributeName("SK_CREATION_DATE")
                .keyType(KeyType.RANGE)
                .build()
        )
        .provisionedThroughput(
            ProvisionedThroughput.builder()
                .readCapacityUnits(2L)
                .writeCapacityUnits(2L)
                .build()
        )
        .tableName("User")
        .build()

    override fun afterSpec(spec: Spec) {
        // Deleting table User
        dynamoDbClient.deleteTable(
            DeleteTableRequest.builder()
                .tableName("User")
                .build()
        )
        super.afterSpec(spec)
    }

    init {
        Given("a user database item") {
            val validKey = fakeDataGenerator.idNumber().valid()
            val creationDate: Date = fakeDataGenerator.date().birthday()
            val formatter = SimpleDateFormat()
            val formattedDate = formatter.format(creationDate)
            val itemValues = HashMap<String, AttributeValue>()
            itemValues["PK_USER_ID"] = AttributeValue.builder().s(validKey).build()
            itemValues["SK_CREATION_DATE"] = AttributeValue.builder().s(formattedDate).build()
            itemValues["FAVORITE_NUMBER"] =
                AttributeValue.builder().s(fakeDataGenerator.random().nextDouble().toString()).build()

            When("inserting the user into an existing DynamoDb Table") {
                val insertDynamoDbItem =
                    synchronousDynamoDbClient.insertDynamoDbItem("User", itemValues)
                Then("the item should has been inserted") {
                    insertDynamoDbItem shouldNotBe null
                }
            }
        }

        Given("a user item persisted in DynamoDb table") {
            // Variables to persist the user
            val tableName = "User"
            val keyValue = fakeDataGenerator.idNumber().valid()
            val creationDate: Date = fakeDataGenerator.date().birthday()
            val formatter = SimpleDateFormat()
            val formattedDate = formatter.format(creationDate)
            val itemValues = HashMap<String, AttributeValue>()
            itemValues["PK_USER_ID"] = AttributeValue.builder().s(keyValue).build()
            itemValues["SK_CREATION_DATE"] = AttributeValue.builder().s(formattedDate).build()
            itemValues["FAVORITE_NUMBER"] =
                AttributeValue.builder().s(fakeDataGenerator.random().nextDouble().toString()).build()

            // Persisting the user
            synchronousDynamoDbClient.insertDynamoDbItem(tableName, itemValues)

            // This key will be used in follows conditions
            val key = hashMapOf<String, AttributeValue>()
            key["PK_USER_ID"] = AttributeValue.builder().s(keyValue).build()
            key["SK_CREATION_DATE"] = AttributeValue.builder().s(formattedDate).build()

            When("requesting the user to DynamoDb table") {
                val requestedUser = synchronousDynamoDbClient.getDynamoDbItem(tableName, key)
                Then("the user should has been found") {
                    requestedUser shouldNotBe null
                }
            }
            When("updating the user") {
                val updatedUser = synchronousDynamoDbClient.updateDynamoDbItem(tableName, key, "FAVORITE_NUMBER", "123")
                Then("the user should has been updated") {
                    updatedUser shouldNotBe null
                }
            }
            When("deleting the user") {
                val deletedUser = synchronousDynamoDbClient.deleteDynamoDbItem(tableName, key)
                Then("the user should has been deleted") {
                    deletedUser shouldNotBe null
                }
            }
        }
    }
}
