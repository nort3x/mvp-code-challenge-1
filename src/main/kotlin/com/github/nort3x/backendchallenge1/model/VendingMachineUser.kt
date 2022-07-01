package com.github.nort3x.backendchallenge1.model

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


    @Column(nullable = false)
    var deposite: Long = 0 // default zero

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(role)

    override fun getPassword(): String = password

    override fun getUsername(): String = username


    // these methods are generally useful in real-world cases to enforce some security aspects but not in my example,
    // so I will just set default (always GOOD) values

    override fun isAccountNonExpired(): Boolean = false

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = false

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