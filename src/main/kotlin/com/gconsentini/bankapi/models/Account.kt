package com.gconsentini.bankapi.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.gconsentini.bankapi.payloads.AccountPayload
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity @Table(name="account")
data class Account(
    @Id
    @GeneratedValue(generator = "uuid2")
    val id: UUID? = null,
    val name: String,

    @Column(unique=true)
    val document: String,
    var balance: BigDecimal,
){
    fun toAccountPayload() = AccountPayload(
        name = this.name,
        document = this.document
    )
}