package com.github.nort3x.backendchallenge1.controller

import com.github.nort3x.backendchallenge1.configuration.security.AccessDecisionCenter
import com.github.nort3x.backendchallenge1.configuration.security.permission.AuthenticatedClient
import com.github.nort3x.backendchallenge1.configuration.security.permission.EveryOne
import com.github.nort3x.backendchallenge1.dto.Coin
import com.github.nort3x.backendchallenge1.model.UserRegisterDto
import com.github.nort3x.backendchallenge1.model.UserUpdateDto
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.service.UserService
import com.github.nort3x.backendchallenge1.utils.asResponseEntity
import com.github.nort3x.backendchallenge1.utils.withConsiderationOf
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


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
    ): ResponseEntity<VendingMachineUser> =
        withConsiderationOf(ac.userCanModifyUserBy(username)) {
            userService.updateUser(username, userUpdateDto).asResponseEntity()
        }


    @GetMapping("/{username}")
    @AuthenticatedClient
    fun getUserInfo(@PathVariable username: String): ResponseEntity<VendingMachineUser> =
        withConsiderationOf(ac.userCanReadUserBy(username)) {
            userService.getUser(username).asResponseEntity()
        }


    @DeleteMapping("/{username}")
    @AuthenticatedClient
    fun deleteUser(@PathVariable username: String): ResponseEntity<Unit> =
        withConsiderationOf(ac.userCanModifyUserBy(username)) {
            userService.deleteUser(username).asResponseEntity()
        }


    @PostMapping("/deposit")
    @AuthenticatedClient
    fun depositCoin(@RequestBody @Valid coin: Coin) {
        //todo
    }
}
