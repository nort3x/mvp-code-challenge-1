package com.github.nort3x.backendchallenge1

import com.github.nort3x.backendchallenge1.configuration.security.SecurityService
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.model.VendingMachineUserRole
import com.github.nort3x.backendchallenge1.repository.VendingMachineUserRepo
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*


data class UserDef(val username: String, val role: VendingMachineUserRole, val userId: Long) {}

@Component
@Profile("test")
class UserSwitcher(val userRepo: VendingMachineUserRepo, val securityService: SecurityService) {
    private fun userDef2Authentication(userDef: UserDef): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(
            userRepo.save(
                VendingMachineUser(
                    userDef.username,
                    "nothing",
                    userDef.role
                ).apply { userId = userDef.userId }
            ), null, mutableListOf(SimpleGrantedAuthority(userDef.role.name))
        )
    }

    fun asUser(userDef: UserDef, func: (VendingMachineUser) -> Unit) {
        SecurityContextHolder.getContext().authentication = userDef2Authentication(userDef)
        func(securityService.currentUserSafe())
    }

    @Transactional
    fun asUserWithTransaction(userDef: UserDef, func: (VendingMachineUser) -> Unit) {
        SecurityContextHolder.getContext().authentication = userDef2Authentication(userDef)
        func(securityService.currentUserSafe())
    }

    @Transactional
    fun inTransaction(func: () -> Unit) {
        func()
    }
}

val randomSource = Random()
val randomChars = ('a'..'z').toList() + ('1'..'9').toList() + ('A'..'Z').toList()
fun randomString(len: Int = 10): String {
    return sequence {
        for (i in 0..len)
            yield(randomChars[(randomSource.nextFloat() * randomChars.size).toInt()])
    }.toList().toCharArray().concatToString()
}