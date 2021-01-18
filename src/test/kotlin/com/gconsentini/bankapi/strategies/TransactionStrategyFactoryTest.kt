package com.gconsentini.bankapi.strategies

import com.gconsentini.bankapi.enums.TransactionType
import com.gconsentini.bankapi.repositories.TransactionRepository
import com.gconsentini.bankapi.services.AccountService
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class TransactionStrategyFactoryTest {

    companion object {
        private const val DEPOSIT_TAX = "0.005"
        private const val TAKEOUT_TAX = "0.01"
    }

    private val transactionRepository = mockk<TransactionRepository>()
    private val accountService = mockk<AccountService>()
    private val depositStrategy = DepositStrategy(transactionRepository, accountService, DEPOSIT_TAX)
    private val takeoutStrategy = TakeoutStrategy(transactionRepository, accountService, TAKEOUT_TAX)
    private val transferStrategy = TransferStrategy(transactionRepository, accountService)

    private val transactionStrategyFactory = TransactionStrategyFactory(listOf(depositStrategy, takeoutStrategy, transferStrategy))

    @Test
    fun `return deposit strategy`() {
        val strategy = transactionStrategyFactory.getStrategyForType(TransactionType.DEPOSIT)

        assertTrue(strategy is DepositStrategy)
    }

    @Test
    fun `return takeout strategy`() {
        val strategy = transactionStrategyFactory.getStrategyForType(TransactionType.TAKEOUT)

        assertTrue(strategy is TakeoutStrategy)
    }

    @Test
    fun `return transfer strategy`() {
        val strategy = transactionStrategyFactory.getStrategyForType(TransactionType.TRANSFER)

        assertTrue(strategy is TransferStrategy)
    }

}