package ru.bmstu.cp.rsoi.session.service

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.bmstu.cp.rsoi.session.domain.entity.User
import ru.bmstu.cp.rsoi.session.exception.UserAlreadyExistAuthenticationException
import ru.bmstu.cp.rsoi.session.model.OperationOut
import ru.bmstu.cp.rsoi.session.repository.RoleRepository
import ru.bmstu.cp.rsoi.session.repository.UserRepository
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

@Service("userService")
class UserService{

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    private val log = Logger.getLogger(UserService::class.java.getName())

    fun registerUser(
        newUser: User
    ): UUID {
        val user = userRepository.findByUsername(newUser.username)
        if (user != null) {
            throw UserAlreadyExistAuthenticationException("Пользователь с именем ${newUser.username} уже существует")
        }

        newUser.password = passwordEncoder.encode(newUser.password)
        val role = roleRepository.findByRolename("ROLE_USER")
        if (role != null) {
            newUser.roles = arrayListOf()
            newUser.roles.add(role)
        }

        val saved = userRepository.save(newUser)
        val id = saved.id

        try {
            val routingKey = "operation"
            val operation = OperationOut(newUser.username, "C")
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation)
            log.log(Level.INFO, "Operation was sent to RabbitMQ: $operation")
        } catch (ex: Exception) {
            log.log(Level.SEVERE, ex.message)
        }
        return id
    }
}