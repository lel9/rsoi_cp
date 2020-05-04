package ru.bmstu.cp.rsoi.session.web.controller;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.cp.rsoi.session.model.UserRegistrationData
import ru.bmstu.cp.rsoi.session.service.UserService
import java.util.*

@RestController
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/user")
    fun addUser(@RequestBody model: UserRegistrationData): UUID {
        return userService.registerUser(model)
    }
}
