package com.github.nort3x.backendchallenge1.service

import com.github.nort3x.backendchallenge1.dto.Coin
import com.github.nort3x.backendchallenge1.exceptions.AlreadyExist
import com.github.nort3x.backendchallenge1.exceptions.NotFound
import com.github.nort3x.backendchallenge1.model.VendingMachineUserRegisterDto
import com.github.nort3x.backendchallenge1.model.UserUpdateDto
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.repository.VendingMachineUserRepo
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(val repo: VendingMachineUserRepo, val passwordEncoder: PasswordEncoder) {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun registerNewUser(user: VendingMachineUserRegisterDto): VendingMachineUser {
        repo.findById(user.username).ifPresent {
            throw AlreadyExist("username: ${user.username} already exist")
        }
        return repo.save(
            VendingMachineUser(user.username, passwordEncoder.encode(user.password), user.role)
        )
    }

    @Transactional(propagation = Propagation.REQUIRED)
    fun updateUser(
        username: String,
        userUpdateDto: UserUpdateDto? = null,
        additionalUpdate: ((VendingMachineUser) -> Unit)? = null
    ): VendingMachineUser {
        val user = getUser(username)

        userUpdateDto?.password?.let {
            user.password = passwordEncoder.encode(it)
        }
        userUpdateDto?.role?.let {
            user.role = it
        }
        additionalUpdate?.invoke(user)

        return repo.save(user)
    }

    fun getUser(username: String): VendingMachineUser =
        repo.findById(username).orElse(null) ?: throw NotFound("user not found")

    fun deleteUser(username: String) =
        repo.deleteById(username)

    @Transactional
    fun depositCoin(coin: Coin, username: String): VendingMachineUser =
        getUser(username)
            .apply {
                deposit += coin.value
            }.save()

    private fun VendingMachineUser.save(): VendingMachineUser {
        return repo.save(this)
    }
}