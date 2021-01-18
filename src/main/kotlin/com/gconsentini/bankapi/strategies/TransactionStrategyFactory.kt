package com.gconsentini.bankapi.strategies

import com.gconsentini.bankapi.enums.TransactionType
import com.gconsentini.bankapi.strategies.TransactionStrategy
import org.springframework.stereotype.Component


@Component
class TransactionStrategyFactory (
    strategies: List<TransactionStrategy>
)
{
    private val strategyMap: Map<TransactionType, TransactionStrategy> = strategies.associateBy { it.getType() }

    fun getStrategyForType(type: TransactionType): TransactionStrategy? {
        return strategyMap[type]
    }
}