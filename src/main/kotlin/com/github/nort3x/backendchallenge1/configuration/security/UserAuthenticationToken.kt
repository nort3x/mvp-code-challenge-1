package com.github.nort3x.backendchallenge1.configuration.security

import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class UserAuthenticationToken(val user: VendingMachineUser): Authentication {
    override fun getName(): String {
        return user.username
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return user.authorities
    }

    override fun getCredentials(): Any {
        return user.password
    }

    override fun getDetails(): Any {
        return user
    }

    override fun getPrincipal(): Any {
        return user
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }
}