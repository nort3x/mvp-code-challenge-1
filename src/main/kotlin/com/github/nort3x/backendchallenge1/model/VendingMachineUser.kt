package com.github.nort3x.backendchallenge1.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.nort3x.backendchallenge1.validation.MultipleOf
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import javax.validation.executable.ValidateOnExecution

@Entity
@ValidateOnExecution
class VendingMachineUser(

    @Column(unique = true)
    private var username: String,

    private var password: String,

    @Enumerated(EnumType.STRING)
    var role: VendingMachineUserRole = VendingMachineUserRole.BUYER,
) : UserDetails {

    @Id
    @GeneratedValue
    var userId: Long? = null

    fun setPassword(encodedPassword: String) {
        password = encodedPassword
    }

    @Column(nullable = false)
    @MultipleOf(5)
    var deposit: Long = 0 // default zero

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(role)

    @JsonIgnore
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



    override fun toString(): String {
        return "VendingMachineUser(username='$username', role=$role, deposit=$deposit)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VendingMachineUser

        if (password != other.password) return false
        if (role != other.role) return false
        if (userId != other.userId) return false
        if (deposit != other.deposit) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        return username.hashCode() // because it's unique
    }


}

data class VendingMachineUserRegisterDto(val username: String, val password: String, val role: VendingMachineUserRole)
data class UserUpdateDto(val password: String?, val role: VendingMachineUserRole?)