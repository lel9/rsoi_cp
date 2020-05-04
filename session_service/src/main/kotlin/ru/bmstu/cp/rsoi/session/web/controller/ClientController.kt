package ru.bmstu.cp.rsoi.session.web.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.cp.rsoi.session.model.ClientOutData
import ru.bmstu.cp.rsoi.session.model.ClientRegistrationData
import ru.bmstu.cp.rsoi.session.service.ClientService

@RestController
class ClientController {

    @Autowired
    private lateinit var clientService: ClientService

    @PostMapping("/client")
    fun addClient(@RequestBody model: ClientRegistrationData): ClientOutData {
        return clientService.registerClient(model)
    }
}