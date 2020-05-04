package ru.bmstu.cp.rsoi.session

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer

@SpringBootApplication
@EnableResourceServer
class Application {
}

fun main(args: Array<String>) {
    var pe = BCryptPasswordEncoder()
    var s = pe.encode("secret")
    runApplication<Application>(*args)
}