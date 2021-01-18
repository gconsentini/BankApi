package com.gconsentini.bankapi.controllers

import com.gconsentini.bankapi.enums.TransactionType
import com.gconsentini.bankapi.payloads.TransactionPayload
import com.gconsentini.bankapi.payloads.TransferTransactionPayload
import com.gconsentini.bankapi.strategies.TransactionStrategyFactory
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("transactions")
class TransactionsController (
    private val transactionStrategyFactory: TransactionStrategyFactory
){
    @ApiOperation(value = "Deposito", notes = "Deposita o valor passado em uma determinada conta.")
    @PostMapping("/deposit")
    fun depositToAccount(@RequestBody @Valid transactionPayload: TransactionPayload) =
        transactionStrategyFactory.getStrategyForType(TransactionType.DEPOSIT)!!.execute(transactionPayload)

    @ApiOperation(value = "Retirada ou sacar um valor", notes = "Retira um determinado valor de uma conta.")
    @PostMapping("/takeout")
    fun takeoutFromAccount(@RequestBody @Valid transactionPayload: TransactionPayload) =
        transactionStrategyFactory.getStrategyForType(TransactionType.TAKEOUT)!!.execute(transactionPayload)

    @ApiOperation(value = "Transferencia", notes = "Faz transferencia entre duas contas.")
    @PostMapping("/transfer")
    fun transferToAccount(@RequestBody @Valid transactionPayload: TransferTransactionPayload) =
        transactionStrategyFactory.getStrategyForType(TransactionType.TRANSFER)!!.execute(transactionPayload)

}