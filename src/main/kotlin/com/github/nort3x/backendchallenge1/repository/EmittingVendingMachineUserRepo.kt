package com.github.nort3x.backendchallenge1.repository

import org.springframework.stereotype.Repository

@Repository
interface EmittingVendingMachineUserRepo<S> {
    fun saveAndEmit(s: S): S;
    fun saveAndFlushAndEmit(s: S): S;
    fun deleteAndEmit(userId: Long)
}