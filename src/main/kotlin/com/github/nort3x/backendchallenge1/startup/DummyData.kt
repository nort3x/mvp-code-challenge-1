package com.github.nort3x.backendchallenge1.startup

import com.github.nort3x.backendchallenge1.model.VendingMachineUserRegisterDto
import com.github.nort3x.backendchallenge1.model.VendingMachineUserRole
import com.github.nort3x.backendchallenge1.service.UserService
import com.github.nort3x.backendchallenge1.utils.fireAndForget
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DummyData(val userService: UserService) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        fireAndForget(useFallBackMessage = true, fallbackMessage = "seller already exist") {
            userService.registerNewUser(
                VendingMachineUserRegisterDto(
                    "seller",
                    "password",
                    VendingMachineUserRole.SELLER
                )
            )
        }
        fireAndForget(useFallBackMessage = true, fallbackMessage = "buyer already exist") {
            userService.registerNewUser(
                VendingMachineUserRegisterDto(
                    "buyer",
                    "password",
                    VendingMachineUserRole.BUYER
                )
            )
        }
    }
}