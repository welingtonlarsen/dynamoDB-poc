package com.dynamoDB

import io.micronaut.runtime.Micronaut.* // ktlint-disable no-wildcard-imports

fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("com.dynamoDB")
		.start()
}
