package com.github.nort3x.backendchallenge1

import com.github.nort3x.backendchallenge1.model.VendingMachineUserRegisterDto
import com.github.nort3x.backendchallenge1.model.VendingMachineUserRole
import com.github.nort3x.backendchallenge1.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class BackendChallenge1Application{
    @Bean
    fun onStartUp(userService: UserService) = CommandLineRunner {
        userService.registerNewUser(VendingMachineUserRegisterDto("seller","password",VendingMachineUserRole.SELLER))
        userService.registerNewUser(VendingMachineUserRegisterDto("buyer","password",VendingMachineUserRole.BUYER))
    }
}


fun main(args: Array<String>) {
    runApplication<BackendChallenge1Application>(*args)
}
