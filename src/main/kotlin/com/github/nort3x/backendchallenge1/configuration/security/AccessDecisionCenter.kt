package com.github.nort3x.backendchallenge1.configuration.security

import org.springframework.stereotype.Service

@Service
class AccessDecisionCenter(val securityService: SecurityService) {



    fun userCanModifyUser(targetResourceId: String) =
        securityService.currentUser()?.username == targetResourceId

    fun userCanReadUser(targetResource: String) =
        securityService.currentUser()?.username == targetResource
}