package com.gconsentini.bankapi.models

import com.gconsentini.bankapi.enums.TransactionType
import com.gconsentini.bankapi.payloads.TransactionCreatedPayload
import com.gconsentini.bankapi.payloads.TransactionPayload
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity @Table(name = "transaction")
data class Transaction(

    @Id
    @GeneratedValue(generator = "uuid2")
    val id: UUID? = null,
    val type: TransactionType,

    val account: UUID,
    val targetAccount: UUID? = null,
    val amount: BigDecimal,

    val balanceBefore: BigDecimal,
    val balanceAfter: BigDecimal,

    @CreationTimestamp
    val createDateTime: Instant? = Instant.now()
) {
    fun toPayload() = TransactionCreatedPayload (
        accountId = this.account,
        targetAccountId = this.targetAccount,
        amount = this.amount,
        type = this.type,
        balanceBefore = this.balanceBefore,
        balanceAfter = this.balanceAfter,
        createDateTime = this.createDateTime
    )
}
