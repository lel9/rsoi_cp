package ru.bmstu.cp.rsoi.session.domain.entity

import java.io.Serializable
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "oauth_client_details")
class Client(val client_secret: String,
             val resource_ids: String,
             val scope: String,
             val web_server_redirect_uri: String,
             val additional_information: String) : Serializable {

    @Id
    val client_id: String = UUID.randomUUID().toString()

    val authorized_grant_types: String = "authorization_code,refresh_token"

    val authorities: String = "THIRD_PARTY_APP"

    val access_token_validity: Int = 3600

    val refresh_token_validity: Int = 60 * 60 * 24 * 7

    val autoapprove: String = "false"

}
