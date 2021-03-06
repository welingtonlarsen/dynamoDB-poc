package com.dynamoDB.application.config

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Value
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Factory
class AwsClientFactory(
    @Value("\${cloud.endpoint}") private val endpoint: String?,
    @Value("\${aws.region}") private val region: String?
) {
    @Bean
    @Primary
    fun dynamoDbClient(): DynamoDbClient = DynamoDbClient.builder()
        .region(Region.of(region!!))
        .endpointOverride(URI.create(endpoint!!))
        .build()
}
