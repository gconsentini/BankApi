package com.gconsentini.bankapi.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class ExceptionHandlerController {

    @ExceptionHandler(AccountAlreadyExistsException::class)
    fun handleConflictException(e: AccountAlreadyExistsException): HttpStatus {
        throw ResponseStatusException(HttpStatus.CONFLICT, e.message, e)
    }

    @ExceptionHandler(InsufficientBalanceException::class)
    fun handleInsufficientBalanceException(e: InsufficientBalanceException): HttpStatus {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message, e)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): HttpStatus {
        throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message, e)
    }
}