package com.gconsentini.bankapi.services

import com.gconsentini.bankapi.exception.AccountAlreadyExistsException
import com.gconsentini.bankapi.exception.InsufficientBalanceException
import com.gconsentini.bankapi.exception.NotFoundException
import com.gconsentini.bankapi.models.Account
import com.gconsentini.bankapi.payloads.AccountPayload
import com.gconsentini.bankapi.repositories.AccountRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class AccountService(private val accountRepository: AccountRepository) {

    fun create(accountPayload: AccountPayload): AccountPayload {
        accountRepository.findByDocument(accountPayload.document)?.let { throw AccountAlreadyExistsException("Conta já existe para esse CPF") }
        return accountRepository.save(accountPayload.toAccount()).toAccountPayload()
    }

    fun findByDocument(document: String) = accountRepository.findByDocument(document)

    private fun findByAccountId(accountId: UUID): Account {
        return accountRepository.findByIdOrNull(accountId)
            ?: throw NotFoundException("Conta $accountId não encontrada")
    }

    fun updateBalance(accountId: UUID, value: BigDecimal): Pair<BigDecimal, BigDecimal> {
        val account = findByAccountId(accountId)
        val balanceBefore = account.balance
        val balanceAfter = account.balance + value

        if (balanceAfter < BigDecimal.ZERO) {
                throw InsufficientBalanceException("Limite não disponível")
        }

        return saveBalance(account, balanceBefore, balanceAfter)
    }



    private fun saveBalance(account: Account, balanceBefore: BigDecimal, balanceAfter: BigDecimal): Pair<BigDecimal, BigDecimal> {
        account.balance = balanceAfter

        accountRepository.save(account)

        return Pair(balanceBefore, balanceAfter)
    }

}