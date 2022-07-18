package com.github.nort3x.backendchallenge1.dto

import com.github.nort3x.backendchallenge1.validation.MultipleOf
import com.github.nort3x.backendchallenge1.validation.ValidCoin

data class Coin(@field:MultipleOf(5) @field:ValidCoin val coinValue: Int)
