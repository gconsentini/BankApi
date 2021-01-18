package com.gconsentini.bankapi.strategies

import com.gconsentini.bankapi.enums.TransactionType
import com.gconsentini.bankapi.models.Account
import com.gconsentini.bankapi.payloads.TransactionPayload
import com.gconsentini.bankapi.repositories.TransactionRepository
import com.gconsentini.bankapi.services.AccountService
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@ExtendWith(MockKExtension::class)
internal class DepositStrategyTest {

    companion object {
        private const val DEPOSIT_TAX = "0.005"
    }

    private val transactionRepository = mockk<TransactionRepository>()
    private val accountService = mockk<AccountService>()
    private val depositStrategy = DepositStrategy(transactionRepository, accountService, DEPOSIT_TAX)

    @Test
    fun `returns type deposit`() {
        assertEquals(TransactionType.DEPOSIT, depositStrategy.getType())
    }

    @Test
    fun `returns a transaction repository`() {
        assertTrue(depositStrategy.getTransactionRepository() is TransactionRepository)
    }

    @Test
    fun `updates balance of an account`() {
        val depositPayload = TransactionPayload(
                documentAccount = mockedDocumentAccount(),
                amount = BigDecimal(100.0)
        )

        val mockedAccount = mockAccount(UUID.randomUUID())
        coEvery { accountService.findByDocument(any()) } returns mockedAccount
        val amountWithBonus = depositPayload.amount.multiply(BigDecimal.ONE.plus(BigDecimal(DEPOSIT_TAX)))
                .setScale(2, RoundingMode.HALF_UP)

        coEvery { accountService.updateBalance(any(), any()) } returns Pair(BigDecimal.ZERO, amountWithBonus)
        val transaction = depositStrategy.updateBalance(depositPayload)

        assertNull(transaction.id)
        assertEquals(depositPayload.amount, transaction.amount)
        assertEquals(BigDecimal.ZERO, transaction.balanceBefore)
        assertEquals(amountWithBonus, transaction.balanceAfter)
        assertEquals(TransactionType.DEPOSIT, transaction.type)
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

}