package com.github.nort3x.backendchallenge1.controller

import com.github.nort3x.backendchallenge1.dto.UserRegisterDto
import com.github.nort3x.backendchallenge1.service.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/user", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(val userService: UserService) {

    @PostMapping
    fun registerNewUser(@RequestBody userRegisterDto: UserRegisterDto): ResponseEntity<Void> {
        userService.registerNewUser(userRegisterDto)
        return ResponseEntity.ok().build()
    }

}