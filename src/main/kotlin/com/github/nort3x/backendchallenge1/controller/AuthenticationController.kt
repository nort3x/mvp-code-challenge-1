package com.github.nort3x.backendchallenge1.controller

import com.github.nort3x.backendchallenge1.configuration.security.permission.AuthenticatedClient
import com.github.nort3x.backendchallenge1.configuration.security.permission.EveryOne
import com.github.nort3x.backendchallenge1.dto.LoginDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class AuthenticationController(
    val sessionRegistry: SessionRegistry,
    val authenticationManager: AuthenticationManager
) {

    @GetMapping("/logout/all")
    @AuthenticatedClient
    fun logoutAll() {
        sessionRegistry.getAllSessions(
            SecurityContextHolder.getContext().authentication.principal,
            true
        )
            .forEach {
                it.expireNow()
            }
    }

    @PostMapping("/login")
    @EveryOne
    fun login(@RequestBody loginDto: LoginDto, request: HttpServletRequest): ResponseEntity<Void> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginDto.username,
                loginDto.password
            )
        )

        if (authentication.isAuthenticated) {
            val context = SecurityContextHolder.getContext()
            context.authentication = authentication
            request.getSession(true)
                .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context)
            return ResponseEntity.status(HttpStatus.OK).body(null)
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
    }

    @PostMapping("/logout")
    @AuthenticatedClient
    fun logOut(request: HttpServletRequest) {
        SecurityContextHolder.clearContext()
        request.getSession(false)?.invalidate()
    }
}