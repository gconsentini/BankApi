package com.gconsentini.bankapi.controllers

import com.gconsentini.bankapi.config.ResourceDataMapper
import com.gconsentini.bankapi.payloads.AccountPayload
import com.gconsentini.bankapi.payloads.TransactionPayload
import com.gconsentini.bankapi.payloads.TransferTransactionPayload
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
import java.math.RoundingMode

@ExtendWith(SpringExtension::class)
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TransactionsControllerSpec{

    @Autowired
    private lateinit var webTestClient: WebTestClient

    companion object {
        private const val API_PATH_CREATE_ACCOUNT = "/accounts/create"
        private const val API_PATH_DEPOSIT_ACCOUNT = "/transactions/deposit"
        private const val API_PATH_TAKEOUT_ACCOUNT = "/transactions/takeout"
        private const val API_PATH_TRANSFER_ACCOUNT = "/transactions/transfer"
        private const val PATH_MESSAGE = "$.message"
        private const val PATH_STATUS = "$.status"
        private val ACCOUNT_PAYLOAD = ResourceDataMapper.getFrom("account_payload.json", AccountPayload::class.java) as AccountPayload
        private val ANOTHER_ACCOUNT_PAYLOAD = ResourceDataMapper.getFrom("another_account_payload.json", AccountPayload::class.java) as AccountPayload
        private val DEPOSIT_PAYLOAD = ResourceDataMapper.getFrom("deposit_payload.json", TransactionPayload::class.java) as TransactionPayload
        private val TAKEOUT_PAYLOAD = ResourceDataMapper.getFrom("takeout_payload.json", TransactionPayload::class.java) as TransactionPayload
        private val TRANSFER_PAYLOAD = ResourceDataMapper.getFrom("transfer_payload.json", TransferTransactionPayload::class.java) as TransferTransactionPayload

    }

    @Test
    fun `Transfer between two differents accounts`() {
        this.webTestClient.post()
                .uri(API_PATH_CREATE_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(ACCOUNT_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.name").isEqualTo(ACCOUNT_PAYLOAD.name)
                .jsonPath("$.document").isEqualTo(ACCOUNT_PAYLOAD.document)
                .jsonPath("$.balance").isEqualTo(BigDecimal.ZERO)

        this.webTestClient.post()
                .uri(API_PATH_CREATE_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(ANOTHER_ACCOUNT_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.name").isEqualTo(ANOTHER_ACCOUNT_PAYLOAD.name)
                .jsonPath("$.document").isEqualTo(ANOTHER_ACCOUNT_PAYLOAD.document)
                .jsonPath("$.balance").isEqualTo(BigDecimal.ZERO)

        this.webTestClient.post()
                .uri(API_PATH_DEPOSIT_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(DEPOSIT_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.balanceBefore").isEqualTo(BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP))
                .jsonPath("$.balanceAfter").isEqualTo(DEPOSIT_PAYLOAD.amount.multiply(BigDecimal(1.005)).setScale(1, RoundingMode.HALF_UP))

        val balanceBefore = DEPOSIT_PAYLOAD.amount.multiply(BigDecimal(1.005)).setScale(1, RoundingMode.HALF_UP)

        this.webTestClient.post()
                .uri(API_PATH_TRANSFER_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(TRANSFER_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.balanceBefore").isEqualTo(balanceBefore.setScale(1, RoundingMode.HALF_UP))
                .jsonPath("$.balanceAfter").isEqualTo(balanceBefore.minus(TRANSFER_PAYLOAD.amount).setScale(1, RoundingMode.HALF_UP))
    }

    @Test
    fun `makes a deposit to an account`() {
        this.webTestClient.post()
                .uri(API_PATH_CREATE_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(ACCOUNT_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.name").isEqualTo(ACCOUNT_PAYLOAD.name)
                .jsonPath("$.document").isEqualTo(ACCOUNT_PAYLOAD.document)
                .jsonPath("$.balance").isEqualTo(BigDecimal.ZERO)

        this.webTestClient.post()
                .uri(API_PATH_DEPOSIT_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(DEPOSIT_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.balanceBefore").isEqualTo(BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP))
                .jsonPath("$.balanceAfter").isEqualTo(DEPOSIT_PAYLOAD.amount.multiply(BigDecimal(1.005)).setScale(1, RoundingMode.HALF_UP))
    }

    @Test
    fun `makes a takeout from an account`() {
        this.webTestClient.post()
                .uri(API_PATH_CREATE_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(ACCOUNT_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.name").isEqualTo(ACCOUNT_PAYLOAD.name)
                .jsonPath("$.document").isEqualTo(ACCOUNT_PAYLOAD.document)
                .jsonPath("$.balance").isEqualTo(BigDecimal.ZERO)

        this.webTestClient.post()
                .uri(API_PATH_DEPOSIT_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(DEPOSIT_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.balanceBefore").isEqualTo(BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP))
                .jsonPath("$.balanceAfter").isEqualTo(DEPOSIT_PAYLOAD.amount.multiply(BigDecimal(1.005)).setScale(1, RoundingMode.HALF_UP))

        val balanceBefore = DEPOSIT_PAYLOAD.amount.multiply(BigDecimal(1.005)).setScale(1, RoundingMode.HALF_UP)

        this.webTestClient.post()
                .uri(API_PATH_TAKEOUT_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(TAKEOUT_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.balanceBefore").isEqualTo(balanceBefore.setScale(1, RoundingMode.HALF_UP))
                .jsonPath("$.balanceAfter").isEqualTo(balanceBefore.minus(TAKEOUT_PAYLOAD.amount.multiply(BigDecimal(1.01))).setScale(1, RoundingMode.HALF_UP))
    }

    @Test
    fun `does not make a takeout from an an invalid transaction`() {
        this.webTestClient.post()
                .uri(API_PATH_CREATE_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(ACCOUNT_PAYLOAD)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.name").isEqualTo(ACCOUNT_PAYLOAD.name)
                .jsonPath("$.document").isEqualTo(ACCOUNT_PAYLOAD.document)
                .jsonPath("$.balance").isEqualTo(BigDecimal.ZERO)

        this.webTestClient.post()
                .uri(API_PATH_TAKEOUT_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(TAKEOUT_PAYLOAD)
                .exchange()
                .expectStatus().is4xxClientError
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.message").isEqualTo("Limite não disponível")
    }

}