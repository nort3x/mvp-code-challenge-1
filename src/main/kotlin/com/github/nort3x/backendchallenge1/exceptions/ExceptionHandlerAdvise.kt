package com.github.nort3x.backendchallenge1.exceptions

import com.github.nort3x.backendchallenge1.dto.VendingMachineManagedException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ExceptionHandlerAdvise {
    @ExceptionHandler(VendingMachineExceptionBase::class)
    fun handleVendingMachineExceptions(
        vendingMachineExceptionBase: VendingMachineExceptionBase,
    ): ResponseEntity<VendingMachineManagedException> =
        ResponseEntity.status(vendingMachineExceptionBase.statusCode)
            .body(
                VendingMachineManagedException(
                    vendingMachineExceptionBase.javaClass.simpleName,
                    vendingMachineExceptionBase.message ?: "no message provided"
                )
            )
}