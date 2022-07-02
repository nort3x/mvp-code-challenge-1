package com.github.nort3x.backendchallenge1.controller

import com.github.nort3x.backendchallenge1.configuration.security.AccessDecisionCenter
import com.github.nort3x.backendchallenge1.configuration.security.permission.AuthenticatedClient
import com.github.nort3x.backendchallenge1.configuration.security.permission.EveryOne
import com.github.nort3x.backendchallenge1.exceptions.UnAuthorized
import com.github.nort3x.backendchallenge1.model.UserRegisterDto
import com.github.nort3x.backendchallenge1.model.UserUpdateDto
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.service.UserService
import com.github.nort3x.backendchallenge1.utils.asResponseEntity
import com.github.nort3x.backendchallenge1.utils.ifCan
import com.github.nort3x.backendchallenge1.utils.otherwiseThrow
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(val userService: UserService, val ac: AccessDecisionCenter) {

    @PostMapping
    @EveryOne
    fun registerNewUser(@RequestBody userRegisterDto: UserRegisterDto): ResponseEntity<VendingMachineUser> =
        userService.registerNewUser(userRegisterDto).asResponseEntity(HttpStatus.CREATED)

    @PutMapping("/{username}")
    @AuthenticatedClient
    fun updateUser(
        @RequestBody userUpdateDto: UserUpdateDto, @PathVariable username: String
    ): ResponseEntity<VendingMachineUser> = ifCan(ac.userCanModifyUser(username)) {
        userService.updateUser(username, userUpdateDto).asResponseEntity()
    } otherwiseThrow UnAuthorized("you can't modify this user")

    @GetMapping("/{username}")
    @AuthenticatedClient
    fun getUserInfo(@PathVariable username: String): ResponseEntity<VendingMachineUser> =
        ifCan(ac.userCanReadUser(username)) {
            userService.getUser(username).asResponseEntity()
        } otherwiseThrow UnAuthorized("you can't read this user")


    @DeleteMapping("/{username}")
    @AuthenticatedClient
    fun deleteUser(@PathVariable username: String): ResponseEntity<Unit> =
        ifCan(ac.userCanModifyUser(username)) {
            userService.deleteUser(username).asResponseEntity()
        } otherwiseThrow UnAuthorized("you can't delete this user")


}