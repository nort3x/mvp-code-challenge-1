package com.github.nort3x.backendchallenge1.configuration

import com.github.nort3x.backendchallenge1.repository.VendingMachineUserRepo
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class UserDetailServiceImpl(val userRepo: VendingMachineUserRepo) : UserDetailsService {


    override fun loadUserByUsername(username: String?): UserDetails {
        return username?.let {
            userRepo.findById(username).orElse(null)
        } ?: throw UsernameNotFoundException(username)
    }
}