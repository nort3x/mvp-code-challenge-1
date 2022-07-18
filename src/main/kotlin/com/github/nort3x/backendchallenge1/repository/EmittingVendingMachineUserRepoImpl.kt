package com.github.nort3x.backendchallenge1.repository

import com.github.nort3x.backendchallenge1.configuration.security.UserDetailsUpdatedEvent
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class EmittingVendingMachineUserRepoImpl(
    val applicationEventPublisher: ApplicationEventPublisher
) : EmittingVendingMachineUserRepo<VendingMachineUser> {

    @Autowired
    @org.springframework.context.annotation.Lazy
    lateinit var vendingMachineUserRepo: VendingMachineUserRepo
    override fun saveAndEmit(s: VendingMachineUser): VendingMachineUser {
        return vendingMachineUserRepo.save(s).apply {
            applicationEventPublisher.publishEvent(UserDetailsUpdatedEvent(this.username))
        }
    }

    override fun saveAndFlushAndEmit(s: VendingMachineUser): VendingMachineUser {
        return vendingMachineUserRepo.saveAndFlush(s).apply {
            applicationEventPublisher.publishEvent(UserDetailsUpdatedEvent(this.username))
        }
    }

    override fun deleteAndEmit(userId: Long) {
        val user = vendingMachineUserRepo.findByIdOrNull(userId)
        vendingMachineUserRepo.deleteById(userId)
        user?.username?.let {
            applicationEventPublisher.publishEvent(UserDetailsUpdatedEvent(it))
        }
    }


}