package com.github.nort3x.backendchallenge1.controller

import com.github.nort3x.backendchallenge1.configuration.security.AccessDecisionCenter
import com.github.nort3x.backendchallenge1.configuration.security.SecurityService
import com.github.nort3x.backendchallenge1.configuration.security.permission.AuthenticatedClient
import com.github.nort3x.backendchallenge1.dto.BuyResponseDto
import com.github.nort3x.backendchallenge1.service.ProductService
import com.github.nort3x.backendchallenge1.service.ShoppingService
import com.github.nort3x.backendchallenge1.utils.asResponseEntity
import com.github.nort3x.backendchallenge1.utils.withConsiderationOf
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/shop", produces = [MediaType.APPLICATION_JSON_VALUE])
class ShoppingController(
    val ac: AccessDecisionCenter,
    val securityService: SecurityService,
    val productService: ProductService,
    val shoppingService: ShoppingService
) {

    @PostMapping("/{productId}")
    @AuthenticatedClient
    fun buyProduct(
        @PathVariable productId: Long,
        @RequestParam amount: Int
    ): ResponseEntity<BuyResponseDto> {

        val product = productService.getProductById(productId)
        return withConsiderationOf(ac.userCanBuyProduct(product, securityService.currentUserSafe())) {
            shoppingService
                .buyProduct(productId, securityService.currentUserSafe().username, amount)
                .asResponseEntity()
        }
    }
}