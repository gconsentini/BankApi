package com.gconsentini.bankapi.repositories

import com.gconsentini.bankapi.models.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository: JpaRepository<Account, UUID> {

    fun findByDocument(document: String): Account?
}