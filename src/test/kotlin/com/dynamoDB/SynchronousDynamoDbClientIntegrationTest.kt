package com.dynamoDB

import com.dynamoDB.integration.dynamodb.SynchronousDynamoDbClient
import com.github.javafaker.Faker
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldNotBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.text.SimpleDateFormat
import java.util.* // ktlint-disable no-wildcard-imports
import javax.inject.Inject
import kotlin.collections.HashMap

@MicronautTest
class SynchronousDynamoDbClientIntegrationTest : BehaviorSpec() {
    @Inject
    private lateinit var dynamoDbClient: DynamoDbClient

    @Inject
    private lateinit var sychronousDynamoDbClient: SynchronousDynamoDbClient

    @Inject
    private lateinit var fakeDataGenerator: Faker

    init {
        Given("a user database item") {
            val validKey = fakeDataGenerator.idNumber().valid()
            val creationDate: Date = fakeDataGenerator.date().birthday()
            val formatter = SimpleDateFormat()
            val formattedDate = formatter.format(creationDate)

            val itemValues = HashMap<String, AttributeValue>()
            itemValues["PK_USER_ID"] = AttributeValue.builder().s(validKey).build()
            itemValues["SK_CREATION_DATE"] = AttributeValue.builder().s(formattedDate).build()
            itemValues["FAVORITE_NUMBER"] = AttributeValue.builder().s(fakeDataGenerator.random().nextDouble().toString()).build()

            When("inserting the user into an existing DynamoDB Table") {
                val insertDynamoDbItem =
                    sychronousDynamoDbClient.insertDynamoDbItem("User", itemValues)
                Then("the item should has been inserted") {
                    insertDynamoDbItem shouldNotBe null
                }
            }
        }
    }
}
