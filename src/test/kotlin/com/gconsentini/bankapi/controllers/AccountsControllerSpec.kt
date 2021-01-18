package com.gconsentini.bankapi.controllers

import com.gconsentini.bankapi.config.ResourceDataMapper
import com.gconsentini.bankapi.payloads.AccountPayload
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import java.math.BigDecimal
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AccountsControllerSpec{

    @Autowired
    private lateinit var webTestClient: WebTestClient

    companion object {
        private const val API_PATH_CREATE_ACCOUNT = "/accounts/create"
        private const val PATH_MESSAGE = "$.message"
        private const val PATH_STATUS = "$.status"
        private val VALID_ACCOUNT_PAYLOAD = ResourceDataMapper.getFrom("account_payload.json", AccountPayload::class.java) as AccountPayload
    }

    @Test
    fun `given an valid name and document it should create an account`() {
        this.webTestClient.post()
                .uri(API_PATH_CREATE_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(VALID_ACCOUNT_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.name").isEqualTo(VALID_ACCOUNT_PAYLOAD.name)
                .jsonPath("$.document").isEqualTo(VALID_ACCOUNT_PAYLOAD.document)
                .jsonPath("$.balance").isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun `given an invalid document it should not create an account`() {
        this.webTestClient.post()
                .uri(API_PATH_CREATE_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(VALID_ACCOUNT_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.name").isEqualTo(VALID_ACCOUNT_PAYLOAD.name)
                .jsonPath("$.document").isEqualTo(VALID_ACCOUNT_PAYLOAD.document)
                .jsonPath("$.balance").isEqualTo(BigDecimal.ZERO)

        this.webTestClient.post()
                .uri(API_PATH_CREATE_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(VALID_ACCOUNT_PAYLOAD)
                .exchange()
                .expectStatus().is4xxClientError
                .expectBody()
                .jsonPath(PATH_STATUS).isEqualTo(HttpStatus.CONFLICT.value())
                .jsonPath(PATH_MESSAGE).isEqualTo("Conta já existe para esse CPF")

    }

}