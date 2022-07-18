package com.github.nort3x.backendchallenge1.service

import com.github.nort3x.backendchallenge1.dto.Coin
import com.github.nort3x.backendchallenge1.exceptions.AlreadyExist
import com.github.nort3x.backendchallenge1.exceptions.NotFound
import com.github.nort3x.backendchallenge1.exceptions.VendingMachineExceptionBase
import com.github.nort3x.backendchallenge1.model.UserUpdateDto
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.model.VendingMachineUserRegisterDto
import com.github.nort3x.backendchallenge1.repository.VendingMachineUserRepo
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(val repo: VendingMachineUserRepo, val passwordEncoder: PasswordEncoder) {

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = [VendingMachineExceptionBase::class])
    @Throws(AlreadyExist::class)
    fun registerNewUser(user: VendingMachineUserRegisterDto): VendingMachineUser {

        try {

            return repo.saveAndFlush(
                VendingMachineUser(user.username, passwordEncoder.encode(user.password), user.role)
            )
        } catch (div: DataIntegrityViolationException) {
            throw AlreadyExist("username: ${user.username} already exist")
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = [VendingMachineExceptionBase::class])
    fun updateUser(
        userId: Long,
        userUpdateDto: UserUpdateDto? = null,
        additionalUpdate: ((VendingMachineUser) -> Unit)? = null
    ): VendingMachineUser {
        val user = getUser(userId)

        userUpdateDto?.password?.let {
            user.password = passwordEncoder.encode(it)
        }
        userUpdateDto?.role?.let {
            user.role = it
        }
        additionalUpdate?.invoke(user)

        return repo.saveAndEmit(user)
    }

    fun getUser(userId: Long): VendingMachineUser =
        repo.findById(userId).orElse(null) ?: throw NotFound("user not found")

    fun deleteUser(userId: Long) =
        repo.deleteAndEmit(userId)

    @Transactional(rollbackFor = [VendingMachineExceptionBase::class])
    fun depositCoin(coin: Coin, userId: Long): VendingMachineUser =
        getUser(userId)
            .apply {
                deposit += coin.coinValue
            }.save()

    private fun VendingMachineUser.save(): VendingMachineUser {
        return repo.saveAndEmit(this)
    }
}