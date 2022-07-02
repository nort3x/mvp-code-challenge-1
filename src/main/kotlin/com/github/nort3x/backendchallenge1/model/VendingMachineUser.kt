package com.github.nort3x.backendchallenge1.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
class VendingMachineUser(

    @Id
    private var username: String,

    private var password: String,

    @Enumerated(EnumType.STRING)
    var role: VendingMachineUserRole = VendingMachineUserRole.BUYER,
) : UserDetails {

    fun setPassword(encodedPassword: String ){
        password = encodedPassword
    }

    @Column(nullable = false)
    var deposite: Long = 0 // default zero

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(role)

    override fun getPassword(): String = password

    override fun getUsername(): String = username


    // these methods are generally useful in real-world cases to enforce some security aspects but not in my example,
    // so I will just set default (always GOOD) values

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = true
    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true
    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = true
    @JsonIgnore
    override fun isEnabled(): Boolean = true
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VendingMachineUser

        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }


}

data class UserRegisterDto(val username: String, val password: String, val role: VendingMachineUserRole)
data class UserUpdateDto(val password: String?, val role: VendingMachineUserRole?)