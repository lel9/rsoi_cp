package ru.bmstu.cp.rsoi.session.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ComponentScan
@EnableWebMvc
class WebMvcConfiguration : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        val r = registry.addViewController("/login")
        r.setViewName("login")
        registry.setOrder(HIGHEST_PRECEDENCE)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/bootstrap/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap/4.0.0/")
    }
}