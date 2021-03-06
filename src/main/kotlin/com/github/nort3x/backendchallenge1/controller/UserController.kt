package com.github.nort3x.backendchallenge1.controller

import com.github.nort3x.backendchallenge1.configuration.security.AccessDecisionCenter
import com.github.nort3x.backendchallenge1.configuration.security.SecurityService
import com.github.nort3x.backendchallenge1.configuration.security.permission.AuthenticatedClient
import com.github.nort3x.backendchallenge1.configuration.security.permission.EveryOne
import com.github.nort3x.backendchallenge1.dto.Coin
import com.github.nort3x.backendchallenge1.model.UserUpdateDto
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.model.VendingMachineUserRegisterDto
import com.github.nort3x.backendchallenge1.service.UserService
import com.github.nort3x.backendchallenge1.utils.asResponseEntity
import com.github.nort3x.backendchallenge1.utils.withConsiderationOf
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.session.SessionRegistry
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/v1/user", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(
    val userService: UserService,
    val ac: AccessDecisionCenter,
    val securityService: SecurityService,
    val sessionRegistry: SessionRegistry
) {
    @PostMapping
    @EveryOne
    fun registerNewUser(@RequestBody vendingMachineUserRegisterDto: VendingMachineUserRegisterDto): ResponseEntity<VendingMachineUser> =
        userService.registerNewUser(vendingMachineUserRegisterDto).asResponseEntity(HttpStatus.CREATED)

    @PutMapping("/{userId}")
    @AuthenticatedClient
    fun updateUser(
        @RequestBody userUpdateDto: UserUpdateDto, @PathVariable userId: Long
    ): ResponseEntity<VendingMachineUser> =
        withConsiderationOf(ac.userCanModifyUserBy(userId, securityService.currentUserSafe())) {
            userService.updateUser(userId, userUpdateDto).asResponseEntity()
        }


    @GetMapping("/{userId}")
    @AuthenticatedClient
    fun getUserInfo(@PathVariable userId: Long): ResponseEntity<VendingMachineUser> =
        withConsiderationOf(ac.userCanReadUserBy(userId, securityService.currentUserSafe())) {
            userService.getUser(userId).asResponseEntity()
        }

    @GetMapping()
    @AuthenticatedClient
    fun getCurrentUserInfo(): ResponseEntity<VendingMachineUser> =
        userService.getUser(securityService.currentUserSafe().userId!!).asResponseEntity()

    @GetMapping("/exist-multiple-sessions")
    @AuthenticatedClient
    fun otherSessionsExist(): ResponseEntity<Boolean> =
        (sessionRegistry.getAllSessions(SecurityContextHolder.getContext().authentication.principal, false).size >= 2)
            .asResponseEntity()


    @DeleteMapping("/{userId}")
    @AuthenticatedClient
    fun deleteUser(@PathVariable userId: Long): ResponseEntity<Unit> =
        withConsiderationOf(ac.userCanModifyUserBy(userId, securityService.currentUserSafe())) {
            userService.deleteUser(userId).asResponseEntity()
        }


    @PostMapping("/deposit")
    @AuthenticatedClient
    fun depositCoin(@RequestBody @Valid coin: Coin): ResponseEntity<VendingMachineUser> =
        withConsiderationOf(ac.userCanDepositCoin(coin, securityService.currentUserSafe())) {
            userService.depositCoin(coin, securityService.currentUserSafe().userId!!).asResponseEntity()
        }

    @AuthenticatedClient
    @PostMapping("/reset")
    fun resetUserDeposit() = withConsiderationOf(ac.userCanResetDeposit(securityService.currentUserSafe())) {
        userService.updateUser(securityService.currentUserSafe().userId!!) {
            it.deposit = 0
        }.asResponseEntity()
    }
}