package com.github.nort3x.backendchallenge1.controller

import com.github.nort3x.backendchallenge1.configuration.security.AccessDecisionCenter
import com.github.nort3x.backendchallenge1.configuration.security.SecurityService
import com.github.nort3x.backendchallenge1.configuration.security.permission.AuthenticatedClient
import com.github.nort3x.backendchallenge1.model.ProductSellerPK
import com.github.nort3x.backendchallenge1.service.ProductService
import com.github.nort3x.backendchallenge1.service.ShoppingService
import com.github.nort3x.backendchallenge1.utils.asResponseEntity
import com.github.nort3x.backendchallenge1.utils.withConsiderationOf
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/shop", produces = [MediaType.APPLICATION_JSON_VALUE])
class ShoppingController(
    val ac: AccessDecisionCenter,
    val securityService: SecurityService,
    val productService: ProductService,
    val shoppingService: ShoppingService
) {

    @PostMapping("/{sellerId}/{productName}")
    @AuthenticatedClient
    fun buyProduct(
        @PathVariable sellerId: String,
        @PathVariable productName: String,
        @RequestParam amount: Int
    ) {

        val product = productService.getProductById(ProductSellerPK(sellerId, productName))
        withConsiderationOf(ac.userCanBuyProduct(product, securityService.currentUserSafe())) {
                shoppingService
                .buyProduct(product.pk(),securityService.currentUserSafe().username,amount)
                .asResponseEntity()
        }
    }
}