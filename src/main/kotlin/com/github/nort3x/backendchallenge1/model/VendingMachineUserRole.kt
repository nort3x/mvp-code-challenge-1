package com.github.nort3x.backendchallenge1.model

import org.springframework.security.core.GrantedAuthority

enum class VendingMachineUserRole : GrantedAuthority {
    SELLER,
    BUYER;

    override fun getAuthority(): String {
        return this.name
    }
}