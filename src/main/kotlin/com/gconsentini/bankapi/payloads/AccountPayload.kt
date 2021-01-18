package com.gconsentini.bankapi.payloads

import com.gconsentini.bankapi.models.Account
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal
import javax.validation.constraints.NotBlank

data class AccountPayload(
    @field:NotBlank
    val name: String,

    @field:NotBlank
    @field:CPF
    val document: String,

    var balance: BigDecimal = BigDecimal.ZERO
) {
    fun toAccount() = Account(
        name = this.name,
        document = this.document,
        balance = this.balance
    )
}