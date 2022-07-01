package com.github.nort3x.backendchallenge1.repository

import com.github.nort3x.backendchallenge1.model.ProductSellerPK
import com.github.nort3x.backendchallenge1.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepo : JpaRepository<Product, ProductSellerPK> {
}