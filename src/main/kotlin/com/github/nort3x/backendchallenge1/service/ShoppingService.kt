package com.github.nort3x.backendchallenge1.service

import com.github.nort3x.backendchallenge1.dto.BuyResponseDto
import com.github.nort3x.backendchallenge1.exceptions.Insufficient
import com.github.nort3x.backendchallenge1.exceptions.VendingMachineExceptionBase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ShoppingService(
    val productService: ProductService,
    val userService: UserService
) {

    @Transactional(rollbackFor = [VendingMachineExceptionBase::class])
    fun buyProduct(productId: Long, username: String, amount: Int): BuyResponseDto {
        var totalCost = -1
        val product = productService.updateProduct(productId) {
            if (it.amountAvailable - amount < 0)
                throw Insufficient("product not available with given amount: $amount maximum availability is: ${it.amountAvailable}")

            totalCost = amount * it.cost
            it.amountAvailable -= amount
        }

        val user = userService.updateUser(username) {
            if (it.deposit - totalCost < 0)
                throw Insufficient("not enough credit to process this action, you need: ${totalCost - it.deposit} more")
            it.deposit -= totalCost
        }

        return BuyResponseDto(totalCost, product, amount, user)

    }
}