package ru.bmstu.cp.rsoi.session.web.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TokenController {
    @Autowired
    private lateinit var tokenServices: ConsumerTokenServices

    @Autowired
    private lateinit var tokenStore: TokenStore

    @PostMapping("/tokens/revoke/{tokenId}")
    fun revokeToken(@PathVariable tokenId: String?): String? {
        tokenServices.revokeToken(tokenId)
        return tokenId
    }

    @PostMapping("/tokens/revokeRefreshToken/{tokenId}")
    fun revokeRefreshToken(@PathVariable tokenId: String?): String? {
        if (tokenStore is JdbcTokenStore) {
            (tokenStore as JdbcTokenStore).removeRefreshToken(tokenId)
        }
        return tokenId
    }
}