package com.gconsentini.bankapi.strategies

import com.gconsentini.bankapi.enums.TransactionType
import com.gconsentini.bankapi.models.Transaction
import com.gconsentini.bankapi.payloads.TransactionInterface
import com.gconsentini.bankapi.payloads.TransactionPayload
import com.gconsentini.bankapi.repositories.TransactionRepository
import com.gconsentini.bankapi.services.AccountService
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

@Service
class TakeoutStrategy(
    private val repository: TransactionRepository,
    private val accountService: AccountService,
    @Value("\${takeout.tax}") private var takeoutPercentage: String
) : TransactionStrategy() {

    override fun getType(): TransactionType {
        return TransactionType.TAKEOUT
    }

    override fun getTransactionRepository(): JpaRepository<Transaction, UUID> {
        return repository
    }

    override fun updateBalance(transactionPayload: TransactionInterface): Transaction {
        val transaction = transactionPayload as TransactionPayload
        val amount = calculateAmount(transaction.amount)
        val account = accountService.findByDocument(transaction.documentAccount)
            ?: throw NotFoundException("Conta não encontrada")
        val (balanceBefore, balanceAfter) = accountService.updateBalance(account.id!!, -amount)
        return transaction.toTransaction(TransactionType.TAKEOUT, account = account, balanceBefore, balanceAfter)
    }

    private fun calculateAmount(amount: BigDecimal): BigDecimal {
        return amount.plus(amount.multiply(BigDecimal(takeoutPercentage))).setScale(2, RoundingMode.HALF_UP)
    }

}