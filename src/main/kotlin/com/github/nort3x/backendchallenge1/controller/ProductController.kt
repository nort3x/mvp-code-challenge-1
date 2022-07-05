package com.github.nort3x.backendchallenge1.controller

import com.github.nort3x.backendchallenge1.configuration.security.AccessDecisionCenter
import com.github.nort3x.backendchallenge1.configuration.security.SecurityService
import com.github.nort3x.backendchallenge1.configuration.security.permission.AuthenticatedClient
import com.github.nort3x.backendchallenge1.model.Product
import com.github.nort3x.backendchallenge1.model.ProductRegisterDto
import com.github.nort3x.backendchallenge1.model.ProductSellerPK
import com.github.nort3x.backendchallenge1.model.ProductUpdateDto
import com.github.nort3x.backendchallenge1.service.ProductService
import com.github.nort3x.backendchallenge1.utils.asResponseEntity
import com.github.nort3x.backendchallenge1.utils.withConsiderationOf
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.streams.asSequence

@RestController
@RequestMapping("/v1/product", produces = [MediaType.APPLICATION_JSON_VALUE])
@Validated
class ProductController(
    val productService: ProductService,
    val securityService: SecurityService,
    val ac: AccessDecisionCenter
) {

    @PostMapping
    @AuthenticatedClient
    fun registerNewProduct(@RequestBody @Valid productRegisterDto: ProductRegisterDto): ResponseEntity<Product> =
        withConsiderationOf(ac.userCanRegisterProductBy(user = securityService.currentUserSafe())) {
            productService.registerNewProduct(productRegisterDto, securityService.currentUserSafe())
                .asResponseEntity(HttpStatus.CREATED)
        }

    @PutMapping("/{productName}")
    @AuthenticatedClient
    fun updateProduct(
        @PathVariable productName: String,
        @RequestBody @Valid productUpdateDto: ProductUpdateDto
    ): ResponseEntity<Product> =
        productService.updateProduct(
            ProductSellerPK(securityService.currentUserSafe().username, productName),
            productUpdateDto,
            additionalUpdateHook = {
                withConsiderationOf(ac.userCanModifyProductBy(user = securityService.currentUserSafe(), it))
                { /*nothing - using the hook to veto if consideration goes wrong*/ }
            }
        ).asResponseEntity()

    @DeleteMapping("/{productName}")
    @AuthenticatedClient
    fun deleteProduct(
        @PathVariable productName: String,
    ): ResponseEntity<Unit> {

        val product = productService.getProductById(
            ProductSellerPK(
                securityService.currentUserSafe().username,
                productName
            )
        )

        println(product)

        withConsiderationOf(ac.userCanModifyProductBy(user = securityService.currentUserSafe(), product)) {
            productService.deleteProduct(product)
        }

        return Unit.asResponseEntity()
    }

    @GetMapping("/{sellerId}/{productName}")
    @AuthenticatedClient
    fun getProduct(
        @PathVariable sellerId: String,
        @PathVariable productName: String,
    ): ResponseEntity<Product> {
        val product = productService.getProductById(ProductSellerPK(sellerId, productName))
        withConsiderationOf(ac.userCanReadProductBy(user = securityService.currentUserSafe(), product)) {}
        return product.asResponseEntity()
    }


    @GetMapping("/{sellerId}")
    @AuthenticatedClient
    @Transactional
    fun getProductsOfUser(
        @PathVariable sellerId: String,
    ): ResponseEntity<Collection<Product>> {

        val products: Collection<Product> = productService.getProductsOfUser(sellerId)

        return products
            .asSequence()
            .filter { !ac.userCanReadProductBy(user = securityService.currentUserSafe(), it).isDenied }
            .toList().asResponseEntity()
    }


    @GetMapping
    @AuthenticatedClient
    @Transactional
    fun getAllProducts(
    ): ResponseEntity<Collection<Product>> {

        val productsSequence: Sequence<Product> = productService.allProducts().asSequence()

        return productsSequence
            .filter { !ac.userCanReadProductBy(user = securityService.currentUserSafe(), it).isDenied }
            .take(100) // it's a good idea to have a hard limit (because we are not paginating here)
            .toList()
            .asResponseEntity()
    }


}