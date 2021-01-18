package com.gconsentini.bankapi.payloads

import com.gconsentini.bankapi.enums.TransactionType
import com.gconsentini.bankapi.models.Account
import com.gconsentini.bankapi.models.Transaction
import java.math.BigDecimal
import javax.validation.constraints.Positive

data class TransactionPayload(
    val documentAccount: String,
    @field:Positive
    val amount: BigDecimal
): TransactionInterface {
    fun toTransaction(type: TransactionType, account: Account?, balanceBefore: BigDecimal, balanceAfter: BigDecimal) = Transaction(
        account = account?.id!!,
        amount = this.amount,
        balanceBefore = balanceBefore,
        balanceAfter = balanceAfter,
        type = type
    )
}
