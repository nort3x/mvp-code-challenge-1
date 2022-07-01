package com.github.nort3x.backendchallenge1.repository

import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VendingMachineUserRepo: JpaRepository<VendingMachineUser, String> {
}