package ru.bmstu.cp.rsoi.session.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.ClientRegistrationException
import org.springframework.security.oauth2.provider.client.BaseClientDetails
import org.springframework.stereotype.Service
import ru.bmstu.cp.rsoi.session.domain.entity.Client
import ru.bmstu.cp.rsoi.session.repository.ClientRepository


@Service
class CouchbaseClientDetailsService : ClientDetailsService {

    @Autowired
    private lateinit var customClientDetailsRepository: ClientRepository

    @Throws(ClientRegistrationException::class)
    override fun loadClientByClientId(clientId: String): ClientDetails {
        val client: Client = customClientDetailsRepository.findById(clientId).get()
        val resourceIds: String = client.resource_ids
        val scopes: String = client.scope
        val grantTypes: String = client.authorized_grant_types
        val authorities: String = client.authorities
        val redirectUri: String = client.web_server_redirect_uri
        val base = BaseClientDetails(client.client_id, resourceIds, scopes, grantTypes, authorities, redirectUri)
        base.clientSecret = client.client_secret
        base.accessTokenValiditySeconds = client.access_token_validity
        base.refreshTokenValiditySeconds = client.refresh_token_validity
        base.additionalInformation = mapOf(Pair("description", client.additional_information))
        base.setAutoApproveScopes(client.autoapprove.split(","))
        return base
    }
}