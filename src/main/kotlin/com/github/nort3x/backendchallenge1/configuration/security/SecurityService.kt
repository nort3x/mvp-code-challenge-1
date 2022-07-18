package com.github.nort3x.backendchallenge1.configuration.security

import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.repository.VendingMachineUserRepo
import org.springframework.context.event.EventListener
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Service
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Service
class SecurityService(val userRepo: VendingMachineUserRepo) : HandlerInterceptor {
    fun currentUser(): VendingMachineUser? =
        when (SecurityContextHolder.getContext().authentication.principal) {
            is VendingMachineUser -> SecurityContextHolder.getContext().authentication.principal as VendingMachineUser
            else -> null
        }

    fun currentUserSafe(): VendingMachineUser =
        currentUser()!!

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        updateSecurityContextIfNeeded(request)
        return true
    }

    val updateRequiredUserContexts: MutableSet<String> = Collections.synchronizedSet(mutableSetOf<String>())
    private fun updateSecurityContextIfNeeded(request: HttpServletRequest) {
        val userName = SecurityContextHolder.getContext().authentication?.name
        if (updateRequiredUserContexts.remove(userName)) {
            SecurityContextHolder.clearContext()
            val context = SecurityContextHolder.getContext()
            userRepo.getVendingMachineUserByUsername(userName!!)?.let {
                context.authentication = UserAuthenticationToken(it)
                request.getSession(true)
                    .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context)
            } ?: run {
                request.getSession(false).invalidate()
            }

        }
    }

    @EventListener
    fun securityContextUpdateRequired(userDetailsUpdatedEvent: UserDetailsUpdatedEvent) {
        updateRequiredUserContexts.add(userDetailsUpdatedEvent.username)
    }


}

data class UserDetailsUpdatedEvent(val username: String)