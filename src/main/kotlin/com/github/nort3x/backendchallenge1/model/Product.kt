package com.github.nort3x.backendchallenge1.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.ManyToOne

class ProductSellerPK(
    var sellerId: String? = null,
    var productName: String? = null
) : java.io.Serializable


@Entity
@IdClass(ProductSellerPK::class)
class Product(
    @ManyToOne
    @Id
    var sellerId: VendingMachineUser,
    @Id
    var productName: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (sellerId != other.sellerId) return false
        if (productName != other.productName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sellerId.hashCode()
        result = 31 * result + productName.hashCode()
        return result
    }
}