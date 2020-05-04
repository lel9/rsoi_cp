package ru.bmstu.cp.rsoi.session.domain.details

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.bmstu.cp.rsoi.session.domain.entity.User


class CustomUserDetails(private val user: User) : UserDetails {

    override fun getUsername(): String {
        return user.id.toString()
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return user.roles
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}