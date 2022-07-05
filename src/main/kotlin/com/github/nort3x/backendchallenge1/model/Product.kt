package com.github.nort3x.backendchallenge1.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.nort3x.backendchallenge1.validation.MultipleOf
import com.github.nort3x.backendchallenge1.validation.ValidCoin
import javax.persistence.*
import javax.validation.constraints.Min

data class ProductSellerPK(
    var seller: String? = null,
    var productName: String? = null
) : java.io.Serializable


@Entity
@IdClass(ProductSellerPK::class)
class Product(
    @ManyToOne
    @Id
    @JsonIgnore
    @JoinColumn(name = "sellerId")
    var seller: VendingMachineUser,
    @Id
    var productName: String,

    @MultipleOf(5)
    var cost: Int = 0,

    var amountAvailable: Int = 0
) {

    fun getSellerId(): String =
        seller.username


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (seller != other.seller) return false
        if (productName != other.productName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = seller.hashCode()
        result = 31 * result + productName.hashCode()
        return result
    }



    fun pk(): ProductSellerPK = ProductSellerPK(seller.username, productName)
    override fun toString(): String {
        return "Product(seller=${seller}, productName='$productName', cost=$cost, amountAvailable=$amountAvailable)"
    }
}

data class ProductRegisterDto(
    val productName: String,
    @field:MultipleOf(5) val cost: Int? = null,
    @field:Min(0) val amountAvailable: Int? = null
)

data class ProductUpdateDto(
    @field:MultipleOf(5) val cost: Int? = null,
    @field:Min(0) val amountAvailable: Int? = null
)
