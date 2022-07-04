package com.github.nort3x.backendchallenge1.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.nort3x.backendchallenge1.dto.VendingMachineManagedException
import com.github.nort3x.backendchallenge1.utils.logger
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse


@ControllerAdvice
class ExceptionHandlerAdvise {
    @ExceptionHandler(VendingMachineExceptionBase::class, MethodArgumentNotValidException::class)
    fun handleVendingMachineExceptions(
        ex: Throwable,
    ): ResponseEntity<VendingMachineManagedException> =
        when (ex) {
            is VendingMachineExceptionBase ->
                ResponseEntity.status(ex.statusCode)
                    .body(
                        VendingMachineManagedException(
                            ex.javaClass.simpleName,
                            ex.message ?: "no message provided"
                        )
                    )
            is MethodArgumentNotValidException ->
                ResponseEntity.status(400)
                    .body(
                        VendingMachineManagedException(
                            ex.javaClass.simpleName,
                            ex.localizedMessage.substring(ex.localizedMessage.indexOf("default message [")) // ugly hack i know
                        )
                    )
            else -> {
                logger().warn("unhandled exception", ex)
                throw NotImplementedError()
            }
        }
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