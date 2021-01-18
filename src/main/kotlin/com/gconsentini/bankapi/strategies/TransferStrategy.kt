package com.gconsentini.bankapi.strategies

import com.gconsentini.bankapi.enums.TransactionType
import com.gconsentini.bankapi.models.Transaction
import com.gconsentini.bankapi.payloads.TransactionInterface
import com.gconsentini.bankapi.payloads.TransferTransactionPayload
import com.gconsentini.bankapi.repositories.TransactionRepository
import com.gconsentini.bankapi.services.AccountService
import javassist.NotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TransferStrategy(
    private val repository: TransactionRepository,
    private val accountService: AccountService
) : TransactionStrategy() {

    override fun getType(): TransactionType {
        return TransactionType.TRANSFER
    }

    override fun getTransactionRepository(): JpaRepository<Transaction, UUID> {
        return repository
    }

    override fun updateBalance(transactionPayload: TransactionInterface): Transaction {
        val transaction = transactionPayload as TransferTransactionPayload
        val account = accountService.findByDocument(transaction.documentAccount)
            ?: throw NotFoundException("Conta não encontrada")
        val toAccount = transaction.toDocumentAccount.let { accountService.findByDocument(it) }
            ?: throw NotFoundException("Conta destino não encontrada")

        val (balanceBefore, balanceAfter) = accountService.updateBalance(account.id!!, -transaction.amount)
        accountService.updateBalance(toAccount.id!!, transaction.amount)

        return transaction.toTransaction(TransactionType.TRANSFER, account = account, targetAccount = toAccount, balanceBefore, balanceAfter)
    }

}