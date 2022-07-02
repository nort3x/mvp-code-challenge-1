package com.github.nort3x.backendchallenge1.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.nort3x.backendchallenge1.dto.VendingMachineManagedException
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse


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


private fun writeToResponse(
    responseEntity: ResponseEntity<*>,
    servletResponse: HttpServletResponse,
    objectMapper: ObjectMapper
) {
    servletResponse.status = responseEntity.statusCode.value()
    servletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
    servletResponse.writer.write(objectMapper.writeValueAsString(responseEntity.body).apply {
        println(this)
    })
    servletResponse.flushBuffer()
}