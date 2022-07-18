package com.github.nort3x.backendchallenge1.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.nort3x.backendchallenge1.dto.VendingMachineManagedException
import com.github.nort3x.backendchallenge1.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse


@ControllerAdvice
class ExceptionHandlerAdvise {
    @ExceptionHandler(VendingMachineExceptionBase::class)
    fun handleVendingMachineExceptions(
        ex: VendingMachineExceptionBase,
    ): ResponseEntity<VendingMachineManagedException> =
        ResponseEntity.status(ex.statusCode)
            .body(
                VendingMachineManagedException(
                    ex.javaClass.simpleName,
                    ex.message ?: "no message provided"
                )
            )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleConstraintViolationException(
        ex: MethodArgumentNotValidException,
    ): ResponseEntity<VendingMachineManagedException> {
        return ResponseEntity.status(400)
            .body(
                VendingMachineManagedException(
                    ex.javaClass.simpleName,
                    ex.bindingResult.allErrors
                        .asSequence()
                        .mapNotNull { it.defaultMessage }
                        .reduce { s1: String, s2: String ->
                            "${s1}\n${s2}"
                        })
            )
    }

    @ExceptionHandler(Throwable::class)
    fun handleUnhandledExceptions(
        ex: Throwable,
    ): ResponseEntity<VendingMachineManagedException> {
        logger().warn("unhandled exception", ex)
        return ResponseEntity.status(500)
            .body(VendingMachineManagedException(ex.javaClass.simpleName, "unhandled exception"))
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(
        ex: AuthenticationException
    ): ResponseEntity<VendingMachineManagedException> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(VendingMachineManagedException(ex.javaClass.simpleName, "credentials denied"))
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    fun handleAccessDeniedException(
        ex: org.springframework.security.access.AccessDeniedException
    ): ResponseEntity<VendingMachineManagedException> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(VendingMachineManagedException(ex.javaClass.simpleName, "AccessDenied"))
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