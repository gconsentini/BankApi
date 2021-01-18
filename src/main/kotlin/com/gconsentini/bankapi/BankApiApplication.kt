package com.gconsentini.bankapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class BankApiApplication

fun main(args: Array<String>) {
	runApplication<BankApiApplication>(*args)
}
