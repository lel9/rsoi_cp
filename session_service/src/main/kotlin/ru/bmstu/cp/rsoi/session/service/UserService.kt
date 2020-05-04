package ru.bmstu.cp.rsoi.session.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.bmstu.cp.rsoi.session.domain.entity.User
import ru.bmstu.cp.rsoi.session.exception.UserAlreadyExistAuthenticationException
import ru.bmstu.cp.rsoi.session.model.UserRegistrationData
import ru.bmstu.cp.rsoi.session.repository.RoleRepository
import ru.bmstu.cp.rsoi.session.repository.UserRepository
import java.util.*

@Service("userService")
class UserService{

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    fun registerUser(registrationData: UserRegistrationData): UUID {
        val user = userRepository.findByUsername(registrationData.username!!)
        if (user != null) {
            throw UserAlreadyExistAuthenticationException(null)
        }

        val newUser = User(
            registrationData.username!!,
            passwordEncoder.encode(registrationData.password!!)
        )
        val role = roleRepository.findByRolename("ROLE_USER")
        if (role != null) {
            newUser.roles.add(role)
        }

        val saved = userRepository.save(newUser)
        return saved.id
    }
}