package com.github.nort3x.backendchallenge1.repository

import com.github.nort3x.backendchallenge1.model.Product
import com.github.nort3x.backendchallenge1.model.ProductSellerPK
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.stream.Stream

@Repository
interface ProductRepo : JpaRepository<Product, ProductSellerPK> {
    @Query("select product from Product product where product.seller.username = :sellerId")
    fun getProductsBySeller(sellerId: String): Collection<Product>

    @Query("select p from Product p")
    fun allProducts(): Stream<Product>
}