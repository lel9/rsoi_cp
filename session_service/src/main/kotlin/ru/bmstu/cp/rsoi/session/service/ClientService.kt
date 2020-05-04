package ru.bmstu.cp.rsoi.session.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.bmstu.cp.rsoi.session.domain.entity.Client
import ru.bmstu.cp.rsoi.session.model.ClientOutData
import ru.bmstu.cp.rsoi.session.model.ClientRegistrationData
import ru.bmstu.cp.rsoi.session.repository.ClientRepository
import java.util.*

@Service("clientService")
class ClientService {

    @Autowired
    private lateinit var clientRepository: ClientRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    fun registerClient(data: ClientRegistrationData): ClientOutData {

        val secret = UUID.randomUUID().toString()
        val secretEncoded = passwordEncoder.encode(secret)
        val resources = data.resource_ids.joinToString(separator = ",")
        val scopes = data.scope.joinToString(separator = ",")

        val client = Client(secretEncoded, resources, scopes, data.web_server_redirect_uri, data.additional_information)

        val saved = clientRepository.save(client)
        return ClientOutData(saved.client_id, secret)
    }
}