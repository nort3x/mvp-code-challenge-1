package com.github.nort3x.backendchallenge1.configuration.security

import com.github.nort3x.backendchallenge1.exceptions.UnAuthorized
import com.github.nort3x.backendchallenge1.model.Product
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.model.VendingMachineUserRole
import org.springframework.stereotype.Service

data class Vetoer private constructor(val isDenied: Boolean, val reason: Throwable) {
    companion object {
        val noError = Vetoer(false, NotImplementedError("this should not be invoked"))
        fun veto(t: Throwable) = Vetoer(true, t)
    }
}

@Service
class AccessDecisionCenter(val securityService: SecurityService) {


    fun userCanModifyUserBy(targetResourceId: String): Vetoer =
        if (securityService.currentUser()?.username == targetResourceId)
            Vetoer.noError
        else Vetoer.veto(UnAuthorized("you can't modify this user"))


    fun userCanReadUserBy(targetResourceId: String): Vetoer =
        if (securityService.currentUser()?.username == targetResourceId)
            Vetoer.noError
        else Vetoer.veto(UnAuthorized("you can't read this user"))


    fun userCanRegisterProductBy(user: VendingMachineUser): Vetoer =
        if (user.role == VendingMachineUserRole.SELLER)
            Vetoer.noError
        else Vetoer.veto(UnAuthorized("only sellers can register new products"))

    fun userCanModifyProductBy(user: VendingMachineUser, product: Product): Vetoer =
        if (product.seller.username == user.username)
            Vetoer.noError
        else Vetoer.veto(UnAuthorized("you can't modify this product"))

    fun userCanReadProductBy(user: VendingMachineUser, product: Product): Vetoer =
        Vetoer.noError // all users can read all products
}