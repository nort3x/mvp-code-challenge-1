package com.github.nort3x.backendchallenge1.dto

import com.github.nort3x.backendchallenge1.model.Product
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.validation.MultipleOf

data class BuyResponseDto(
    @MultipleOf(5)
    val amountSpent: Int,
    val product: Product,
    val amountBought: Int,
    val currentUserState: VendingMachineUser
)