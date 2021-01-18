package com.gconsentini.bankapi.strategies

import com.gconsentini.bankapi.enums.TransactionType
import com.gconsentini.bankapi.models.Transaction
import com.gconsentini.bankapi.payloads.TransactionCreatedPayload
import com.gconsentini.bankapi.payloads.TransactionInterface
import com.gconsentini.bankapi.payloads.TransactionPayload
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

abstract class TransactionStrategy {
    abstract fun getType(): TransactionType

    abstract fun getTransactionRepository(): JpaRepository<Transaction, UUID>

    abstract fun updateBalance(transactionPayload: TransactionInterface): Transaction

    @Transactional
    open fun execute(transactionPayload: TransactionInterface): TransactionCreatedPayload {
        val transaction = updateBalance(transactionPayload)
        val transactionExecuted = getTransactionRepository().save(transaction)

        return transactionExecuted.toPayload()
    }
}