package com.github.nort3x.backendchallenge1.service

import com.github.nort3x.backendchallenge1.exceptions.AlreadyExist
import com.github.nort3x.backendchallenge1.exceptions.NotFound
import com.github.nort3x.backendchallenge1.model.*
import com.github.nort3x.backendchallenge1.repository.ProductRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Stream

@Service
class ProductService(val productRepo: ProductRepo) {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun registerNewProduct(productRegisterDto: ProductRegisterDto, vendingMachineUser: VendingMachineUser): Product {
        val pk = ProductSellerPK(
            vendingMachineUser.username,
            productRegisterDto.productName
        )
        if (productRepo.findById(pk).isEmpty)
            return productRepo.save(Product(vendingMachineUser, productRegisterDto.productName))
        else throw AlreadyExist("product already exist: $pk")
    }

    @Transactional
    fun updateProduct(
        productKey: ProductSellerPK, updateDto: ProductUpdateDto, additionalUpdateHook: ((Product) -> Unit)? = null
    ): Product {

        val product = productRepo.findById(productKey).orElse(null) ?: throw NotFound("product not found $productKey")

        updateDto.cost?.let {
            product.cost = it
        }
        updateDto.amountAvailable?.let {
            product.amountAvailable = it
        }

        additionalUpdateHook?.invoke(product)

        return productRepo.save(product)
    }

    fun getProductById(productKey: ProductSellerPK): Product {
        return productRepo.findById(productKey).orElse(null) ?: throw NotFound("product not found: $productKey")
    }

    fun deleteProduct(product: Product) {
        productRepo.deleteById(product.pk())
    }

    fun getProductsOfUser(sellerId: String): Collection<Product> {
        return productRepo.getProductsBySeller(sellerId)
    }

    fun allProducts(): Stream<Product> {
        return productRepo.allProducts()
    }

}
