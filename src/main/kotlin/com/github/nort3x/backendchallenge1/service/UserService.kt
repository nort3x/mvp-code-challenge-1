package com.github.nort3x.backendchallenge1.service

import com.github.nort3x.backendchallenge1.dto.UserRegisterDto
import com.github.nort3x.backendchallenge1.exceptions.AlreadyExist
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.repository.VendingMachineUserRepo
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(val repo: VendingMachineUserRepo, val passwordEncoder: PasswordEncoder) {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun registerNewUser(user: UserRegisterDto) {
        repo.findById(user.username).ifPresent {
            throw AlreadyExist("username: ${user.username} already exist")
        }
        repo.save(
            VendingMachineUser(user.username, passwordEncoder.encode(user.password), user.role)
        )
    }

}