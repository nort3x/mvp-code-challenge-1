package com.github.nort3x.backendchallenge1.configuration.security

import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


@Service
class SecurityService {
    fun currentUser(): VendingMachineUser? =
        when(SecurityContextHolder.getContext().authentication.details){
            is VendingMachineUser -> SecurityContextHolder.getContext().authentication.details as VendingMachineUser
            else -> null
        }
}