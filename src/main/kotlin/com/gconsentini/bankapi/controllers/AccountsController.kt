package com.gconsentini.bankapi.controllers

import com.gconsentini.bankapi.models.Account
import com.gconsentini.bankapi.payloads.AccountPayload
import com.gconsentini.bankapi.services.AccountService
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.tags.Tag
import org.hibernate.validator.constraints.br.CPF
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("accounts")
class AccountsController (
    private val accountService: AccountService
){
    @ApiOperation(value = "Criar uma nova conta", notes = "Cria uma nova conta passando um CPF e um Nome.")
    @PostMapping("/create")
    fun createAccount(@RequestBody @Valid accountPayload: AccountPayload) = accountService.create(accountPayload)

    @ApiOperation(value = "Encontra uma conta", notes = "Encontra uma conta passando um CPF")
    @GetMapping("/find")
    fun findAccount(@RequestParam document: String): Account? {
        return accountService.findByDocument(document = document)
    }

}