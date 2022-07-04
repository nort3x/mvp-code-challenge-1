package com.github.nort3x.backendchallenge1.configuration.security

import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


@Service
class SecurityService {
    fun currentUser(): VendingMachineUser? =
        when (SecurityContextHolder.getContext().authentication.principal) {
            is VendingMachineUser -> SecurityContextHolder.getContext().authentication.principal as VendingMachineUser
            else -> null
        }

    fun currentUserSafe(): VendingMachineUser =
        currentUser()!!
}