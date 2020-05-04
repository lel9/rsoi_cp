package ru.bmstu.cp.rsoi.session.model

data class ClientRegistrationData (

    var resource_ids: MutableList<String>,

    var scope: MutableList<String>,

    var web_server_redirect_uri: String,

    var additional_information: String

)