package com.gconsentini.bankapi.payloads

import com.gconsentini.bankapi.enums.TransactionType
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.Positive

data class TransactionCreatedPayload(
    val accountId: UUID,
    val targetAccountId: UUID?,
    @field:Positive
    val amount: BigDecimal,
    val type: TransactionType,
    val balanceBefore: BigDecimal,
    val balanceAfter: BigDecimal,
    val createDateTime: Instant?
)