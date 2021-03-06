package com.dynamoDB

import com.github.javafaker.Faker
import io.micronaut.context.annotation.Factory
import java.util.Locale
import javax.inject.Singleton

@Factory
class FakeDataGenerator {
    @Singleton
    fun builder(): Faker = Faker(Locale("pt_BR"))
}
