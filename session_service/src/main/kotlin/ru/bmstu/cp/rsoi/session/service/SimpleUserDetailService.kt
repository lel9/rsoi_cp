package ru.bmstu.cp.rsoi.session.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.bmstu.cp.rsoi.session.domain.details.CustomUserDetails
import ru.bmstu.cp.rsoi.session.repository.UserRepository

@Service
internal class SimpleUserDetailService @Autowired
constructor(private val repo: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = repo.findByUsername(username) ?: throw UsernameNotFoundException(username)
        return CustomUserDetails(user)
    }

}