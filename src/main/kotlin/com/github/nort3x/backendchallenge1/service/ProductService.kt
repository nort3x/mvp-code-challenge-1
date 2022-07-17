package com.github.nort3x.backendchallenge1.service

import com.github.nort3x.backendchallenge1.exceptions.AlreadyExist
import com.github.nort3x.backendchallenge1.exceptions.NotFound
import com.github.nort3x.backendchallenge1.exceptions.VendingMachineExceptionBase
import com.github.nort3x.backendchallenge1.model.Product
import com.github.nort3x.backendchallenge1.model.ProductRegisterDto
import com.github.nort3x.backendchallenge1.model.ProductUpdateDto
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.repository.ProductRepo
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Stream

@Service
class ProductService(val productRepo: ProductRepo) {
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = [VendingMachineExceptionBase::class])
    fun registerNewProduct(productRegisterDto: ProductRegisterDto, vendingMachineUser: VendingMachineUser): Product {
        try {
            val productEntity = Product(vendingMachineUser, productRegisterDto.productName).apply {
                productRegisterDto.cost?.let { this.cost = it }
                productRegisterDto.amountAvailable?.let { this.amountAvailable = it }
            }

            return productRepo.save(productEntity)

        } catch (ex: DataIntegrityViolationException) {
            val actualProduct = productRepo
                .getProductByUserAndName(vendingMachineUser.username, productRegisterDto.productName)
            throw AlreadyExist("product with this name already exist for this user, productId: ${actualProduct?.productId}")
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = [VendingMachineExceptionBase::class])
    fun updateProduct(
        productId: Long,
        updateDto: ProductUpdateDto? = null,
        additionalUpdateHook: ((Product) -> Unit)? = null
    ): Product {

        val product = getProductById(productId)

        updateDto?.cost?.let {
            product.cost = it
        }
        updateDto?.amountAvailable?.let {
            product.amountAvailable = it
        }

        additionalUpdateHook?.invoke(product)

        return productRepo.save(product)
    }

    fun getProductById(productId: Long): Product {
        return productRepo.findById(productId).orElse(null) ?: throw NotFound("product not found: $productId")
    }

    fun deleteProduct(product: Product) {
        productRepo.deleteById(product.productId!!)
    }

    fun getProductsOfUser(sellerId: String): Stream<Product> {
        return productRepo.getProductsBySeller(sellerId)
    }

    fun allProducts(): Stream<Product> {
        return productRepo.allProducts()
    }

}
