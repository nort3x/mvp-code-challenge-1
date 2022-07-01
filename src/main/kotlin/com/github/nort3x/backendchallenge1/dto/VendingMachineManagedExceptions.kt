package com.github.nort3x.backendchallenge1.dto

import java.time.Instant

data class VendingMachineManagedException(
    val type: String,
    val message: String,
    val time: Instant = Instant.now(),
    val overload: Any? = null
) {
}