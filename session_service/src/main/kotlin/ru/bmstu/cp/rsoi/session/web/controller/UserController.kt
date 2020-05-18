package ru.bmstu.cp.rsoi.session.web.controller;

import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.cp.rsoi.session.domain.entity.User
import ru.bmstu.cp.rsoi.session.model.UserRegistrationData
import ru.bmstu.cp.rsoi.session.service.UserService
import java.util.*

@RestController
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var modelMapper: ModelMapper

    @PostMapping("/registration/user")
    fun addUser(@RequestBody model: UserRegistrationData): UUID {
        val user = modelMapper.map(model, User::class.java)
        return userService.registerUser(user)
    }
}
