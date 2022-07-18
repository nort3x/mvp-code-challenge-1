package com.github.nort3x.backendchallenge1.repository

import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VendingMachineUserRepo: JpaRepository<VendingMachineUser, Long> , EmittingVendingMachineUserRepo<VendingMachineUser> {

    @Query("select u from VendingMachineUser u where u.username = :userName")
    fun getVendingMachineUserByUsername(userName: String): VendingMachineUser?
}