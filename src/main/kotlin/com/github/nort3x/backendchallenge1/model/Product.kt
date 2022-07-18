package com.github.nort3x.backendchallenge1.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.nort3x.backendchallenge1.validation.MultipleOf
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(uniqueConstraints = [UniqueConstraint(name = "uc_name_for_seller", columnNames = ["seller_id", "product_name"])])
class Product(

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "seller_id")
    var seller: VendingMachineUser,

    @Column(name = "product_name")
    var productName: String,

    @MultipleOf(5)
    var cost: Int = 0,

    @PositiveOrZero
    var amountAvailable: Int = 0
) {

    @Id
    @GeneratedValue
    var productId: Long? = null

    @JsonProperty("sellerId")
    fun getSellerId(): Long =
        seller.userId!!


    override fun toString(): String {
        return "Product(seller=${seller}, productName='$productName', cost=$cost, amountAvailable=$amountAvailable)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (seller != other.seller) return false
        if (productName != other.productName) return false
        if (cost != other.cost) return false
        if (amountAvailable != other.amountAvailable) return false
        if (productId != other.productId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = seller.hashCode()
        result = 31 * result + productName.hashCode()
        result = 31 * result + cost
        result = 31 * result + amountAvailable
        result = 31 * result + (productId?.hashCode() ?: 0)
        return result
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
