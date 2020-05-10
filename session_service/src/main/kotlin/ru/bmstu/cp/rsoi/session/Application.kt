package ru.bmstu.cp.rsoi.session

import org.modelmapper.ModelMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer

@SpringBootApplication
@EnableResourceServer
class Application {
    @Bean
    fun modelMapper(): ModelMapper? {
        return ModelMapper()
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}