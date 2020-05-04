package ru.bmstu.cp.rsoi.session.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import ru.bmstu.cp.rsoi.session.service.CouchbaseClientDetailsService
import ru.bmstu.cp.rsoi.session.service.SimpleUserDetailService


@Configuration
@EnableAuthorizationServer
class OAuth2AuthorizationServer : AuthorizationServerConfigurerAdapter() {

    @Autowired
    private lateinit var tokenStore: TokenStore

    @Autowired
    private lateinit var userDetailService: SimpleUserDetailService

    @Autowired
    private lateinit var couchbaseClientDetailsService: CouchbaseClientDetailsService

    private var authenticationManager: AuthenticationManager? = null

    @Autowired
    fun authorizationServerConfig(authenticationManager: AuthenticationManager?) {
        this.authenticationManager = authenticationManager
    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints
            .tokenStore(tokenStore)
            .reuseRefreshTokens(false)
            .userDetailsService(userDetailService)
            .authenticationManager(authenticationManager)
    }

    @Throws(Exception::class)
    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
    }

    @Throws(java.lang.Exception::class)
    override fun configure(configurer: ClientDetailsServiceConfigurer) {
        configurer.withClientDetails(couchbaseClientDetailsService)
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices? {
        val tokenServices = DefaultTokenServices()
        tokenServices.setSupportRefreshToken(true)
        tokenServices.setReuseRefreshToken(false)
        tokenServices.setTokenStore(tokenStore)
        return tokenServices
    }
}