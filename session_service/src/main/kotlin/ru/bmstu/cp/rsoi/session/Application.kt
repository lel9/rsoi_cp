package ru.bmstu.cp.rsoi.session

import org.modelmapper.ModelMapper
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
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

    @Bean
    fun jsonMessageConverter(): MessageConverter? {
        return Jackson2JsonMessageConverter()
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}