package com.github.nort3x.backendchallenge1.controller

import com.github.nort3x.backendchallenge1.configuration.security.permission.AuthenticatedClient
import com.github.nort3x.backendchallenge1.configuration.security.permission.EveryOne
import com.github.nort3x.backendchallenge1.dto.UserRegisterDto
import com.github.nort3x.backendchallenge1.dto.UserUpdateDto
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.service.UserService
import com.github.nort3x.backendchallenge1.utils.asOk
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(val userService: UserService) {

    @PostMapping
    @EveryOne
    fun registerNewUser(@RequestBody userRegisterDto: UserRegisterDto): ResponseEntity<VendingMachineUser> =
        userService.registerNewUser(userRegisterDto).asOk()

    @PutMapping("/{username}")
    @AuthenticatedClient
    @PreAuthorize("@accessDecisionCenter.userCanModifyUser(#username)")
    fun updateUser(
        @RequestBody userUpdateDto: UserUpdateDto,
        @PathVariable username: String
    ): ResponseEntity<VendingMachineUser> =
        userService.updateUser(username, userUpdateDto).asOk()

    @GetMapping("/{username}")
    @AuthenticatedClient
    @PreAuthorize("@accessDecisionCenter.userCanReadUser(#username)")
    fun getUserInfo(@PathVariable username: String): ResponseEntity<VendingMachineUser> {

        println(SecurityContextHolder.getContext().authentication)
        return userService.getUser(username).asOk()

    }


    @DeleteMapping("/{username}")
    @AuthenticatedClient
    @PreAuthorize("@accessDecisionCenter.userCanModifyUser(#username)")
    fun deleteUser(@PathVariable username: String): ResponseEntity<Unit> =
        userService.deleteUser(username).asOk()


}