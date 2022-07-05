package com.github.nort3x.backendchallenge1.controller

import com.github.nort3x.backendchallenge1.configuration.security.permission.AuthenticatedClient
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.session.SessionRegistry
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LogOutAllController(val sessionRegistry: SessionRegistry) {

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
}