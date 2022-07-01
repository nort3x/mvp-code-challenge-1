package com.github.nort3x.backendchallenge1.dto

import com.github.nort3x.backendchallenge1.model.VendingMachineUserRole

data class UserUpdateDto(val password: String?, val role: VendingMachineUserRole?)