package com.github.nort3x.backendchallenge1.configuration.security

import com.github.nort3x.backendchallenge1.repository.VendingMachineUserRepo
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

/**
 * simple implementation to query and find users with JPA
 * used in SecurityFilterChain implicitly
 */
@Component
class UserDetailServiceImpl(val userRepo: VendingMachineUserRepo) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        return username?.let {
            userRepo.getVendingMachineUserByUsername(username)
        } ?: throw UsernameNotFoundException(username)
    }
}