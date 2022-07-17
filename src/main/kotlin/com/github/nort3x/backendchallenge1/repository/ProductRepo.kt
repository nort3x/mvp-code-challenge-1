package com.github.nort3x.backendchallenge1.repository

import com.github.nort3x.backendchallenge1.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.stream.Stream

@Repository
interface ProductRepo : JpaRepository<Product, Long> {
    @Query("select p from Product p where p.seller.username = :username and p.productName = :productName")
    fun getProductByUserAndName(username: String, productName: String): Product?
    @Query("select product from Product product where product.seller.username = :sellerId")
    fun getProductsBySeller(sellerId: String): Stream<Product>

    @Query("select p from Product p")
    fun allProducts(): Stream<Product>
}