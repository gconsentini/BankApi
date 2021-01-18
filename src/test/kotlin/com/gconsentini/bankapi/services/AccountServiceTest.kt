package com.gconsentini.bankapi.services

import com.gconsentini.bankapi.exception.AccountAlreadyExistsException
import com.gconsentini.bankapi.exception.InsufficientBalanceException
import com.gconsentini.bankapi.exception.NotFoundException
import com.gconsentini.bankapi.models.Account
import com.gconsentini.bankapi.payloads.AccountPayload
import com.gconsentini.bankapi.repositories.AccountRepository
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal
import java.util.*

@ExtendWith(MockKExtension::class)
internal class AccountServiceTest {

    private val accountRepository = mockk<AccountRepository>()
    private val accountService = AccountService(accountRepository)

    @Test
    fun `creates an account`(){
        val mockedAccount = mockAccount(UUID.randomUUID())

        coEvery { accountRepository.findByDocument(any()) } returns null
        coEvery { accountRepository.save(any()) } returns mockedAccount

        val accountPayload = AccountPayload(
                name = mockedAccount.name,
                document = mockedAccount.document)

        val returnedAccountPayload = accountService.create(accountPayload)

        assertEquals(accountPayload.name, returnedAccountPayload.name)
        assertEquals(accountPayload.document, returnedAccountPayload.document)
        assertEquals(BigDecimal.ZERO, returnedAccountPayload.balance)
    }

    @Test
    fun `thrown an exception when an account already exists`(){
        val mockedAccount = mockAccount(UUID.randomUUID())
        coEvery { accountRepository.findByDocument(any()) } returns mockedAccount

        val accountPayload = AccountPayload(
                name = mockedAccount.name,
                document = mockedAccount.document)

        val exception = { org.junit.jupiter.api.assertThrows<AccountAlreadyExistsException> { runBlocking { accountService.create(accountPayload) } } }
        assertEquals("Conta já existe para esse CPF", exception().message)
    }

    @Test
    fun `throw an exception when an account is not found`(){

        val accountId = UUID.randomUUID()
        coEvery { accountRepository.findByIdOrNull(any()) } returns null

        val exception = { org.junit.jupiter.api.assertThrows<NotFoundException> { runBlocking { accountService.updateBalance(accountId, BigDecimal.ONE) } } }
        assertEquals("Conta $accountId não encontrada", exception().message)
    }

    @Test
    fun `updates the balance of an account`(){
        val mockedAccount = mockAccount(UUID.randomUUID())
        mockedAccount.balance = BigDecimal(10000)

        coEvery { accountRepository.findByIdOrNull(any()) } returns mockedAccount
        coEvery { accountRepository.save(any()) } returns mockedAccount

        val (balanceBefore, balanceAfter) = accountService.updateBalance(UUID.randomUUID(), BigDecimal(10000))

        assertEquals(BigDecimal(10000), balanceBefore)
        assertEquals(BigDecimal(20000), balanceAfter)
    }

    @Test
    fun `throw an exception if the balance is negative`() {
        val mockedAccount = mockAccount(UUID.randomUUID())

        coEvery { accountRepository.findByIdOrNull(any()) } returns mockedAccount

        val exception = { org.junit.jupiter.api.assertThrows<InsufficientBalanceException> { runBlocking { accountService.updateBalance(UUID.randomUUID(), BigDecimal.ZERO.minus(BigDecimal.ONE)) } } }
        assertEquals("Limite não disponível", exception().message)
    }

    private fun mockAccount(randomUUID: UUID?): Account {
        return Account (
                id = randomUUID,
                name = "Gustavo",
                document = "66081949001",
                balance = BigDecimal.ZERO,
        )
    }

}