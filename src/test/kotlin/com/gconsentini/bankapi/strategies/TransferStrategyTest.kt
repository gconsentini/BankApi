package com.gconsentini.bankapi.strategies

import com.gconsentini.bankapi.enums.TransactionType
import com.gconsentini.bankapi.models.Account
import com.gconsentini.bankapi.payloads.TransferTransactionPayload
import com.gconsentini.bankapi.repositories.TransactionRepository
import com.gconsentini.bankapi.services.AccountService
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.util.*

@ExtendWith(MockKExtension::class)
internal class TransferStrategyTest {

    private val transactionRepository = mockk<TransactionRepository>()
    private val accountService = mockk<AccountService>()
    private val transferStrategy = TransferStrategy(transactionRepository, accountService)

    @Test
    fun `returns type transfer`() {
        assertEquals(TransactionType.TRANSFER, transferStrategy.getType())
    }

    @Test
    fun `returns a transaction repository`() {
        assertTrue(transferStrategy.getTransactionRepository() is TransactionRepository)
    }

    @Test
    fun `updates balance of an account`() {
        val transferPayload = TransferTransactionPayload(
                documentAccount = mockedDocumentAccount(),
                toDocumentAccount = targetMockedDocumentAccount(),
                amount = BigDecimal(100.0)
        )

        val mockedAccount = mockAccount(UUID.randomUUID())
        coEvery { accountService.findByDocument(any()) } returns mockedAccount

        coEvery { accountService.updateBalance(any(), any()) } returns Pair(BigDecimal.ZERO, BigDecimal.ONE)

        val transaction = transferStrategy.updateBalance(transferPayload)

        assertNull(transaction.id)
        assertEquals(transferPayload.amount, transaction.amount)
        assertEquals(BigDecimal.ZERO, transaction.balanceBefore)
        assertEquals(BigDecimal.ONE, transaction.balanceAfter)
        assertEquals(TransactionType.TRANSFER, transaction.type)
    }

    private fun mockAccount(randomUUID: UUID?): Account {
        return Account (
                id = randomUUID,
                name = "Gustavo",
                document = "66081949001",
                balance = BigDecimal.ZERO,
        )
    }

    private fun mockedDocumentAccount () = "66081949001"
    private fun targetMockedDocumentAccount () = "22222222222"

}