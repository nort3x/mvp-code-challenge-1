package com.github.nort3x.backendchallenge1.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.nort3x.backendchallenge1.dto.VendingMachineManagedException
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest
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

@Component
@Primary
class AccessDeniedExceptionHandler(val exceptionHandlerAdvise: ExceptionHandlerAdvise, val objectMapper: ObjectMapper) :
    AccessDeniedHandler {


    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        exceptionHandlerAdvise.handleVendingMachineExceptions(
            Forbidden(
                accessDeniedException.message ?: "no message provided"
            )
        ).let {
            writeToResponse(it, response, objectMapper)
        }
    }
}

@Component
@Primary
class AuthenticationEntryPointImpl(val exceptionHandlerAdvise: ExceptionHandlerAdvise, val objectMapper: ObjectMapper) :
    AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        exceptionHandlerAdvise.handleVendingMachineExceptions(
            UnAuthorized()
        ).let {
            writeToResponse(it, response, objectMapper)
        }
    }
}